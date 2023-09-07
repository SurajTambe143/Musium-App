package com.example.musicapp.home.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.musicapp.R
import com.example.musicapp.adapter.ViewPagerAdapter
import com.example.musicapp.model_data.song_details.Hit
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailsActivity : AppCompatActivity() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private var song: Hit? = null
    lateinit var detailsShimmer: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        detailsShimmer = findViewById(R.id.details_shimmer_layout)

        song = intent.getParcelableExtra("songDetail")
        tabLayout = findViewById(R.id.tab_layout)
        viewPager = findViewById(R.id.view_pager)
        val adapter = ViewPagerAdapter(
            supportFragmentManager,
            lifecycle,
            song
        )
        viewPager.adapter = adapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Details"
                1 -> tab.text = "Lyrics"
            }
        }.attach()
        detailsShimmer.visibility=View.GONE
    }
}