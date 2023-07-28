package com.example.slide

import android.app.Application
import androidx.room.Room
import com.example.slide.db.SlideDatabase

class MySlideApplication : Application() {

    companion object {
        lateinit var instance: MySlideApplication
            private set
    }

    lateinit var slideDatabase: SlideDatabase

    override fun onCreate() {
        super.onCreate()
        instance = this

        slideDatabase = Room.databaseBuilder(
            applicationContext,
            SlideDatabase::class.java,
            "slide_database"
        ).build()
    }
}
