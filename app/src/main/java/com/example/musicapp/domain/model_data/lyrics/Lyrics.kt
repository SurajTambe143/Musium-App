package com.example.musicapp.domain.model_data.lyrics

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Lyrics(
    val _type: String,
    val api_path: String,
    val lyrics: com.example.musicapp.domain.model_data.lyrics.LyricsX,
//    val path: String,
//    val song_id: Int,
//    val tracking_data: TrackingData
):Parcelable