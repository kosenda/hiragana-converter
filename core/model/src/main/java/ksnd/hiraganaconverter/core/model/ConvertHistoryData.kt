package ksnd.hiraganaconverter.core.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "convert_history")
data class ConvertHistoryData(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    var time: String,
    var before: String,
    var after: String,
)
