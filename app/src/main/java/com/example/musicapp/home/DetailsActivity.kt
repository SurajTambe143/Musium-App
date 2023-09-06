package com.example.musicapp.home

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.musicapp.R
import com.example.musicapp.adapter.ViewPagerAdapter
import com.example.musicapp.api_call.MusicApi
import com.example.musicapp.model_data.Hit
import com.example.musicapp.model_data.LyricsResponse
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsActivity : AppCompatActivity() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private var song: Hit? = null
    lateinit var progressBar: ProgressBar
    lateinit var detailsShimmer: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

//        progressBar=findViewById(R.id.progressbar_details)
        detailsShimmer=findViewById(R.id.details_shimmer_layout)

        song = intent.getParcelableExtra("songDetail")
        val idForSongs = song?.result?.id
        tabLayout = findViewById(R.id.tab_layout)
        viewPager = findViewById(R.id.view_pager)
        updateUI(idForSongs)

    }

    private fun updateUI(id: Int?) {
        getLyrics(id) { lyricsResponse ->
            if (lyricsResponse != null) {
                val adapter = ViewPagerAdapter(supportFragmentManager, lifecycle, song, lyricsResponse)
                viewPager.adapter = adapter
                TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                    when (position) {
                        0 -> tab.text = "Details"
                        1 -> tab.text = "Lyrics"
                    }
                }.attach()
//                progressBar.visibility= View.GONE
                detailsShimmer.visibility=View.GONE
            } else {
                Log.d("Error ", "updateUI: Failure in Api call")
            }
        }


    }

    private fun getLyrics(id: Int?, callback: (LyricsResponse?) -> Unit) {
        val lyrics = MusicApi.getMusicService().getSongLyrics(id,"plain")
        lyrics.enqueue(object : Callback<LyricsResponse> {
            override fun onResponse(
                call: Call<LyricsResponse>,
                response: Response<LyricsResponse>
            ) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    Log.d("Api Call", "Error in API call: ${response.code()}")
                    callback(null)
                }
            }
            override fun onFailure(call: Call<LyricsResponse>, t: Throwable) {
                Log.d("Api Call", "Error in API call")
                callback(null)
            }
        })
    }
}