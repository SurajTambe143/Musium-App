package com.example.musicapp.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.musicapp.R
import com.example.musicapp.api_call.RetrofitHelper
import com.example.musicapp.api_call.SongService
import com.example.musicapp.model_data.song_details.Hit
import com.example.musicapp.repository.APIResponse
import com.example.musicapp.repository.SongRepository
import com.example.musicapp.viewmodel.MainViewModel
import com.example.musicapp.viewmodel.MainViewModelFactory

class LyricsFragment : Fragment() {
    lateinit var mainViewModel: MainViewModel
    lateinit var progressBar: ProgressBar
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_lyrics, container, false)

        progressBar=view.findViewById(R.id.progressBar)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val lyric: Hit?=arguments?.getParcelable("songDetail")

        val song_id:Int?=lyric?.result?.id

        val songService = RetrofitHelper.getInstance().create(SongService::class.java)
        val repository = SongRepository(songService, requireContext())
        mainViewModel =
            ViewModelProvider(requireActivity(), MainViewModelFactory(repository)).get(MainViewModel::class.java)
        mainViewModel.getSongLyrics(song_id)

        mainViewModel.lyrics.observe(requireActivity(), Observer {
            when(it){
                is APIResponse.Loading -> {
                    progressBar.visibility=View.VISIBLE
                }
                is APIResponse.Success -> {
                    it.data.let {
                        val textView=view.findViewById<TextView>(R.id.lyrics)
                        progressBar.visibility=View.GONE
                        textView.text=it?.lyrics?.lyrics?.body?.plain
                    }
                }
                is APIResponse.Error -> {}
            }
        })

    }

}