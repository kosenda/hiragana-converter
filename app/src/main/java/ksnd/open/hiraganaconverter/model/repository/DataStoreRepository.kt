package ksnd.open.hiraganaconverter.model.repository

import kotlinx.coroutines.flow.Flow
import ksnd.open.hiraganaconverter.view.CustomFont

interface DataStoreRepository {
    fun selectedThemeNum(): Flow<Int>
    fun selectedCustomFont(): Flow<String>
    suspend fun updateThemeNum(newThemeNum: Int)
    suspend fun updateCustomFont(newCustomFont: CustomFont)
    suspend fun checkReachedConvertMaxLimit(today: String): Boolean
}
