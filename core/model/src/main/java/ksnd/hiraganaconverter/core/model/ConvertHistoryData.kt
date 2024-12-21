package ksnd.hiraganaconverter.core.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity(tableName = "convert_history")
@Serializable
data class ConvertHistoryData(
    @SerialName("id")
    @PrimaryKey(autoGenerate = true) var id: Long = 0,

    @SerialName("time")
    var time: String,

    @SerialName("before")
    var before: String,

    @SerialName("after")
    var after: String,
)
