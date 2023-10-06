package com.example.musicapp.home.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.musicapp.api_call.RetrofitHelper
import com.example.musicapp.api_call.SongService
import com.example.musicapp.databinding.FragmentLyricsBinding
import com.example.musicapp.model_data.song_details.Hit
import com.example.musicapp.repository.APIResponse
import com.example.musicapp.repository.SongRepository
import com.example.musicapp.viewmodel.MainViewModel
import com.example.musicapp.viewmodel.MainViewModelFactory
import com.google.android.material.snackbar.Snackbar

class LyricsFragment : Fragment() {
    lateinit var mainViewModel: MainViewModel
    private var _binding: FragmentLyricsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLyricsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val lyric: Hit? = arguments?.getParcelable("songDetail")

        val songId: Int? = lyric?.result?.id

        setUpViewModelObserver(songId)

    }

    private fun setUpViewModelObserver(songId: Int?) {
        val songService = RetrofitHelper.getInstance().create(SongService::class.java)
        val repository = SongRepository(songService, requireContext())
        mainViewModel =
            ViewModelProvider(
                requireActivity(),
                MainViewModelFactory(repository)
            ).get(MainViewModel::class.java)

        mainViewModel.getSongLyrics(songId)
        Log.e("songID", songId.toString())

        mainViewModel.lyrics.observe(requireActivity(), Observer {
            when (it) {
                is APIResponse.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is APIResponse.Success -> {
                    it.data.let {
                        binding.progressBar.visibility = View.GONE
                        Log.e("Check lyrics",it?.lyrics?.lyrics?.body?.plain.toString() )
                        binding.lyrics.text= it?.lyrics?.lyrics?.body?.plain.toString()
                    }
                }

                is APIResponse.Error -> {
                    it.errorMessage.let {
                        Snackbar.make(binding.root,it.toString(),Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}