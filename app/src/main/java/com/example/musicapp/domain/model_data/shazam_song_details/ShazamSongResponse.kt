package com.example.musicapp.domain.model_data.shazam_song_details

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShazamSongResponse(
//    val artists: Artists? = Artists(),
    val tracks: com.example.musicapp.domain.model_data.shazam_song_details.Tracks? = com.example.musicapp.domain.model_data.shazam_song_details.Tracks()
) : Parcelable