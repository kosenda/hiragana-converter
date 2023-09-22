package ksnd.hiraganaconverter.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ksnd.hiraganaconverter.core.model.ConvertHistoryData

@Dao
interface ConvertHistoryDao {
    // ■ CREATE
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertConvertHistory(convertHistoryData: ConvertHistoryData)

    // ■ READ
    @Query("select * from convert_history")
    fun getAllConvertHistory(): Flow<List<ConvertHistoryData>>

    // ■ DELETE
    @Query("delete from convert_history")
    fun deleteAllConvertHistory()

    @Query("delete from convert_history where id = :id")
    fun deleteConvertHistory(id: Long)
}
