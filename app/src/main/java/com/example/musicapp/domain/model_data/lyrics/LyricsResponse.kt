package com.example.musicapp.domain.model_data.lyrics

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LyricsResponse(
    val lyrics: com.example.musicapp.domain.model_data.lyrics.Lyrics
):Parcelable