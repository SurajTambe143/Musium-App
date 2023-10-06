package com.example.musicapp.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.musicapp.api_call.SongService
import com.example.musicapp.model_data.lyrics.LyricsResponse
import com.example.musicapp.model_data.song_details.DetailsResponse
import com.example.musicapp.utility.NetworkCheck

class SongRepository(private val songService: SongService, val context: Context) {

    private val songLiveData = MutableLiveData<APIResponse<DetailsResponse>>()

    val songs: LiveData<APIResponse<DetailsResponse>>
        get() = songLiveData

    private val lyricLiveData =MutableLiveData<APIResponse<LyricsResponse>>()

    val lyrics:LiveData<APIResponse<LyricsResponse>>
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