package com.example.musicapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.musicapp.repository.ShazamSongRepository

class ShazamViewModelFactory(private val repository: ShazamSongRepository):ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ShazamViewModel(repository) as T
    }
}