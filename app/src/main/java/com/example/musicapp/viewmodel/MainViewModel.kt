package com.example.musicapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.model_data.lyrics.LyricsResponse
import com.example.musicapp.model_data.song_details.DetailsResponse
import com.example.musicapp.repository.APIResponse
import com.example.musicapp.repository.SongRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val repository: SongRepository): ViewModel() {
    private var vm_q:String=""
    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getSongDetails(vm_q)
        }
    }

    val songs:LiveData<APIResponse<DetailsResponse>>
        get() = repository.songs

    val lyrics:LiveData<APIResponse<LyricsResponse>>
        get() = repository.lyrics

    fun setQuerry(q:String){
        vm_q=q
    }
    fun getSongDetails(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.getSongDetails(vm_q)
        }
    }

    fun getSongLyrics(id:Int?){
        viewModelScope.launch(Dispatchers.IO){
            repository.getSongLyrics(id)
        }
    }
}