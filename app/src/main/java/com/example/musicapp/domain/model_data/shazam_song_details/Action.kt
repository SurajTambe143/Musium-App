package com.example.musicapp.domain.model_data.shazam_song_details

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Action(
    val id: String? = null,
    val name: String? = null,
    val type: String? = null,
    val uri: String? = ""
):Parcelable