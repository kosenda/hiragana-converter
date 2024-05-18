package ksnd.hiraganaconverter.feature.converter

import androidx.lifecycle.SavedStateHandle
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import ksnd.hiraganaconverter.core.analytics.MockAnalyticsHelper
import ksnd.hiraganaconverter.core.domain.usecase.ConversionFailedException
import ksnd.hiraganaconverter.core.domain.usecase.ConvertTextUseCase
import ksnd.hiraganaconverter.core.model.ui.HiraKanaType
import ksnd.hiraganaconverter.core.testing.MainDispatcherRule
import ksnd.hiraganaconverter.core.ui.navigation.Nav
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ConvertViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val convertTextUseCase = mockk<ConvertTextUseCase>(relaxUnitFun = true)
    private val viewModel = ConvertViewModel(
        convertTextUseCase = convertTextUseCase,
        analytics = MockAnalyticsHelper(),
        savedStateHandle = SavedStateHandle(),
    )

    @Test
    fun init_receivedText_isUpdatedInputText() {
        val savedStateHandle = SavedStateHandle().apply {
            set(Nav.Converter::receivedText.name, RECEIVED_TEXT)
        }
        val viewModel = ConvertViewModel(
            convertTextUseCase = convertTextUseCase,
            analytics = MockAnalyticsHelper(),
            savedStateHandle = savedStateHandle,
        )

        assertThat(viewModel.uiState.value.inputText).isEqualTo(RECEIVED_TEXT)
    }

    @Test
    fun updateInputText_newInputText_isUpdated() {
        assertThat(viewModel.uiState.value.inputText).isEmpty()

        viewModel.updateInputText(inputText = INPUT_TEXT)

        assertThat(viewModel.uiState.value.inputText).isEqualTo(INPUT_TEXT)
    }

    @Test
    fun updateOutputText_newOutputText_isUpdated() {
        viewModel.updateOutputText(outputText = OUTPUT_TEXT)

        assertThat(viewModel.uiState.value.outputText).isEqualTo(OUTPUT_TEXT)
    }

    @Test
    fun convert_notChangedText_notCalledUseCase() = runTest {
        viewModel.convert()
        coVerify(exactly = 0) { convertTextUseCase(any(), any()) }
    }

    @Test
    fun convert_firstInputText_updateTexts() = runTest {
        coEvery { convertTextUseCase(INPUT_TEXT, any()) } returns OUTPUT_TEXT
        viewModel.updateInputText(INPUT_TEXT)

        viewModel.convert()

        assertThat(viewModel.uiState.value.outputText).isEqualTo(OUTPUT_TEXT)
        assertThat(viewModel.uiState.value.previousInputText).isEqualTo(INPUT_TEXT)
        assertThat(viewModel.uiState.value.convertErrorType).isNull()
        assertThat(viewModel.uiState.value.showErrorCard).isFalse()
        coVerify(exactly = 1) { convertTextUseCase(INPUT_TEXT, any()) }
    }

    @Test
    fun convert_throwException_updateErrorType() = runTest {
        coEvery { convertTextUseCase(INPUT_TEXT, any()) } throws ConversionFailedException
        viewModel.updateInputText(inputText = INPUT_TEXT)
        assertThat(viewModel.uiState.value.convertErrorType).isNull()

        viewModel.convert()

        assertThat(viewModel.uiState.value.convertErrorType).isNotNull()
        assertThat(viewModel.uiState.value.showErrorCard).isTrue()
        coVerify(exactly = 1) { convertTextUseCase(INPUT_TEXT, any()) }
    }

    @Test
    fun changeHiraKanaType_differentType_isUpdatedAndClearPrevious() = runTest {
        assertThat(viewModel.uiState.value.selectedTextType).isEqualTo(HiraKanaType.HIRAGANA)
        coEvery { convertTextUseCase(INPUT_TEXT, HiraKanaType.HIRAGANA) } returns OUTPUT_TEXT
        viewModel.updateInputText(inputText = INPUT_TEXT)
        viewModel.convert()
        assertThat(viewModel.uiState.value.previousInputText).isEqualTo(INPUT_TEXT)

        viewModel.changeHiraKanaType(HiraKanaType.KATAKANA)

        assertThat(viewModel.uiState.value.selectedTextType).isEqualTo(HiraKanaType.KATAKANA)
        assertThat(viewModel.uiState.value.previousInputText).isEmpty()
    }

    @Test
    fun clearConvertErrorType_once_isEmpty() = runTest {
        coEvery { convertTextUseCase(INPUT_TEXT, any()) } throws ConversionFailedException
        viewModel.updateInputText(inputText = INPUT_TEXT)
        viewModel.convert()
        assertThat(viewModel.uiState.value.convertErrorType).isNotNull()

        viewModel.clearConvertErrorType()

        assertThat(viewModel.uiState.value.convertErrorType).isNull()
    }

    @Test
    fun clearAllText_once_isAllEmpty() = runTest {
        coEvery { convertTextUseCase(any(), any()) } throws ConversionFailedException
        viewModel.updateInputText(inputText = INPUT_TEXT)
        viewModel.updateOutputText(outputText = OUTPUT_TEXT)
        viewModel.convert()
        assertThat(viewModel.uiState.value.inputText).isNotEmpty()
        assertThat(viewModel.uiState.value.outputText).isNotEmpty()
        assertThat(viewModel.uiState.value.convertErrorType).isNotNull()
        assertThat(viewModel.uiState.value.showErrorCard).isTrue()

        viewModel.clearAllText()

        assertThat(viewModel.uiState.value.inputText).isEmpty()
        assertThat(viewModel.uiState.value.outputText).isEmpty()
        assertThat(viewModel.uiState.value.convertErrorType).isNull()
        assertThat(viewModel.uiState.value.showErrorCard).isFalse()
    }

    @Test
    fun hideErrorCard_once_isHidden() = runTest {
        coEvery { convertTextUseCase(INPUT_TEXT, any()) } throws ConversionFailedException
        viewModel.updateInputText(inputText = INPUT_TEXT)
        viewModel.convert()
        assertThat(viewModel.uiState.value.showErrorCard).isTrue()

        viewModel.hideErrorCard()

        assertThat(viewModel.uiState.value.showErrorCard).isFalse()
    }

    private companion object {
        private const val RECEIVED_TEXT = "亜あアAa"
        private const val INPUT_TEXT = "漢字"
        private const val OUTPUT_TEXT = "かんじ"
    }
}
