package com.example.musicapp.model_data.shazam_song_details

data class Option(
    val actions: List<ActionX>? = listOf(),
    val beacondata: Beacondata? = Beacondata(),
    val caption: String? = "",
    val colouroverflowimage: Boolean? = false,
    val image: String? = "",
    val listcaption: String? = "",
    val overflowimage: String? = "",
    val providername: String? = "",
    val type: String? = ""
)