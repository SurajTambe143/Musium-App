package com.example.musicapp.model_data.lyrics

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
@Parcelize
data class Body(
    val html: String?="",
    val plain: String?=""
):Parcelable