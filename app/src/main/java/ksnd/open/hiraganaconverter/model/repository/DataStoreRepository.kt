package ksnd.open.hiraganaconverter.model.repository

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    fun selectedThemeNum(): Flow<Int>
    fun selectedCustomFont(): Flow<String>
    fun lastConvertTime(): Flow<String>
    fun convertCount(): Flow<Int>
    fun updateLastConvertTime(lastConvertTime: String)
    fun updateConvertCount(convertCount: Int)
}
