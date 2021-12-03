package com.example.milkkhata.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.util.*

@Database(entities = [MyRecord::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun myRecordDao(): MyRecordDao
}

@Entity(tableName = "MyRecord")
data class MyRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var price: Double = 0.0,
    var date: Long = Date().time,
    var quantity: Double = 0.0,
    var quality: Int = 5,
    var note: String? = null,
    var paid: Boolean? = false
) {
    override fun toString() = "id=$id, date=$date, qty=$quantity, qlt=$quality, note=$note, paid=$paid"
}

@Dao
interface MyRecordDao {

    @Query("select * from MyRecord")
    fun getAll(): Flow<List<MyRecord>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(record: MyRecord): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(record: MyRecord): Int
}