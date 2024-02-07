package com.example.musicapp.home.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.musicapp.adapter.ViewPagerAdapter
import com.example.musicapp.databinding.ActivityDetailsBinding
import com.example.musicapp.model_data.song_details.Hit
import com.example.musicapp.utility.logEvent
import com.google.android.material.tabs.TabLayoutMediator

class DetailsActivity : AppCompatActivity() {
    private var song: Hit? = null
    private var _binding: ActivityDetailsBinding?=null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        song = intent.getParcelableExtra("songDetail")
        viewPagerAdapter()

        val bundle = Bundle()
        bundle.putString("UserName", "Suraj Tambe")
        logEvent(this, "DetailsActivity", bundle);

    }


    private fun viewPagerAdapter(){
        val adapter = ViewPagerAdapter(
            supportFragmentManager,
            lifecycle,
            song
        )
        binding.viewPager.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Details"
                1 -> tab.text = "Lyrics"
            }
        }.attach()
        binding.detailsShimmerLayout.visibility=View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
}