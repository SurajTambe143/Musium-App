package com.example.musicapp.model_data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LyricsResponse(
    val lyrics: Lyrics
):Parcelable