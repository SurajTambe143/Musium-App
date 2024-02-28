package com.example.musicapp.domain.model_data.song_details

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Hit(
    val index: String,
    val result: com.example.musicapp.domain.model_data.song_details.Result,
    val type: String
):Parcelable