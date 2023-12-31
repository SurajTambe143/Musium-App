package com.example.musicapp.model_data.shazam_song_details

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Hub(
    val actions: List<Action>? = listOf(),
    val displayname: String? = "",
    val explicit: Boolean? = false,
    val image: String? = "",
//    val options: List<Option>? = listOf(),
//    val providers: List<Provider>? = listOf(),
    val type: String? = ""
):Parcelable