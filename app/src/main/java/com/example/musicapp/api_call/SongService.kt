package com.example.musicapp.api_call

import com.example.musicapp.model_data.lyrics.LyricsResponse
import com.example.musicapp.model_data.song_details.DetailsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface SongService {

    @Headers(
        "X-RapidAPI-Key: bcb30a9572mshc2b47de90799a6bp19761djsn2b7a8caffbdd",
        "X-RapidAPI-Host: genius-song-lyrics1.p.rapidapi.com"
    )
    @GET("search/")
    suspend fun getSongDetails(@Query("q") q: String): Response<DetailsResponse>

    @Headers(
        "X-RapidAPI-Key: bcb30a9572mshc2b47de90799a6bp19761djsn2b7a8caffbdd",
        "X-RapidAPI-Host: genius-song-lyrics1.p.rapidapi.com"
    )
    @GET("song/lyrics/")
    suspend fun getSongLyrics(@Query("id") id:Int?, @Query("text_format") text_format:String?): Response<LyricsResponse>
}
//dec3972ccfmsh0ab9c5962377bb0p115879jsn8b9953306098