package com.example.musicapp.presentation.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.domain.model_data.shazam_song_details.ShazamSongResponse
import com.example.musicapp.data.repository.APIResponse
import com.example.musicapp.data.repository.ShazamSongRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShazamViewModel(private val repository: ShazamSongRepository):ViewModel(){

    val shazamSongs: LiveData<APIResponse<com.example.musicapp.domain.model_data.shazam_song_details.ShazamSongResponse>>
        get() = repository.shazamSongs


    fun getShazamSongDetails(q: String){
        viewModelScope.launch(Dispatchers.IO) {
            repository.getShazamSongDetails(q)
        }
    }

}