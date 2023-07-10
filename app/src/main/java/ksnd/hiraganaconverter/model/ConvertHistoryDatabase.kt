package ksnd.hiraganaconverter.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ConvertHistoryData::class], version = 1, exportSchema = false)
abstract class ConvertHistoryDatabase : RoomDatabase() {

    abstract fun convertHistoryDao(): ConvertHistoryDao

    companion object {
        private var singleton: ConvertHistoryDatabase? = null

        fun getInstance(context: Context): ConvertHistoryDatabase =
            singleton ?: buildDatabase(context).also { singleton = it }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            ConvertHistoryDatabase::class.java,
            "history",
        ).build()
    }
}
