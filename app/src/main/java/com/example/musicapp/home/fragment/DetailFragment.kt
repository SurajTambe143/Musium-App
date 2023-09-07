package com.example.musicapp.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.musicapp.R
import com.example.musicapp.model_data.song_details.Hit

class DetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val songList: Hit? = arguments?.getParcelable("songDetail")
        val imageTitle = view.findViewById<ImageView>(R.id.details_title_image)
        val imageTitleBg = view.findViewById<ImageView>(R.id.details_title_image_bg)
        val title = view.findViewById<TextView>(R.id.details_title)
        val artistImage = view.findViewById<ImageView>(R.id.details_artist_image)
        val artistName = view.findViewById<TextView>(R.id.details_artist)
        val releaseDate = view.findViewById<TextView>(R.id.details_release_date)

        Glide.with(requireContext()).load(songList?.result?.header_image_url).into(imageTitle)
        Glide.with(requireContext()).load(songList?.result?.song_art_image_url).into(imageTitleBg)
        title.text = songList?.result?.title
        Glide.with(requireContext()).load(songList?.result?.primary_artist?.image_url).into(artistImage)
        artistName.text = songList?.result?.artist_names
        releaseDate.text = songList?.result?.release_date_for_display
    }

}