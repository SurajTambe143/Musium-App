package com.example.musicapp.presentation.podcasts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.musicapp.R
import com.example.musicapp.databinding.FragmentLyricsBinding
import com.example.musicapp.databinding.FragmentPodcastsBinding


class PodcastsFragment : Fragment() {
    private var _binding: FragmentPodcastsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPodcastsBinding.inflate(inflater, container, false)
        return binding.root
    }

}