package com.example.musicapp.home.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.musicapp.adapter.ViewPagerAdapter
import com.example.musicapp.databinding.ActivityDetailsBinding
import com.example.musicapp.model_data.song_details.Hit
import com.example.musicapp.service.SongPlayingServices
import com.google.android.material.tabs.TabLayoutMediator

class DetailsActivity : AppCompatActivity() {
    private var song: Hit? = null
    private var _binding: ActivityDetailsBinding?=null
    private val binding get() = _binding!!
    val TAG:String="Detail Activity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.e(TAG, "onCreate: is called of D Activity", )
        song = intent.getParcelableExtra("songDetail")
        viewPagerAdapter()

    }


    private fun viewPagerAdapter(){
        Log.e(TAG, "viewPagerAdapter: is called in D Activity", )
        val adapter = ViewPagerAdapter(
            supportFragmentManager,
            lifecycle,
            song
        )
        binding.viewPager.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Music"
                1 -> tab.text = "Lyrics"
            }
        }.attach()
        binding.detailsShimmerLayout.visibility=View.GONE
    }

    override fun onResume() {
        super.onResume()
        Log.e(TAG, "onResume: is called  of D Activity")
    }

    override fun onPause() {
        super.onPause()
        Log.e(TAG, "onPause: is called  of D Activity")
    }

    override fun onStart() {
        super.onStart()
        Log.e(TAG, "onStart: is called  of D Activity")
    }

    override fun onStop() {
        super.onStop()
        Log.e(TAG, "onStop: is called of D Activity", )
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG, "onDestroy: is called  of D Activity")
        _binding=null
        val intent = Intent(this, SongPlayingServices::class.java)
//        stopService(intent)
    }
}