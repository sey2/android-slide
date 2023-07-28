package com.example.slide.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SlideDao {
    @Query("SELECT * FROM slideentity")
    fun getAll(): List<SlideEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(slideEntity: SlideEntity)

    @Delete
    fun delete(slideEntity: SlideEntity)

    @Query("DELETE FROM slideentity")
    fun deleteAll()
}
