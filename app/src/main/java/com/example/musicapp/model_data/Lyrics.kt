package com.example.musicapp.model_data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Lyrics(
    val _type: String,
    val api_path: String,
    val lyrics: LyricsX,
//    val path: String,
//    val song_id: Int,
//    val tracking_data: TrackingData
):Parcelable