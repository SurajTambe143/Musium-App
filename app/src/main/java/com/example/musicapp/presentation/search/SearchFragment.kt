package com.example.musicapp.presentation.search

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicapp.R
import com.example.musicapp.data.repository.APIResponse
import com.example.musicapp.data.repository.SongRepository
import com.example.musicapp.databinding.ActivityMainBinding
import com.example.musicapp.databinding.FragmentLyricsBinding
import com.example.musicapp.databinding.FragmentSearchBinding
import com.example.musicapp.presentation.home.activity.DetailsActivity
import com.example.musicapp.presentation.home.adapter.SongsAdapter
import com.example.musicapp.presentation.home.viewmodel.MainViewModel
import com.example.musicapp.presentation.home.viewmodel.MainViewModelFactory
import com.example.musicapp.remote.api_call.RetrofitHelper
import com.example.musicapp.remote.api_call.SongService
import com.example.musicapp.utility.NetworkMonitor
import com.google.android.material.snackbar.Snackbar


class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    lateinit var rootView: View
    lateinit var mainViewModel: MainViewModel
    private lateinit var networkMonitor: NetworkMonitor
    private val TAG="Main Activity"
    private val songsAdapter = SongsAdapter {
        networkMonitor.resetStatus()
        val intent = Intent(requireActivity(), DetailsActivity::class.java)
        intent.putExtra("songDetail", it)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //Tool Bar


        setUpRecyclerViews()
        networkMonitor = NetworkMonitor(requireContext(),{
            Snackbar.make(binding.clMainActivityRoot,"Network available", Snackbar.LENGTH_SHORT).show()
            mainViewModel.getSongDetails("Perfect")
        },{
            Snackbar.make(binding.clMainActivityRoot,"You are Offline", Snackbar.LENGTH_SHORT).show()
            binding.shimmerViewContainer.visibility= View.GONE
        })
        networkMonitor.resetStatus()

        setUpViewModelObservers()
    }

    private fun setUpRecyclerViews() {
        rootView = binding.clMainActivityRoot
        binding.rvContainer.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvContainer.adapter = songsAdapter
    }

    private fun setUpViewModelObservers() {

        //Retrofit Instance
        val songService = RetrofitHelper.getInstance().create(SongService::class.java)
        val repository = SongRepository(songService, requireContext())

        mainViewModel =
            ViewModelProvider(this, MainViewModelFactory(repository)).get(MainViewModel::class.java)

        mainViewModel.songs.observe(requireActivity(), Observer {

            when (it) {
                is APIResponse.Loading -> {
                    binding.shimmerViewContainer.visibility = View.VISIBLE
                }

                is APIResponse.Success -> {
                    it.data?.let {
                        Log.w(TAG,it.toString() )
                        songsAdapter.updateList(it.hits)
                        binding.shimmerViewContainer.visibility = View.GONE
                    }
                }

                is APIResponse.Error -> {
                    it.errorMessage?.let {
                        Snackbar.make(binding.clMainActivityRoot, it, Snackbar.LENGTH_LONG).show()
                        binding.shimmerViewContainer.visibility = View.GONE
                    }
                }
            }
        })

    }


}