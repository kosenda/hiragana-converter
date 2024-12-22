package ksnd.hiraganaconverter.viewmodel

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.InstallStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ksnd.hiraganaconverter.core.data.inappupdate.InAppUpdateState
import ksnd.hiraganaconverter.core.domain.inappupdate.InAppUpdateManager
import ksnd.hiraganaconverter.core.domain.repository.DataStoreRepository
import ksnd.hiraganaconverter.core.domain.usecase.CancelReviewUseCase
import ksnd.hiraganaconverter.core.domain.usecase.CompletedRequestReviewUseCase
import ksnd.hiraganaconverter.core.domain.usecase.ObserveIsRequestingReviewUseCase
import ksnd.hiraganaconverter.view.MainActivityUiState
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val inAppUpdateManager: InAppUpdateManager,
    private val completedRequestReviewUseCase: CompletedRequestReviewUseCase,
    private val cancelReviewUseCase: CancelReviewUseCase,
    observeIsRequestingReviewUseCase: ObserveIsRequestingReviewUseCase,
) : ViewModel(),
    InstallStateUpdatedListener {
    private val inAppUpdateState: MutableStateFlow<InAppUpdateState> = MutableStateFlow(InAppUpdateState.Requesting)
    private val isConnectNetwork: MutableStateFlow<Boolean?> = MutableStateFlow(null)

    val uiState = combine(
        dataStoreRepository.theme(),
        dataStoreRepository.fontType(),
        inAppUpdateState,
        observeIsRequestingReviewUseCase(),
        isConnectNetwork,
    ) { theme, fontType, inAppUpdateState, isRequestingReview, isConnectNetwork ->
        MainActivityUiState(
            theme = theme,
            fontType = fontType,
            inAppUpdateState = inAppUpdateState,
            isRequestingReview = isRequestingReview,
            isConnectNetwork = isConnectNetwork,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = MainActivityUiState(),
    )

    init {
        inAppUpdateManager.registerListener(this)
    }

    fun startInAppUpdateInstall() {
        inAppUpdateState.value = InAppUpdateState.Installing
        inAppUpdateManager.startInstall()
    }

    suspend fun requestInAppUpdate(activityResultLauncher: ActivityResultLauncher<IntentSenderRequest>) {
        if (dataStoreRepository.enableInAppUpdate().firstOrNull() == true) {
            inAppUpdateManager.requestUpdate(
                activityResultLauncher = activityResultLauncher,
                alreadyDownloaded = { inAppUpdateState.value = InAppUpdateState.Downloaded },
                notAvailable = { inAppUpdateState.value = InAppUpdateState.NotAvailable },
                onFailed = { inAppUpdateState.value = InAppUpdateState.Failed },
            )
        } else {
            inAppUpdateState.value = InAppUpdateState.NotAvailable
        }
    }

    fun updateInAppUpdateState(state: InAppUpdateState) {
        inAppUpdateState.value = state
    }

    fun completedRequestReview() {
        viewModelScope.launch {
            completedRequestReviewUseCase()
        }
    }

    fun cancelledReview() {
        viewModelScope.launch {
            cancelReviewUseCase()
        }
    }

    fun onNetworkConnectivityChanged(isConnectNetwork: Boolean) {
        this.isConnectNetwork.value = isConnectNetwork
    }

    override fun onStateUpdate(state: InstallState) {
        when (state.installStatus()) {
            InstallStatus.DOWNLOADING -> {
                inAppUpdateState.value =
                    InAppUpdateState.Downloading(
                        percentage = ((state.bytesDownloaded().toFloat() / state.totalBytesToDownload().toFloat()) * 100).toInt().coerceIn(0, 100),
                    )
            }
            InstallStatus.DOWNLOADED -> inAppUpdateState.value = InAppUpdateState.Downloaded
            InstallStatus.FAILED -> {
                Timber.d("Failed in app update installErrorCode: ${state.installErrorCode()}")
                inAppUpdateState.value = InAppUpdateState.Failed
            }
            else -> {}
        }
    }

    override fun onCleared() {
        inAppUpdateManager.unregisterListener(this)
    }
}
