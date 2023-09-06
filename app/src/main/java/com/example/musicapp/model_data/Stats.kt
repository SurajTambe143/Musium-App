package com.example.musicapp.model_data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Stats(
    val concurrents: Int,
    val hot: Boolean,
    val pageviews: Int,
    val unreviewed_annotations: Int
):Parcelable