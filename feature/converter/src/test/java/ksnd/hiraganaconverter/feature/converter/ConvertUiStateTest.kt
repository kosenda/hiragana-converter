package ksnd.hiraganaconverter.feature.converter

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ConvertUiStateTest {
    private var convertUiState = ConvertUiState()

    @Test
    fun isChangedInputText_firstEnterInputText_isTrue() {
        convertUiState = convertUiState.copy(previousInputText = "", inputText = "temp")
        assertThat(convertUiState.isChangedInputText()).isTrue()
    }

    @Test
    fun isChangedInputText_existPreviousAndEmptyInput_isFalse() {
        convertUiState = convertUiState.copy(previousInputText = "", inputText = "")
        assertThat(convertUiState.isChangedInputText()).isFalse()
    }

    @Test
    fun isChangedInputText_noChangedInput_isFalse() {
        convertUiState = convertUiState.copy(previousInputText = "temp", inputText = "temp")
        assertThat(convertUiState.isChangedInputText()).isFalse()
    }

    @Test
    fun convertUiState_differentInput_isTrue() {
        convertUiState = convertUiState.copy(previousInputText = "", inputText = "temp")
        assertThat(convertUiState.isChangedInputText()).isTrue()
    }
}
