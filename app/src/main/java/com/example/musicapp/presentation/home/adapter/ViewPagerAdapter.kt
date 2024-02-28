package com.example.musicapp.presentation.home.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.musicapp.presentation.home.fragment.DetailFragment
import com.example.musicapp.presentation.home.fragment.LyricsFragment
import com.example.musicapp.domain.model_data.song_details.Hit

class ViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: androidx.lifecycle.Lifecycle,
    private val hit: com.example.musicapp.domain.model_data.song_details.Hit?
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