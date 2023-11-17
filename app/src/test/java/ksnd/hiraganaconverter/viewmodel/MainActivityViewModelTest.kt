package ksnd.hiraganaconverter.viewmodel

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
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
    private val inAppUpdateManager = mockk<InAppUpdateManager>(relaxed = true)
    private val observeNeedRequestReviewUseCase = mockk<ObserveNeedRequestReviewUseCase>(relaxUnitFun = true)
    private val completedRequestReviewUseCase = mockk<CompletedRequestReviewUseCase>(relaxUnitFun = true)
    private val cancelReviewUseCase = mockk<CancelReviewUseCase>(relaxUnitFun = true)

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
    fun mainActivityViewModel_collect_changeUiState() =
        runTest {
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
    fun startInAppUpdateInstall_state_changeToInstalling() =
        runTest {
            viewModel.uiState.test {
                assertThat(awaitItem().inAppUpdateState).isEqualTo(InAppUpdateState.Requesting)
                viewModel.startInAppUpdateInstall()
                assertThat(awaitItem().inAppUpdateState).isEqualTo(InAppUpdateState.Installing)
                coVerify(exactly = 1) { inAppUpdateManager.startInstall() }
            }
        }

    @Test
    fun requestInAppUpdate_enableInAppUpdate_callRequestUpdate() =
        runTest {
            coEvery { dataStoreRepository.enableInAppUpdate() } returns flowOf(true)
            coEvery { inAppUpdateManager.requestUpdate(any(), any(), any(), any()) } just runs
            assertThat(viewModel.uiState.value.inAppUpdateState).isEqualTo(InAppUpdateState.Requesting)
            viewModel.requestInAppUpdate(mockk(relaxed = true))
            coVerify(exactly = 1) { inAppUpdateManager.requestUpdate(any(), any(), any(), any()) }
        }

    @Test
    fun requestInAppUpdate_disEnableInAppUpdate_notCallRequestUpdate() =
        runTest {
            coEvery { dataStoreRepository.enableInAppUpdate() } returns flowOf(false)
            assertThat(viewModel.uiState.value.inAppUpdateState).isEqualTo(InAppUpdateState.Requesting)
            viewModel.requestInAppUpdate(mockk(relaxed = true))
            assertThat(viewModel.uiState.value.inAppUpdateState).isEqualTo(InAppUpdateState.NotAvailable)
            coVerify(exactly = 0) { inAppUpdateManager.requestUpdate(any(), any(), any(), any()) }
        }

    @Test
    fun updateInAppUpdateState_state_changeUiState() =
        runTest {
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
    }
}
