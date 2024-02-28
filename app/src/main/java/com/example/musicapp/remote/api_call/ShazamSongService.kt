package com.example.musicapp.remote.api_call

import com.example.musicapp.BuildConfig
import com.example.musicapp.domain.model_data.shazam_song_details.ShazamSongResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ShazamSongService {

    @Headers(
        "X-RapidAPI-Key: ${BuildConfig.API_KEY}",
        "X-RapidAPI-Host: shazam.p.rapidapi.com"
    )
    @GET("/search")
    suspend fun getShazamSongDetails(@Query("term") q: String): Response<com.example.musicapp.domain.model_data.shazam_song_details.ShazamSongResponse>
}