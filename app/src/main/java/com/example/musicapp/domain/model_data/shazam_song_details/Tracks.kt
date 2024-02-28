package com.example.musicapp.domain.model_data.shazam_song_details

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Tracks(
    val hits: List<com.example.musicapp.domain.model_data.shazam_song_details.HitX>? = listOf()
):Parcelable