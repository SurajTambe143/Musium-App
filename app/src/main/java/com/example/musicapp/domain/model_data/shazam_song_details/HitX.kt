package com.example.musicapp.domain.model_data.shazam_song_details

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HitX(
    val track: com.example.musicapp.domain.model_data.shazam_song_details.Track? = com.example.musicapp.domain.model_data.shazam_song_details.Track()
):Parcelable