package com.example.slide.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [SlideEntity::class], version = 1)
abstract class SlideDatabase : RoomDatabase() {
    abstract fun slideDao(): SlideDao

    companion object {
        @Volatile private var INSTANCE: SlideDatabase? = null

        fun getDatabase(context: Context): SlideDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SlideDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
