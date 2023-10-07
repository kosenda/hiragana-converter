package ksnd.hiraganaconverter.viewmodel

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.InstallStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import ksnd.hiraganaconverter.core.domain.inappupdate.InAppUpdateManager
import ksnd.hiraganaconverter.core.domain.repository.DataStoreRepository
import ksnd.hiraganaconverter.data.inappupdate.InAppUpdateState
import ksnd.hiraganaconverter.view.MainActivityUiState
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val inAppUpdateManager: InAppUpdateManager,
) : ViewModel(), InstallStateUpdatedListener {
    private val _inAppUpdateState: MutableStateFlow<InAppUpdateState> = MutableStateFlow(InAppUpdateState.Requesting)
    val inAppUpdateState: Flow<InAppUpdateState> = _inAppUpdateState.asStateFlow()

    val uiState = combine(
        dataStoreRepository.theme(),
        dataStoreRepository.fontType(),
    ) { theme, fontType ->
        MainActivityUiState(
            theme = theme,
            fontType = fontType,
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
        _inAppUpdateState.value = InAppUpdateState.Installing
        inAppUpdateManager.startInstall()
    }

    suspend fun requestInAppUpdate(activityResultLauncher: ActivityResultLauncher<IntentSenderRequest>) {
        if (dataStoreRepository.enableInAppUpdate().firstOrNull() == true) {
            inAppUpdateManager.requestUpdate(
                activityResultLauncher = activityResultLauncher,
                alreadyDownloaded = { _inAppUpdateState.value = InAppUpdateState.Downloaded },
                notAvailable = { _inAppUpdateState.value = InAppUpdateState.NotAvailable },
                onFailed = { _inAppUpdateState.value = InAppUpdateState.Failed },
            )
        } else {
            _inAppUpdateState.value = InAppUpdateState.NotAvailable
        }
    }

    fun updateInAppUpdateState(state: InAppUpdateState) {
        _inAppUpdateState.value = state
    }

    override fun onStateUpdate(state: InstallState) {
        when (state.installStatus()) {
            InstallStatus.DOWNLOADING -> {
                _inAppUpdateState.value = InAppUpdateState.Downloading(
                    percentage = ((state.bytesDownloaded().toFloat() / state.totalBytesToDownload().toFloat()) * 100).toInt().coerceIn(0, 100),
                )
            }
            InstallStatus.DOWNLOADED -> _inAppUpdateState.value = InAppUpdateState.Downloaded
            InstallStatus.FAILED -> {
                Timber.d("Failed in app update installErrorCode: ${state.installErrorCode()}")
                _inAppUpdateState.value = InAppUpdateState.Failed
            }
            else -> {}
        }
    }

    override fun onCleared() {
        inAppUpdateManager.unregisterListener(this)
    }
}
