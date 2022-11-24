package ksnd.open.hiraganaconverter.model.repository

import kotlinx.coroutines.flow.Flow
import ksnd.open.hiraganaconverter.view.CustomFont

interface DataStoreRepository {
    fun selectedThemeNum(): Flow<Int>
    fun selectedCustomFont(): Flow<String>
    fun updateThemeNum(newThemeNum: Int)
    fun updateCustomFont(newCustomFont: CustomFont)
    fun lastConvertTime(): Flow<String>
    fun convertCount(): Flow<Int>
    fun updateLastConvertTime(lastConvertTime: String)
    fun updateConvertCount(convertCount: Int)
}
