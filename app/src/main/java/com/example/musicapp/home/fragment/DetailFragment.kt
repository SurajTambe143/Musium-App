package com.example.musicapp.home.fragment

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.musicapp.api_call.RetrofitHelper
import com.example.musicapp.api_call.ShazamSongService
import com.example.musicapp.databinding.FragmentDetailBinding
import com.example.musicapp.model_data.shazam_song_details.ShazamSongResponse
import com.example.musicapp.model_data.song_details.Hit
import com.example.musicapp.repository.APIResponse
import com.example.musicapp.repository.ShazamSongRepository
import com.example.musicapp.service.SongPlayingServices
import com.example.musicapp.utility.SongDurationFormator
import com.example.musicapp.utility.logEvent
import com.example.musicapp.viewmodel.ShazamViewModel
import com.example.musicapp.viewmodel.ShazamViewModelFactory

class DetailFragment : Fragment(), ServiceConnection {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    var playUri: String? = ""
    var songPlayingServices: SongPlayingServices? = null
    var songList:Hit?=null
    var shazamSongList:ShazamSongResponse?=null
    lateinit var shazamViewModel: ShazamViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        songList= arguments?.getParcelable("songDetail")
        songPlayingServices?.setMusicList(songList)
        setUI(songList)
        shazamViewModelInit()

        val bundle = Bundle()
        bundle.putString("UserName", "Suraj Tambe")
        logEvent(requireContext(), "DetailsFragment", bundle);
    }

    override fun onStart() {
        super.onStart()
        var intent = Intent(requireActivity(), SongPlayingServices::class.java)
        requireActivity().bindService(intent, this@DetailFragment, Context.BIND_AUTO_CREATE)
        requireActivity().startService(intent)
    }

    override fun onStop() {
        super.onStop()
        requireActivity().unbindService(this@DetailFragment)
    }

    private fun setUI(songList: Hit?) {
        Glide.with(requireContext()).load(songList?.result?.header_image_url)
            .into(binding.detailsTitleImage)
        Glide.with(requireContext()).load(songList?.result?.song_art_image_url)
            .into(binding.detailsTitleImageBg)
        binding.detailsTitle.text = songList?.result?.title
//        Glide.with(requireContext()).load(songList?.result?.primary_artist?.image_url)
//            .into(binding.detailsArtistImage)
        binding.detailsArtist.text = songList?.result?.artist_names
        binding.detailsReleaseDate.text = songList?.result?.release_date_for_display
    }

    private fun controlSound(song: String?) {
        try {
            val uri: Uri?= Uri.parse(song.toString())
            if (songPlayingServices!!.mp==null)  songPlayingServices!!.mp = MediaPlayer()
            songPlayingServices!!.mp = MediaPlayer.create(requireActivity(),uri)
            binding.seekBar.progress=0
            binding.seekBar.max= songPlayingServices!!.mp!!.duration
            binding.seekBarDurationStart.text=SongDurationFormator.formatDuration(songPlayingServices!!.mp!!.currentPosition.toLong())
            binding.seekBarDurationEnd.text=SongDurationFormator.formatDuration(songPlayingServices!!.mp!!.duration.toLong())

            binding.play.setOnClickListener {
//            var intent = Intent(requireActivity(), SongPlayingServices::class.java)
//            intent.putExtra("Shazam", song)
//            requireActivity().bindService(intent, this@DetailFragment,Context.BIND_AUTO_CREATE)
//            requireActivity().startService(intent)
                songPlayingServices!!.mp?.start()
                songPlayingServices!!.showNotification()
            }
            binding.stop.setOnClickListener {
//            var intent = Intent(requireActivity(), SongPlayingServices::class.java)
//            requireActivity().stopService(intent)
                songPlayingServices!!.mp?.stop()
                songPlayingServices!!.mp?.release()
                NotificationManagerCompat.from(requireContext()).cancel(122)
                binding.playSlant.visibility=View.VISIBLE
                binding.pauseSlant.visibility=View.VISIBLE
                binding.stopSlant.visibility=View.VISIBLE
            }
            binding.pause.setOnClickListener {
                songPlayingServices!!.mp?.pause()
            }
            binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    if (p2) songPlayingServices!!.mp?.seekTo(p1)
                }
                override fun onStartTrackingTouch(p0: SeekBar?) =Unit

                override fun onStopTrackingTouch(p0: SeekBar?) = Unit
            })

        }catch (e:Exception){
            return
        }
        fun myey(){

        }
    }

    override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
        val binder = p1 as SongPlayingServices.MyBinder
        songPlayingServices = binder.currentService()
        songPlayingServices!!.notificationChannelInit()
    }

    override fun onServiceDisconnected(p0: ComponentName?) {
        songPlayingServices = null
    }

    fun shazamViewModelInit(){

        val shazamSongService =
            RetrofitHelper.getInstanceOfShazamApi().create(ShazamSongService::class.java)
        val repository = ShazamSongRepository(shazamSongService, requireContext())
        shazamViewModel = ViewModelProvider(this, ShazamViewModelFactory(repository)).get(
            ShazamViewModel::class.java
        )
        shazamViewModel.getShazamSongDetails(songList?.result?.title.toString())
        shazamViewModel.shazamSongs.observe(requireActivity(), Observer {
            when (it) {
                is APIResponse.Loading -> {}
                is APIResponse.Success -> {
                    it.data.let {
                        shazamSongList=it
                        Log.e("Shazam Success", it.toString())
                        songPlayingServices?.setMusicList(songList)
                        playUri = it?.tracks?.hits?.get(0)?.track?.hub?.actions?.get(1)?.uri
                        controlSound(playUri)
                    }
                }
                is APIResponse.Error -> {}
            }
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}