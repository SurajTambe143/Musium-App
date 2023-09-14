package com.example.musicapp.model_data.shazam_song_details

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HitX(
    val track: Track? = Track()
):Parcelable