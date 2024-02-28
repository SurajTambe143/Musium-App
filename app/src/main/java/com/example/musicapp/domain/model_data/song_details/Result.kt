package com.example.musicapp.domain.model_data.song_details

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Result(
    val _type: String?="",
    val annotation_count: Int?=0,
    val api_path: String?="",
    val artist_names: String?="",
//    val featured_artists: List<FeaturedArtist>,
    val full_title: String?="",
    val header_image_thumbnail_url: String?="",
    val header_image_url: String?="",
    val id: Int?=0,
//    val instrumental: Boolean,
    val lyrics_owner_id: Int?=0,
    val lyrics_state: String?="",
    val lyrics_updated_at: Int?=0,
    val path: String?="",
    val primary_artist: com.example.musicapp.domain.model_data.song_details.PrimaryArtist,
    val pyongs_count: Int?=0,
    val relationships_index_url: String?="",
//    val release_date_components: ReleaseDateComponents,
    val release_date_for_display: String?="",
    val release_date_with_abbreviated_month_for_display: String?="",
    val song_art_image_thumbnail_url: String?="",
    val song_art_image_url: String?="",
//    val stats: Stats,
    val title: String?="",
    val title_with_featured: String,
    val updated_by_human_at: Int?=0,
    val url: String?=""
): Parcelable