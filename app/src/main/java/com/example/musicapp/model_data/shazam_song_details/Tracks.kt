package com.example.musicapp.model_data.shazam_song_details

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Tracks(
    val hits: List<HitX>? = listOf()
):Parcelable