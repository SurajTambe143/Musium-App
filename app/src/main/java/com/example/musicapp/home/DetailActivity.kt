package com.example.musicapp.home

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.musicapp.R
import com.example.musicapp.model_data.Hit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val song:Hit?=intent.getParcelableExtra("songDetail")

        val imageTitle=findViewById<ImageView>(R.id.detail_title_image)
        val imageTitleBg=findViewById<ImageView>(R.id.detail_title_image_bg)
        val title =findViewById<TextView>(R.id.detail_title)
        val artistImage=findViewById<ImageView>(R.id.detail_artist_image)
        val artistName=findViewById<TextView>(R.id.detail_artist)
        val releaseDate=findViewById<TextView>(R.id.release_date)

        Glide.with(this).load(song?.result?.header_image_url).into(imageTitle)
        Glide.with(this).load(song?.result?.song_art_image_url).into(imageTitleBg)
        title.text=song?.result?.title
        Glide.with(this).load(song?.result?.primary_artist?.image_url).into(artistImage)
        artistName.text=song?.result?.artist_names
        releaseDate.text=song?.result?.release_date_for_display

        findViewById<ImageButton>(R.id.back_image_btn).setOnClickListener {
            val intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.detail_lyrics_btn).setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val id=song?.result?.id
                val intent=Intent(this@DetailActivity,LyricsActivity::class.java)
                intent.putExtra("id data",id)
                startActivity(intent)
            }
        }
    }

}