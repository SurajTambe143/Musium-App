package com.example.musicapp.home.fragment

import android.content.ComponentName
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
import com.example.musicapp.R
import com.example.musicapp.api_call.RetrofitHelper
import com.example.musicapp.api_call.ShazamSongService
import com.example.musicapp.databinding.FragmentDetailBinding
import com.example.musicapp.model_data.shazam_song_details.ShazamSongResponse
import com.example.musicapp.model_data.song_details.Hit
import com.example.musicapp.repository.APIResponse
import com.example.musicapp.repository.ShazamSongRepository
import com.example.musicapp.service.SongPlayingServices
import com.example.musicapp.service.SongPlayingServices.Companion.mediaPlayer
import com.example.musicapp.utility.SongDurationFormator
import com.example.musicapp.viewmodel.ShazamViewModel
import com.example.musicapp.viewmodel.ShazamViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DetailFragment : Fragment(), ServiceConnection {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    var playUri: String? = ""
    var songPlayingServices: SongPlayingServices? = null
    var songList: Hit? = null
    var shazamSongList: ShazamSongResponse? = null
    lateinit var shazamViewModel: ShazamViewModel
    var mp: MediaPlayer? = null
    private var counterIcon = 0
    val TAG: String = "Detail Fragment"

    private val uiThread = CoroutineScope(Job() + Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e(TAG, "onViewCreated: is called  of DF")
        songList = arguments?.getParcelable("songDetail")
        Log.e(TAG, songList.toString() )
        songPlayingServices?.setMusicList(songList)
        setUI(songList)
        shazamViewModelInit()
    }

    override fun onStart() {
        super.onStart()
        Log.e(TAG, "onStart: is called  of DF")
        var intent = Intent(requireActivity(), SongPlayingServices::class.java)
        //requireActivity().bindService(intent, this@DetailFragment, Context.BIND_AUTO_CREATE)
        requireActivity().startService(intent)
        initMediaPlayCheck()
        uiThread.launch {
            initSeekBar()
        }
        seekBarChange()
    }

    override fun onStop() {
        super.onStop()
        Log.e(TAG, "onStop: is called  of DF")
        //requireActivity().unbindService(this@DetailFragment)
        uiThread.cancel()

    }

    private fun setUI(songList: Hit?) {
        Glide.with(requireContext()).load(songList?.result?.header_image_url)
            .into(binding.detailsTitleImage)
        Glide.with(requireContext()).load(songList?.result?.song_art_image_url)
            .into(binding.detailsTitleImageBg)
        binding.detailsTitle.text = songList?.result?.title
        binding.detailsArtist.text = songList?.result?.artist_names
        binding.detailsReleaseDate.text = songList?.result?.release_date_for_display
        binding.seekBar.progress=0
    }

    private fun controlSound(song: String?) {
        Log.e(TAG, "controlSound: Media Player is called on DF")
        try {
            val uri: Uri? = Uri.parse(song.toString())
            if (songPlayingServices!!.mp == null) {
                Log.e(TAG, "controlSound: MP is initialized")
                songPlayingServices!!.mp = MediaPlayer()
                songPlayingServices!!.mp = MediaPlayer.create(requireActivity(), uri)
                binding.playProgressBar.visibility = View.GONE
                binding.seekBar.progress = 0
                binding.seekBar.max = songPlayingServices!!.mp!!.duration
                binding.seekBarDurationStart.text =
                    SongDurationFormator.formatDuration(songPlayingServices!!.mp!!.currentPosition.toLong())
                binding.seekBarDurationEnd.text =
                    SongDurationFormator.formatDuration(songPlayingServices!!.mp!!.duration.toLong())

                binding.play.setOnClickListener {
                    Log.e(TAG, "controlSound: MP is started")
                    songPlayingServices!!.mp?.start()
                    songPlayingServices!!.showNotification()
                }
                binding.stop.setOnClickListener {
                    if (songPlayingServices?.mp != null) {
                        Log.e(TAG, "controlSound: MP is stop")
                        songPlayingServices!!.mp?.stop()
                        songPlayingServices!!.mp?.release()
                        NotificationManagerCompat.from(requireContext()).cancel(122)
                        binding.playSlant.visibility = View.VISIBLE
                        binding.pauseSlant.visibility = View.VISIBLE
                        binding.stopSlant.visibility = View.VISIBLE
                    }
                }
                binding.pause.setOnClickListener {
                    if (songPlayingServices?.mp != null) {
                        Log.e(TAG, "controlSound: MP is pause")
                        songPlayingServices!!.mp?.pause()
                    }
                }
                binding.seekBar.setOnSeekBarChangeListener(object :
                    SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                        if (p2)
                            if (songPlayingServices?.mp != null) songPlayingServices!!.mp?.seekTo(p1)
                    }

                    override fun onStartTrackingTouch(p0: SeekBar?) = Unit

                    override fun onStopTrackingTouch(p0: SeekBar?) = Unit
                })
            }

        } catch (e: Exception) {
            return
        }
    }

    override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
        val binder = p1 as SongPlayingServices.MyBinder
        Log.e(TAG, "onService Connected: is called  of DF")
        songPlayingServices = binder.currentService()
        songPlayingServices!!.notificationChannelInit()

    }

    override fun onServiceDisconnected(p0: ComponentName?) {
        Log.e(TAG, "onService Disconnected: is called  of DF")
        songPlayingServices = null
        uiThread.cancel()
    }

    fun shazamViewModelInit() {
        Log.e(TAG, "shazamViewModelInit: is called in DF")

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
                        shazamSongList = it
                        Log.e("Shazam Success", it.toString())
                        songPlayingServices?.setMusicList(songList)
                        playUri = it?.tracks?.hits?.get(0)?.track?.hub?.actions?.get(1)?.uri
                        Log.e(TAG, "shazamViewModelInit: On success API,Control sound is called")
//                        controlSound(playUri)
                        mediaPlayerInit(playUri)
                        binding.play.setImageResource(R.drawable.round_pause_24)
                        btnControl()
                        onCompleteMediaPlayer(playUri)
                    }
                }

                is APIResponse.Error -> {
                    it.errorMessage.let {
                        Snackbar.make(binding.root, it.toString(), Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        })

    }

    override fun onResume() {
        super.onResume()
        Log.e(TAG, "onResume: is called  of DF")
    }

    override fun onPause() {
        super.onPause()
        Log.e(TAG, "onPause: is called  of DF")
    }

    private fun play() {
        if (SongPlayingServices.mediaPlayer == null) {
            Log.e(TAG, "play: Media Player is created")
            SongPlayingServices.mediaPlayer = MediaPlayer.create(
                context,
                SongPlayingServices.uri
            )
        }
        Log.e(TAG, "play: Media Player is Started")
        SongPlayingServices.mediaPlayer?.start()
    }

    private fun pause() {
        if (SongPlayingServices.mediaPlayer == null) {
            Log.e(TAG, "pause: Media Player is created")
            SongPlayingServices.mediaPlayer =
                MediaPlayer.create(
                    context,
                    SongPlayingServices.uri
                )
        }
        if (SongPlayingServices.mediaPlayer!!.isPlaying) {
            Log.e(TAG, "pause: Media Player is paused")
            SongPlayingServices.mediaPlayer!!.pause()
        }
    }
    private fun initMediaPlayCheck(){
        if (mediaPlayer!=null){
            mediaPlayer!!.stop()
            binding.seekBar.progress=0
        }
    }

    private fun mediaPlayerInit(song: String?) {
        if (SongPlayingServices.mediaPlayer == null) {
            val uri: Uri? = Uri.parse(song.toString())
            mp = MediaPlayer.create(requireContext(), uri)
            SongPlayingServices.mediaPlayer = mp
            Log.e(TAG, "mediaPlayerInit: Media Player is Initialised")
        } else {
            SongPlayingServices.mediaPlayer!!.stop()
            SongPlayingServices.mediaPlayer!!.release()
            val uri: Uri? = Uri.parse(song.toString())
            SongPlayingServices.mediaPlayer = MediaPlayer.create(requireContext(), uri)
        }
        binding.playProgressBar.visibility = View.GONE
        SongPlayingServices.mediaPlayer!!.start()
        binding.seekBarDurationEnd.text =
            SongDurationFormator.formatDuration(SongPlayingServices.mediaPlayer!!.duration.toLong())
    }

    private fun btnControl() {
        binding.play.setOnClickListener {
            if (counterIcon % 2 == 0) {
                pause()
                binding.play.setImageResource(R.drawable.round_play)
                counterIcon++
            } else {
                play()
                binding.play.setImageResource(R.drawable.round_pause_24)
                counterIcon++
            }
//            Log.e(TAG, "btnControl: MP is started")
//            play()
        }
        binding.pause.setOnClickListener {
            Log.e(TAG, "btnControl: MP is paused")
            replyFive()
        }

        binding.stop.setOnClickListener {
            forwardFive()
        }

    }

    private fun seekBarChange(){
        binding.seekBar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                if (p2)
                    if (SongPlayingServices.mediaPlayer != null) SongPlayingServices.mediaPlayer?.seekTo(
                        p1.times(1000)
                    )
            }
            override fun onStartTrackingTouch(p0: SeekBar?) = Unit

            override fun onStopTrackingTouch(p0: SeekBar?) = Unit
        })
    }

    private suspend fun initSeekBar() {
        while (true) {
            delay(1000)
            binding.seekBar.max = mediaPlayer?.duration?.div(1000) ?: 0
            mediaPlayer?.currentPosition?.let {
                binding.seekBar.progress = it.div(1000)
                binding.seekBarDurationStart.text =
                    SongDurationFormator.formatDuration(it.toLong())
            }
        }
    }
    private fun forwardFive(){
            mediaPlayer?.seekTo((mediaPlayer!!.currentPosition+5000))
    }
     private fun replyFive(){
             mediaPlayer?.seekTo((mediaPlayer!!.currentPosition-5000))
     }

    private fun onCompleteMediaPlayer(playUri: String?){
        mediaPlayer?.setOnCompletionListener {
            mediaPlayerInit(playUri)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG, "onDestroy: is called  of DF")
        NotificationManagerCompat.from(requireContext()).cancel(122)
        _binding = null
        songPlayingServices = null
    }

}