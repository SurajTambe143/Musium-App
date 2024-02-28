package com.example.musicapp.domain.model_data.song_details

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ReleaseDateComponents(
    val day: Int,
    val month: Int,
    val year: Int
):Parcelable