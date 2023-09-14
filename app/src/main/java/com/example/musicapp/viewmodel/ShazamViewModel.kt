package com.example.musicapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.model_data.shazam_song_details.ShazamSongResponse
import com.example.musicapp.repository.APIResponse
import com.example.musicapp.repository.ShazamSongRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShazamViewModel(private val repository: ShazamSongRepository):ViewModel(){

    val shazamSongs: LiveData<APIResponse<ShazamSongResponse>>
        get() = repository.shazamSongs


    fun getShazamSongDetails(q: String){
        viewModelScope.launch(Dispatchers.IO) {
            repository.getShazamSongDetails(q)
        }
    }

}