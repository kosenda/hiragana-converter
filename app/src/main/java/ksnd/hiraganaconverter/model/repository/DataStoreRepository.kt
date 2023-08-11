package ksnd.hiraganaconverter.model.repository

import kotlinx.coroutines.flow.Flow
import ksnd.hiraganaconverter.view.FontType
import ksnd.hiraganaconverter.view.Theme

interface DataStoreRepository {
    fun selectedTheme(): Flow<Theme>
    fun selectedFontType(): Flow<FontType>
    suspend fun updateTheme(newTheme: Theme)
    suspend fun updateFontType(fontType: FontType)
    suspend fun checkIsExceedingMaxLimit(): Boolean
}
