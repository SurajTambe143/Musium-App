package com.example.musicapp.api_call

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {

    const val BASE_URL = "https://genius-song-lyrics1.p.rapidapi.com/"

    const val BASE_URL_SHAZAM ="https://shazam.p.rapidapi.com"

    fun getInstance():Retrofit{

        var logging= HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC)
        logging.redactHeader("Authorization")
        logging.redactHeader("Cookie")
        var httpClient= OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
    }


    fun getInstanceOfShazamApi():Retrofit{

        var logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC)
        logging.redactHeader("Authorization")
        logging.redactHeader("Cookie")

        var httpClient= OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL_SHAZAM)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()

    }
}