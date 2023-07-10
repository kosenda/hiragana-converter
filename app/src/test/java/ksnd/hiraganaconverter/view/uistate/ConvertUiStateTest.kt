package ksnd.hiraganaconverter.view.uistate

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ConvertUiStateTest {
    private var convertUiState = ConvertUiState()

    @Test
    fun convertUiState_initialization_isFalse() {
        assertThat(convertUiState.isChangedInputText(previousInputText = "")).isFalse()
    }

    @Test
    fun convertUiState_firstEnterInputText_isTrue() {
        convertUiState = convertUiState.copy(inputText = "temp")
        assertThat(convertUiState.isChangedInputText(previousInputText = "")).isTrue()
    }

    @Test
    fun convertUiState_notNewEnterInputText_isFalse() {
        assertThat(convertUiState.isChangedInputText(previousInputText = "temp")).isFalse()
    }

    @Test
    fun convertUiState_sameInputText_isFalse() {
        convertUiState = convertUiState.copy(inputText = "temp_")
        assertThat(convertUiState.isChangedInputText(previousInputText = "temp_")).isFalse()
    }

    @Test
    fun convertUiState_differentInputText_isTrue() {
        convertUiState = convertUiState.copy(inputText = "input")
        assertThat(convertUiState.isChangedInputText(previousInputText = "previous")).isTrue()
    }
}
