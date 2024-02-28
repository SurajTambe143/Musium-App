package com.example.musicapp.remote.api_call

import com.example.musicapp.domain.model_data.lyrics.LyricsResponse
import com.example.musicapp.domain.model_data.song_details.DetailsResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query



const val BASE_URL = "https://genius-song-lyrics1.p.rapidapi.com/"

interface MusicInterface {

    @Headers(
        "X-RapidAPI-Key: bcb30a9572mshc2b47de90799a6bp19761djsn2b7a8caffbdd",
        "X-RapidAPI-Host: genius-song-lyrics1.p.rapidapi.com"
    )
    @GET("search/")
    fun getSongDetails(@Query("q") q: String): Call<com.example.musicapp.domain.model_data.song_details.DetailsResponse>

    @Headers(
        "X-RapidAPI-Key: bcb30a9572mshc2b47de90799a6bp19761djsn2b7a8caffbdd",
        "X-RapidAPI-Host: genius-song-lyrics1.p.rapidapi.com"
    )
    @GET("song/lyrics/")
     fun getSongLyrics(@Query("id") id:Int?,@Query("text_format") text_format:String?): Call<com.example.musicapp.domain.model_data.lyrics.LyricsResponse>
}

object MusicApi {
    private val retrofit: Retrofit

    init {
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getMusicService(): MusicInterface {
        return retrofit.create(MusicInterface::class.java)
    }

}