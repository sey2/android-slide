package com.example.slide.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.slide.SlideManager
import com.example.slide.model.SlideViewModel

class SlideViewModelFactory(private val slideManager: SlideManager) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SlideViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SlideViewModel(slideManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
