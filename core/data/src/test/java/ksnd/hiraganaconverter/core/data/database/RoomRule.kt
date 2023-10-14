package ksnd.hiraganaconverter.core.data.database

import android.content.Context
import androidx.room.Room
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class RoomRule(context: Context) : TestWatcher() {
    private var database: ConvertHistoryDatabase = Room
        .databaseBuilder(context = context, ConvertHistoryDatabase::class.java, "test")
        .allowMainThreadQueries()
        .build()

    val dao: ConvertHistoryDao = database.convertHistoryDao()

    override fun finished(description: Description) {
        database.close()
    }
}
