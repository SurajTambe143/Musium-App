package com.example.musicapp.domain.model_data.lyrics

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LyricsX(
    val body: com.example.musicapp.domain.model_data.lyrics.Body
):Parcelable