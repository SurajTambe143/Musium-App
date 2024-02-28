package com.example.musicapp.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.musicapp.remote.api_call.SongService
import com.example.musicapp.domain.model_data.lyrics.LyricsResponse
import com.example.musicapp.domain.model_data.song_details.DetailsResponse
import com.example.musicapp.utility.NetworkCheck

class SongRepository(private val songService: SongService, val context: Context) {

    private val songLiveData = MutableLiveData<APIResponse<com.example.musicapp.domain.model_data.song_details.DetailsResponse>>()

    val songs: LiveData<APIResponse<com.example.musicapp.domain.model_data.song_details.DetailsResponse>>
        get() = songLiveData

    private val lyricLiveData =MutableLiveData<APIResponse<com.example.musicapp.domain.model_data.lyrics.LyricsResponse>>()

    val lyrics:LiveData<APIResponse<com.example.musicapp.domain.model_data.lyrics.LyricsResponse>>
        get() = lyricLiveData

    suspend fun getSongDetails(q: String) {
        if (NetworkCheck.isOnline(context)){
            val result = songService.getSongDetails(q)
            if (result.body() != null) {
                songLiveData.postValue(APIResponse.Success(result.body()))
            }else{
                songLiveData.postValue(APIResponse.Error("Error occurred while fetching song"))
            }
        }

    }
    suspend fun getSongLyrics(id:Int?){
        if (NetworkCheck.isOnline(context)){
            val result = songService.getSongLyrics(id,"plain")
            if (result.body() != null) {
                lyricLiveData.postValue(APIResponse.Success(result.body()))
            }else{
                lyricLiveData.postValue(APIResponse.Error("Error occurred while fetching lyrics"))
            }

        }
    }

}