package ksnd.hiraganaconverter.model.repository

import kotlinx.coroutines.flow.Flow
import ksnd.hiraganaconverter.view.FontType

interface DataStoreRepository {
    fun selectedTheme(): Flow<Int>
    fun selectedFontType(): Flow<String>
    suspend fun updateTheme(newThemeNum: Int)
    suspend fun updateCustomFont(fontType: FontType)
    suspend fun checkReachedConvertMaxLimit(today: String): Boolean
}
