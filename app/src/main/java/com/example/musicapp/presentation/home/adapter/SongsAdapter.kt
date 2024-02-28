package com.example.musicapp.presentation.home.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.musicapp.R
import com.example.musicapp.domain.model_data.song_details.Hit

class SongsAdapter(val onClick: (com.example.musicapp.domain.model_data.song_details.Hit) -> Unit) :
    RecyclerView.Adapter<SongsAdapter.MyViewHolder>() {
    private var result: List<com.example.musicapp.domain.model_data.song_details.Hit> = emptyList<com.example.musicapp.domain.model_data.song_details.Hit>().toMutableList()

    class MyViewHolder(itemView: View) : ViewHolder(itemView) {
        var image = itemView.findViewById<ImageView>(R.id.song_header_image)
        var title = itemView.findViewById<TextView>(R.id.song_title)
        var artist = itemView.findViewById<TextView>(R.id.song_artist)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.rv_item_layout, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return result.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val song = result[position]
        holder.title.text = song.result.title
        holder.artist.text = song.result.artist_names
        Glide.with(holder.itemView.context).load(song.result.header_image_url).into(holder.image)
        holder.itemView.setOnClickListener {
            onClick.invoke(song)
            Log.d("testing-values", "On touch")
        }
    }

    fun updateList(list: List<com.example.musicapp.domain.model_data.song_details.Hit>) {
        result = list
        notifyDataSetChanged()
    }
}