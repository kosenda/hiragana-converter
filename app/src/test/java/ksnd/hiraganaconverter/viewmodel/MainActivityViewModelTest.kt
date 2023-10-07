package ksnd.hiraganaconverter.viewmodel

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
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
        viewModel = MainActivityViewModel(
            dataStoreRepository = dataStoreRepository,
            inAppUpdateManager = inAppUpdateManager,
        )
    }

    @Test
    fun viewModel_init_calledRegisterListener() {
        verify(exactly = 1) { inAppUpdateManager.registerListener(viewModel) }
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
            suspend fun emit(theme: Theme) { this.theme.emit(theme) }
            suspend fun emit(fontType: FontType) { this.fontType.emit(fontType) }
        }
    }
}