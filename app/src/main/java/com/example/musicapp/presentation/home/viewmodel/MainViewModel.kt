package com.example.musicapp.presentation.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.domain.model_data.lyrics.LyricsResponse
import com.example.musicapp.domain.model_data.song_details.DetailsResponse
import com.example.musicapp.data.repository.APIResponse
import com.example.musicapp.data.repository.SongRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val repository: SongRepository): ViewModel() {
    init {
        getSongDetails("Perfect")
    }

    val songs:LiveData<APIResponse<com.example.musicapp.domain.model_data.song_details.DetailsResponse>>
        get() = repository.songs

    val lyrics:LiveData<APIResponse<com.example.musicapp.domain.model_data.lyrics.LyricsResponse>>
        get() = repository.lyrics


    fun getSongDetails(queryName:String){
        viewModelScope.launch(Dispatchers.IO) {
            repository.getSongDetails(queryName)
        }
    }

    fun getSongLyrics(id:Int?){
        viewModelScope.launch(Dispatchers.IO){
            repository.getSongLyrics(id)
        }
    }


}