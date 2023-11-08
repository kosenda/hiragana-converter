package ksnd.hiraganaconverter.viewmodel

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
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

    private lateinit var viewModel: MainActivityViewModel

    @Before
    fun setUp() {
        viewModel =
            MainActivityViewModel(
                dataStoreRepository = dataStoreRepository,
                inAppUpdateManager = inAppUpdateManager,
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
