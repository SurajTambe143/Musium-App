package com.example.musicapp.domain.model_data.song_details

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FeaturedArtist(
    val _type: String,
    val api_path: String,
    val header_image_url: String,
    val id: Int,
    val image_url: String,
    val index_character: String,
    val iq: Int,
    val is_meme_verified: Boolean,
    val is_verified: Boolean,
    val name: String,
    val slug: String,
    val url: String
):Parcelable