package com.example.musicapp.model_data.shazam_song_details

data class Provider(
    val actions: List<ActionX>? = listOf(),
    val caption: String? = "",
    val images: Images? = Images(),
    val type: String? = ""
)