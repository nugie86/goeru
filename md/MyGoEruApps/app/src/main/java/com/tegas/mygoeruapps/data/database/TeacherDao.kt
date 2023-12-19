package com.tegas.mygoeruapps.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tegas.mygoeruapps.data.response.GuruItem

@Dao
interface TeacherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(teacher: GuruItem)

    @Query("SELECT * FROM teacher")
    fun loadAll(): LiveData<MutableList<GuruItem>>

    @Query("SELECT * FROM teacher WHERE id_user LIKE :id LIMIT 1")
    fun findById(id: String): GuruItem

    @Delete
    fun delete(teacher: GuruItem)
}