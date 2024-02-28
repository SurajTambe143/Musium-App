package com.example.musicapp.domain.model_data.shazam_song_details

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Track(
//    val artists: List<ArtistX>? = listOf(),
    val hub: com.example.musicapp.domain.model_data.shazam_song_details.Hub? = com.example.musicapp.domain.model_data.shazam_song_details.Hub(),
//    val images: ImagesX? = ImagesX(),
    val key: String? = "",
    val layout: String? = "",
//    val share: Share? = Share(),
    val subtitle: String? = "",
    val title: String? = "",
    val type: String? = "",
    val url: String? = ""
):Parcelable