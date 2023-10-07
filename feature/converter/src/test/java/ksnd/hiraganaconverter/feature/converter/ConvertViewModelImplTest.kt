package ksnd.hiraganaconverter.feature.converter

import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import ksnd.hiraganaconverter.core.domain.usecase.ConversionFailedException
import ksnd.hiraganaconverter.core.domain.usecase.ConvertTextUseCase
import ksnd.hiraganaconverter.core.model.ui.HiraKanaType
import ksnd.hiraganaconverter.core.testing.MainDispatcherRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ConvertViewModelImplTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val convertTextUseCase = mockk<ConvertTextUseCase>(relaxUnitFun = true)
    private val viewModel = ConvertViewModelImpl(
        convertTextUseCase = convertTextUseCase,
        ioDispatcher = mainDispatcherRule.testDispatcher,
    )

    @Test
    fun updateInputText_newInputText_isUpdated() {
        assertThat(viewModel.uiState.value.inputText).isEqualTo("")
        viewModel.updateInputText("漢字")
        assertThat(viewModel.uiState.value.inputText).isEqualTo("漢字")
    }

    @Test
    fun updateOutputText_newOutputText_isUpdated() {
        assertThat(viewModel.uiState.value.outputText).isEqualTo("")
        viewModel.updateOutputText("かんじ")
        assertThat(viewModel.uiState.value.outputText).isEqualTo("かんじ")
    }

    @Test
    fun convert_notChangedText_notCalledUseCase() = runTest {
        assertThat(viewModel.uiState.value.inputText).isEqualTo("")
        viewModel.convert()
        coVerify(exactly = 0) { convertTextUseCase(any(), any()) }
    }

    @Test
    fun convert_firstInputText_updateTexts() = runTest {
        val inputText = "漢字"
        val outputText = "かんじ"
        coEvery { convertTextUseCase(any(), any()) } returns outputText
        viewModel.updateInputText(inputText)
        viewModel.convert()
        assertThat(viewModel.uiState.value.outputText).isEqualTo(outputText)
        assertThat(viewModel.uiState.value.previousInputText).isEqualTo(inputText)
        assertThat(viewModel.uiState.value.convertErrorType).isNull()
        coVerify(exactly = 1) { convertTextUseCase(any(), any()) }
    }

    @Test
    fun convert_throwException_updateErrorType() = runTest {
        coEvery { convertTextUseCase(any(), any()) } throws ConversionFailedException
        viewModel.updateInputText("漢字")
        assertThat(viewModel.uiState.value.convertErrorType).isNull()
        viewModel.convert()
        assertThat(viewModel.uiState.value.convertErrorType).isNotNull()
        coVerify(exactly = 1) { convertTextUseCase(any(), any()) }
    }

    @Test
    fun changeHiraKanaType_differentType_isUpdatedAndClearPrevious() = runTest {
        assertThat(viewModel.uiState.value.selectedTextType).isEqualTo(HiraKanaType.HIRAGANA)
        viewModel.updateInputText("漢字")
        coEvery { convertTextUseCase(any(), HiraKanaType.HIRAGANA) } returns "かんじ"
        viewModel.convert()
        assertThat(viewModel.uiState.value.previousInputText).isEqualTo("漢字")
        viewModel.changeHiraKanaType(HiraKanaType.KATAKANA)
        assertThat(viewModel.uiState.value.selectedTextType).isEqualTo(HiraKanaType.KATAKANA)
        assertThat(viewModel.uiState.value.previousInputText).isEqualTo("")
    }

    @Test
    fun clearConvertErrorType_once_isEmpty() = runTest {
        coEvery { convertTextUseCase(any(), any()) } throws ConversionFailedException
        viewModel.updateInputText("漢字")
        viewModel.convert()
        assertThat(viewModel.uiState.value.convertErrorType).isNotNull()
        viewModel.clearConvertErrorType()
        assertThat(viewModel.uiState.value.convertErrorType).isNull()
    }

    @Test
    fun clearAllText_once_isAllEmpty() = runTest {
        coEvery { convertTextUseCase(any(), any()) } throws ConversionFailedException
        viewModel.updateInputText("漢字")
        viewModel.updateOutputText("カンジ")
        viewModel.convert()
        assertThat(viewModel.uiState.value.inputText).isNotEmpty()
        assertThat(viewModel.uiState.value.outputText).isNotEmpty()
        assertThat(viewModel.uiState.value.convertErrorType).isNotNull()
        viewModel.clearAllText()
        assertThat(viewModel.uiState.value.inputText).isEmpty()
        assertThat(viewModel.uiState.value.outputText).isEmpty()
        assertThat(viewModel.uiState.value.convertErrorType).isNull()
    }
}