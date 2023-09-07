package com.example.musicapp.model_data.song_details

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Hit(
    val index: String,
    val result: Result,
    val type: String
):Parcelable