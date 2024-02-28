package com.example.musicapp.presentation.home

import android.os.Bundle
import android.text.Html
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.musicapp.R
import com.example.musicapp.remote.api_call.MusicApi
import com.example.musicapp.domain.model_data.lyrics.LyricsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LyricsActivity : AppCompatActivity() {

    var result: String?=""
    lateinit var tv: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lyrics)

        tv=findViewById(R.id.tv_lyrics)
        val value=intent.extras?.getInt("id data")
        getLyrics(value)
    }
    private fun getLyrics(id: Int?){
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val lyrics = MusicApi.getMusicService().getSongLyrics(id,"plain")
                lyrics.enqueue(object : Callback<com.example.musicapp.domain.model_data.lyrics.LyricsResponse> {
                    override fun onResponse(
                        call: Call<com.example.musicapp.domain.model_data.lyrics.LyricsResponse>,
                        response: Response<com.example.musicapp.domain.model_data.lyrics.LyricsResponse>
                    ) {
                        val lyric=response.body()
                        lyric?.let {
                            result= it.lyrics?.lyrics?.body?.html
                            tv.setText(Html.fromHtml(result).toString())
                        }
                    }
                    override fun onFailure(call: Call<com.example.musicapp.domain.model_data.lyrics.LyricsResponse>, t: Throwable) {
                        Toast.makeText(this@LyricsActivity,"No Lyrics Found", Toast.LENGTH_LONG)
                        Log.d("Api Call","Error in API call")
                    }

                })
            } catch (e: Exception) {
                Log.d("Api Call","Error in API")
            }
        }
    }
}