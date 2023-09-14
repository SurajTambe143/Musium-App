package com.example.musicapp.api_call

import com.example.musicapp.model_data.shazam_song_details.ShazamSongResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ShazamSongService {

    @Headers(
        "X-RapidAPI-Key: bcb30a9572mshc2b47de90799a6bp19761djsn2b7a8caffbdd",
        "X-RapidAPI-Host: shazam.p.rapidapi.com"
    )
    @GET("/search")
    suspend fun getShazamSongDetails(@Query("term") q: String): Response<ShazamSongResponse>
}