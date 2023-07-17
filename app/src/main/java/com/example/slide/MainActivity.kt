package com.example.slide

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.slide.factory.ReactFactory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("Create Model test", "React 1 " + ReactFactory.createSquareSlide(216).toString())
        Log.d("Create Model test", "React 2 " +  ReactFactory.createSquareSlide(384).toString())
        Log.d("Create Model test", "React 3 " +  ReactFactory.createSquareSlide(108).toString())
        Log.d("Create Model test", "React 4 " + ReactFactory.createSquareSlide(233).toString())

    }
}