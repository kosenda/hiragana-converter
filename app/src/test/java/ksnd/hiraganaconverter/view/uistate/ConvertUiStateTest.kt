package ksnd.hiraganaconverter.view.uistate

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ConvertUiStateTest {
    private var convertUiState = ConvertUiState()

    @Test
    fun isChangedInputText_emptyPreviousInputText_isFalse() {
        convertUiState = convertUiState.copy(inputText = "")
        assertThat(convertUiState.isChangedInputText(previousInputText = "")).isFalse()
    }

    @Test
    fun isChangedInputText_firstEnterInputText_isTrue() {
        convertUiState = convertUiState.copy(inputText = "temp")
        assertThat(convertUiState.isChangedInputText(previousInputText = "")).isTrue()
    }

    @Test
    fun isChangedInputText_existPreviousAndEmptyInput_isFalse() {
        convertUiState = convertUiState.copy(inputText = "")
        assertThat(convertUiState.isChangedInputText(previousInputText = "temp")).isFalse()
    }

    @Test
    fun isChangedInputText_noChangedInput_isFalse() {
        convertUiState = convertUiState.copy(inputText = "temp")
        assertThat(convertUiState.isChangedInputText(previousInputText = "temp")).isFalse()
    }

    @Test
    fun convertUiState_differentInput_isTrue() {
        convertUiState = convertUiState.copy(inputText = "temp")
        assertThat(convertUiState.isChangedInputText(previousInputText = "previous")).isTrue()
    }
}
