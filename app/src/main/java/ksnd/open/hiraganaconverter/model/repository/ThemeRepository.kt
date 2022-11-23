package ksnd.open.hiraganaconverter.model.repository

import kotlinx.coroutines.flow.Flow

interface ThemeRepository {
    fun selectedThemeNum(): Flow<Int>
    fun selectedCustomFont(): Flow<String>
}
