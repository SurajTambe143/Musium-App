package com.example.musicapp.domain.model_data.shazam_song_details

data class Option(
    val actions: List<com.example.musicapp.domain.model_data.shazam_song_details.ActionX>? = listOf(),
    val beacondata: com.example.musicapp.domain.model_data.shazam_song_details.Beacondata? = com.example.musicapp.domain.model_data.shazam_song_details.Beacondata(),
    val caption: String? = "",
    val colouroverflowimage: Boolean? = false,
    val image: String? = "",
    val listcaption: String? = "",
    val overflowimage: String? = "",
    val providername: String? = "",
    val type: String? = ""
)