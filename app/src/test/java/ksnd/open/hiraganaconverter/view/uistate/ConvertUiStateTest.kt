package ksnd.open.hiraganaconverter.view.uistate

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ConvertUiStateTest {
    private var convertUiState = ConvertUiState()

    @Test
    fun convertUiState_initialization_isFalse() {
        assertFalse(convertUiState.isChangedInputText(previousInputText = ""))
    }

    @Test
    fun convertUiState_firstEnterInputText_isTrue() {
        convertUiState = convertUiState.copy(inputText = "temp")
        assertTrue(convertUiState.isChangedInputText(previousInputText = ""))
    }

    @Test
    fun convertUiState_notNewEnterInputText_isFalse() {
        assertFalse(convertUiState.isChangedInputText(previousInputText = "temp"))
    }

    @Test
    fun convertUiState_sameInputText_isFalse() {
        convertUiState = convertUiState.copy(inputText = "temp")
        assertFalse(convertUiState.isChangedInputText(previousInputText = "temp"))
    }

    @Test
    fun convertUiState_differentInputText_isTrue() {
        convertUiState = convertUiState.copy(inputText = "input")
        assertTrue(convertUiState.isChangedInputText(previousInputText = "previous"))
    }
}
