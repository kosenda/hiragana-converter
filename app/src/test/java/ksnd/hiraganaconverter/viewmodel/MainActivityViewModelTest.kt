package ksnd.hiraganaconverter.viewmodel

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import app.cash.turbine.test
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.InstallStatus
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import ksnd.hiraganaconverter.core.data.inappupdate.InAppUpdateState
import ksnd.hiraganaconverter.core.domain.inappupdate.InAppUpdateManager
import ksnd.hiraganaconverter.core.domain.repository.DataStoreRepository
import ksnd.hiraganaconverter.core.domain.usecase.CancelReviewUseCase
import ksnd.hiraganaconverter.core.domain.usecase.CompletedRequestReviewUseCase
import ksnd.hiraganaconverter.core.domain.usecase.ObserveNeedRequestReviewUseCase
import ksnd.hiraganaconverter.core.model.ui.FontType
import ksnd.hiraganaconverter.core.model.ui.Theme
import ksnd.hiraganaconverter.core.testing.MainDispatcherRule
import ksnd.hiraganaconverter.view.MainActivityUiState
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainActivityViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val dataStoreRepository = spyk(FakeDataStoreRepository())
    private val observeNeedRequestReviewUseCase = mockk<ObserveNeedRequestReviewUseCase>(relaxUnitFun = true)
    private val completedRequestReviewUseCase = mockk<CompletedRequestReviewUseCase>(relaxUnitFun = true)
    private val cancelReviewUseCase = mockk<CancelReviewUseCase>(relaxUnitFun = true)
    private val inAppUpdateManager = spyk(FakeInAppUpdateManager(scope = mainDispatcherRule.testScope))

    private lateinit var viewModel: MainActivityViewModel

    @Before
    fun setUp() {
        every { observeNeedRequestReviewUseCase() } returns flowOf(false)
        viewModel = MainActivityViewModel(
            dataStoreRepository = dataStoreRepository,
            inAppUpdateManager = inAppUpdateManager,
            observeNeedRequestReviewUseCase = observeNeedRequestReviewUseCase,
            completedRequestReviewUseCase = completedRequestReviewUseCase,
            cancelReviewUseCase = cancelReviewUseCase,
        )
    }

    @Test
    fun mainActivityViewModel_collect_changeUiState() = runTest {
        viewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(MainActivityUiState())
            val nextTheme = Theme.NIGHT
            val nextFontType = FontType.HACHI_MARU_POP
            dataStoreRepository.emit(nextTheme)
            assertThat(awaitItem().theme).isEqualTo(nextTheme)
            dataStoreRepository.emit(nextFontType)
            assertThat(awaitItem().fontType).isEqualTo(nextFontType)
        }
        verify(exactly = 1) { dataStoreRepository.theme() }
        verify(exactly = 1) { dataStoreRepository.fontType() }
    }

    @Test
    fun viewModel_init_calledRegisterListener() {
        verify(exactly = 1) { inAppUpdateManager.registerListener(viewModel) }
    }

    @Test
    fun startInAppUpdateInstall_state_changeToInstalling() = runTest {
        viewModel.uiState.test {
            assertThat(awaitItem().inAppUpdateState).isEqualTo(InAppUpdateState.Requesting)

            viewModel.startInAppUpdateInstall()

            assertThat(awaitItem().inAppUpdateState).isEqualTo(InAppUpdateState.Installing)
            coVerify(exactly = 1) { inAppUpdateManager.startInstall() }
        }
    }

    @Test
    fun requestInAppUpdate_enableInAppUpdate_callRequestUpdate() = runTest {
        coEvery { dataStoreRepository.enableInAppUpdate() } returns flowOf(true)
        assertThat(viewModel.uiState.value.inAppUpdateState).isEqualTo(InAppUpdateState.Requesting)

        viewModel.requestInAppUpdate(mockk(relaxed = true))

        coVerify(exactly = 1) { inAppUpdateManager.requestUpdate(any(), any(), any(), any()) }
    }

    @Test
    fun requestInAppUpdate_disEnableInAppUpdate_notCallRequestUpdate() = runTest {
        coEvery { dataStoreRepository.enableInAppUpdate() } returns flowOf(false)
        assertThat(viewModel.uiState.value.inAppUpdateState).isEqualTo(InAppUpdateState.Requesting)

        viewModel.requestInAppUpdate(mockk(relaxed = true))

        assertThat(viewModel.uiState.value.inAppUpdateState).isEqualTo(InAppUpdateState.NotAvailable)
        coVerify(exactly = 0) { inAppUpdateManager.requestUpdate(any(), any(), any(), any()) }
    }

    @Test
    fun requestInAppUpdate_alreadyDownloaded_changeStateDownloaded() = runTest {
        inAppUpdateManager.setAppUpdateInfoState(state = AppUpdateInfoState.ALREADY_DOWNLOADED)
        coEvery { dataStoreRepository.enableInAppUpdate() } returns flowOf(true)
        assertThat(viewModel.uiState.value.inAppUpdateState).isEqualTo(InAppUpdateState.Requesting)

        viewModel.requestInAppUpdate(mockk(relaxed = true))

        assertThat(viewModel.uiState.value.inAppUpdateState).isEqualTo(InAppUpdateState.Downloaded)
        coVerify(exactly = 1) { inAppUpdateManager.requestUpdate(any(), any(), any(), any()) }
    }

    @Test
    fun requestInAppUpdate_notAvailable_changeStateDownloaded() = runTest {
        inAppUpdateManager.setAppUpdateInfoState(state = AppUpdateInfoState.NOT_AVAILABLE)
        coEvery { dataStoreRepository.enableInAppUpdate() } returns flowOf(true)
        assertThat(viewModel.uiState.value.inAppUpdateState).isEqualTo(InAppUpdateState.Requesting)

        viewModel.requestInAppUpdate(mockk(relaxed = true))

        assertThat(viewModel.uiState.value.inAppUpdateState).isEqualTo(InAppUpdateState.NotAvailable)
        coVerify(exactly = 1) { inAppUpdateManager.requestUpdate(any(), any(), any(), any()) }
    }

    @Test
    fun requestInAppUpdate_failure_changeStateFailed() = runTest {
        inAppUpdateManager.setAppUpdateInfoState(state = AppUpdateInfoState.FAILURE)
        coEvery { dataStoreRepository.enableInAppUpdate() } returns flowOf(true)
        assertThat(viewModel.uiState.value.inAppUpdateState).isEqualTo(InAppUpdateState.Requesting)

        viewModel.requestInAppUpdate(mockk(relaxed = true))

        assertThat(viewModel.uiState.value.inAppUpdateState).isEqualTo(InAppUpdateState.Failed)
        coVerify(exactly = 1) { inAppUpdateManager.requestUpdate(any(), any(), any(), any()) }
    }

    @Test
    fun updateInAppUpdateState_state_changeUiState() = runTest {
        viewModel.uiState.test {
            assertThat(awaitItem().inAppUpdateState).isEqualTo(InAppUpdateState.Requesting)

            viewModel.updateInAppUpdateState(InAppUpdateState.Downloaded)
            assertThat(awaitItem().inAppUpdateState).isEqualTo(InAppUpdateState.Downloaded)

            viewModel.updateInAppUpdateState(InAppUpdateState.Canceled)
            assertThat(awaitItem().inAppUpdateState).isEqualTo(InAppUpdateState.Canceled)
        }
    }

    @Test
    fun completedRequestReview_callUseCase() = runTest {
        viewModel.completedRequestReview()
        coVerify(exactly = 1) { completedRequestReviewUseCase() }
    }

    @Test
    fun cancelReview_callUseCase() = runTest {
        viewModel.cancelledReview()
        coVerify(exactly = 1) { cancelReviewUseCase() }
    }

    @Test
    fun onNetworkConnectivityChanged_changeUiState() = runTest {
        viewModel.uiState.test {
            assertThat(awaitItem().isConnectNetwork).isNull()
            viewModel.onNetworkConnectivityChanged(false)
            assertThat(awaitItem().isConnectNetwork).isFalse()
            viewModel.onNetworkConnectivityChanged(true)
            assertThat(awaitItem().isConnectNetwork).isTrue()
        }
    }

    @Test
    fun onStateUpdate_DOWNLOADING_changeDownloadingAndPercentage() = runTest {
        viewModel.onStateUpdate(MockInstallState(installStatus = InstallStatus.DOWNLOADING, bytesDownloaded = 0, totalBytesToDownload = 1000))
        assertThat(viewModel.uiState.value.inAppUpdateState).isEqualTo(InAppUpdateState.Downloading(percentage = 0))
        viewModel.onStateUpdate(MockInstallState(installStatus = InstallStatus.DOWNLOADING, bytesDownloaded = 100, totalBytesToDownload = 1000))
        assertThat(viewModel.uiState.value.inAppUpdateState).isEqualTo(InAppUpdateState.Downloading(percentage = 10))
        viewModel.onStateUpdate(MockInstallState(installStatus = InstallStatus.DOWNLOADING, bytesDownloaded = 1000, totalBytesToDownload = 1000))
        assertThat(viewModel.uiState.value.inAppUpdateState).isEqualTo(InAppUpdateState.Downloading(percentage = 100))
    }

    @Test
    fun onStateUpdate_DOWNLOADED_changeDownloaded() = runTest {
        viewModel.onStateUpdate(MockInstallState(installStatus = InstallStatus.DOWNLOADED))

        assertThat(viewModel.uiState.value.inAppUpdateState).isEqualTo(InAppUpdateState.Downloaded)
    }

    @Test
    fun onStateUpdate_FAILED_changeFailed() = runTest {
        viewModel.onStateUpdate(MockInstallState(installStatus = InstallStatus.FAILED))

        assertThat(viewModel.uiState.value.inAppUpdateState).isEqualTo(InAppUpdateState.Failed)
    }

    @Test
    fun onStateUpdate_CANCELED_notChange() = runTest {
        val firstState = viewModel.uiState.value.inAppUpdateState

        viewModel.onStateUpdate(MockInstallState(installStatus = InstallStatus.CANCELED))

        assertThat(viewModel.uiState.value.inAppUpdateState).isEqualTo(firstState)
    }

    @Test
    fun onCleared_callUnregisterListener() {
        // Forced testing using Reflection since it is a protected function.
        val onCleared = viewModel.javaClass.getDeclaredMethod("onCleared")
        onCleared.invoke(viewModel)

        coVerify(exactly = 1) { inAppUpdateManager.unregisterListener(any()) }
    }

    companion object {
        class FakeDataStoreRepository : DataStoreRepository {
            private val theme = MutableStateFlow(MainActivityUiState().theme)
            private val fontType = MutableStateFlow(MainActivityUiState().fontType)

            override fun theme(): Flow<Theme> = theme
            override fun fontType(): Flow<FontType> = fontType
            override fun enableInAppUpdate(): Flow<Boolean> = flowOf(false)
            override suspend fun updateTheme(newTheme: Theme) {}
            override suspend fun updateFontType(fontType: FontType) {}
            override suspend fun checkIsExceedingMaxLimit(): Boolean = false
            override suspend fun updateUseInAppUpdate(isUsed: Boolean) {}

            suspend fun emit(theme: Theme) {
                this.theme.emit(theme)
            }

            suspend fun emit(fontType: FontType) {
                this.fontType.emit(fontType)
            }
        }

        class FakeInAppUpdateManager(
            private val scope: TestScope,
        ) : InAppUpdateManager {
            private val appUpdateInfoState = MutableStateFlow(AppUpdateInfoState.AVAILABLE)

            fun setAppUpdateInfoState(state: AppUpdateInfoState) {
                scope.launch {
                    appUpdateInfoState.emit(state)
                }
            }

            override suspend fun requestUpdate(
                activityResultLauncher: ActivityResultLauncher<IntentSenderRequest>,
                alreadyDownloaded: () -> Unit,
                notAvailable: () -> Unit,
                onFailed: () -> Unit,
            ) {
                when (appUpdateInfoState.value) {
                    AppUpdateInfoState.AVAILABLE -> {
                        /* no-op */
                    }

                    AppUpdateInfoState.ALREADY_DOWNLOADED -> alreadyDownloaded()
                    AppUpdateInfoState.NOT_AVAILABLE -> notAvailable()
                    AppUpdateInfoState.FAILURE -> onFailed()
                }
            }

            override fun startInstall() {
                /* no-op */
            }

            override fun registerListener(listener: InstallStateUpdatedListener) {
                /* no-op */
            }

            override fun unregisterListener(listener: InstallStateUpdatedListener) {
                /* no-op */
            }
        }

        class MockInstallState(
            private val installStatus: Int,
            private val bytesDownloaded: Long = 0,
            private val totalBytesToDownload: Long = 0,
        ) : InstallState() {
            override fun bytesDownloaded(): Long = bytesDownloaded
            override fun totalBytesToDownload(): Long = totalBytesToDownload
            override fun installStatus(): Int = installStatus
            override fun installErrorCode(): Int = 0
            override fun packageName(): String = ""
        }

        enum class AppUpdateInfoState {
            ALREADY_DOWNLOADED,
            AVAILABLE,
            NOT_AVAILABLE,
            FAILURE,
        }
    }
}
