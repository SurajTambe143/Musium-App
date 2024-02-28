package com.example.musicapp.domain.model_data.shazam_song_details

data class Provider(
    val actions: List<com.example.musicapp.domain.model_data.shazam_song_details.ActionX>? = listOf(),
    val caption: String? = "",
    val images: com.example.musicapp.domain.model_data.shazam_song_details.Images? = com.example.musicapp.domain.model_data.shazam_song_details.Images(),
    val type: String? = ""
)