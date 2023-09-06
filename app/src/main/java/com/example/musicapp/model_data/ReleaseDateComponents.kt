package com.example.musicapp.model_data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ReleaseDateComponents(
    val day: Int,
    val month: Int,
    val year: Int
):Parcelable