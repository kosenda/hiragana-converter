package ksnd.hiraganaconverter.core.domain.repository

import kotlinx.coroutines.flow.Flow
import ksnd.hiraganaconverter.core.model.ui.FontType
import ksnd.hiraganaconverter.core.model.ui.Theme

interface DataStoreRepository {
    fun theme(): Flow<Theme>
    fun fontType(): Flow<FontType>
    fun enableInAppUpdate(): Flow<Boolean>
    suspend fun updateTheme(newTheme: Theme)
    suspend fun updateFontType(fontType: FontType)
    suspend fun checkIsExceedingMaxLimit(): Boolean
    suspend fun updateUseInAppUpdate(isUsed: Boolean)
}
