package com.example.musicapp.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.musicapp.api_call.ShazamSongService
import com.example.musicapp.model_data.shazam_song_details.ShazamSongResponse
import com.example.musicapp.utility.NetworkCheck

class ShazamSongRepository(private val shazamSongService: ShazamSongService,val context: Context) {

    private val shazamSongLiveData = MutableLiveData<APIResponse<ShazamSongResponse>>()

    val shazamSongs: LiveData<APIResponse<ShazamSongResponse>>
        get() = shazamSongLiveData


    suspend fun getShazamSongDetails(q: String){
        if (NetworkCheck.isOnline(context)){
            val result = shazamSongService.getShazamSongDetails(q)
            if (result.body() != null) {
                shazamSongLiveData.postValue(APIResponse.Success(result.body()))
            }else{
                shazamSongLiveData.postValue(APIResponse.Error("Error occurred while fetching song"))
            }

        }
    }
}