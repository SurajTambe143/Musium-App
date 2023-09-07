package com.example.musicapp.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.musicapp.home.fragment.DetailFragment
import com.example.musicapp.home.fragment.LyricsFragment
import com.example.musicapp.model_data.song_details.Hit

class ViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: androidx.lifecycle.Lifecycle,
    private val hit: Hit?
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                DetailFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable("songDetail", hit)
                    }
                }
            }

            1 -> {
                LyricsFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable("songDetail", hit)
                    }
                }
            }

            else -> {
                DetailFragment()
            }
        }
    }
}