package com.example.musicapp.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.musicapp.R
import com.example.musicapp.model_data.LyricsResponse

class LyricsFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lyrics, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val lyric:LyricsResponse?=arguments?.getParcelable("lyricsBody")

        val textView=view.findViewById<TextView>(R.id.lyrics)
        val reply=lyric?.lyrics?.lyrics?.body?.plain
        textView.text=reply
    }

}