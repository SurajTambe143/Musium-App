package com.example.musicapp.home.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicapp.R
import com.example.musicapp.adapter.SongsAdapter
import com.example.musicapp.api_call.RetrofitHelper
import com.example.musicapp.api_call.SongService
import com.example.musicapp.databinding.ActivityMainBinding
import com.example.musicapp.repository.APIResponse
import com.example.musicapp.repository.SongRepository
import com.example.musicapp.utility.DayNightModeHelper
import com.example.musicapp.utility.NetworkMonitor
import com.example.musicapp.utility.logEvent
import com.example.musicapp.viewmodel.MainViewModel
import com.example.musicapp.viewmodel.MainViewModelFactory
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings

class MainActivity : AppCompatActivity() {
    lateinit var rootView: View
    private lateinit var analytics: FirebaseAnalytics
    lateinit var mainViewModel: MainViewModel
    private lateinit var networkMonitor: NetworkMonitor
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private val songsAdapter = SongsAdapter {
        networkMonitor.resetStatus()
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra("songDetail", it)
        startActivity(intent)
    }
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        analytics = Firebase.analytics

        //Tool Bar
        setSupportActionBar(binding.myToolbar)

        setUpRecyclerViews()

        setUpViewModelObservers()

//        val searchView=findViewById<com.google.android.material.search.SearchView>(R.id.search_view)
//
//        searchView.editText.setOnEditorActionListener(TextView.OnEditorActionListener { textView, i, keyEvent ->
//            val t=textView.text.toString()
//            if(i == KeyEvent.KEYCODE_ENTER || (keyEvent != null && keyEvent.action == KeyEvent.ACTION_DOWN)) {
//                if (t==""){
//                    Snackbar.make(rootView,"Sorry ! No result found ",Snackbar.LENGTH_SHORT).show()
//                }
//                else{
//                    shimmerView.visibility=View.VISIBLE
//                    rv2 = findViewById(R.id.rv_container1)
//                    rv2.layoutManager = LinearLayoutManager(this@MainActivity)
//                    rv2.adapter = songsAdapter
////                    getSong(t)
//                    mainViewModel.getSongDetails()
//                }
//                return@OnEditorActionListener true
//            } else {
//                return@OnEditorActionListener false
//            }
//        }
//        )

        remoteConfig()

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                Log.d(TAG, "Unique Token for the device is $token")
            }
            else {
                return@OnCompleteListener
            }
        })

    }

    private fun remoteConfig() {
        val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task->
                if (task.isSuccessful){
                    val update= task.result
                    Log.e(TAG, "remoteConfig:*********** $update " )
                    setDarkNightMode()
                }
                else Log.e(TAG, "remoteConfig: FAALEE***********  " )

            }
        remoteConfig.addOnConfigUpdateListener(object : ConfigUpdateListener {
            override fun onUpdate(configUpdate: ConfigUpdate) {
                Log.e(TAG, "Updated keys: " + configUpdate.updatedKeys)

                if (configUpdate.updatedKeys.contains("android_update_mode")) {
                    remoteConfig.activate().addOnCompleteListener {
                        Log.e(TAG, "Updated keys inside if condition : ${it.toString()}")
                    }
                }
            }

            override fun onError(error: FirebaseRemoteConfigException) {
                Log.w(TAG, "Config update error with code: " + error.code, error)
            }
        })
    }

    private fun setDarkNightMode() {
        val mode = FirebaseRemoteConfig.getInstance().getString("mode")
        if (mode=="dark") DayNightModeHelper.setNightMode(this) else DayNightModeHelper.setDayMode(this)
        Log.e(TAG, "remoteConfig Mode:*********** $mode " )
    }

    private fun setUpRecyclerViews() {
        rootView = binding.clMainActivityRoot
        binding.rvContainer.layoutManager = LinearLayoutManager(this@MainActivity)
        binding.rvContainer.adapter = songsAdapter
    }

    private fun setUpViewModelObservers() {

        //Retrofit Instance
        val songService = RetrofitHelper.getInstance().create(SongService::class.java)
        val repository = SongRepository(songService, applicationContext)

        val bundle = Bundle()
        bundle.putString("UserName", "Suraj Tambe")
        logEvent(this, "MainActivity", bundle);

        mainViewModel =
            ViewModelProvider(this, MainViewModelFactory(repository)).get(MainViewModel::class.java)

        networkMonitor = NetworkMonitor(this) {
            mainViewModel.getSongDetails("Perfect")
        }

        mainViewModel.songs.observe(this, Observer {

            when (it) {
                is APIResponse.Loading -> {
                    binding.shimmerViewContainer.visibility = View.VISIBLE
                }

                is APIResponse.Success -> {
                    it.data?.let {
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

    override fun onStart() {
        super.onStart()
        networkMonitor.startNetworkMonitoring()
    }


    override fun onStop() {
        super.onStop()
        networkMonitor.stopNetworkMonitoring()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)

        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as androidx.appcompat.widget.SearchView

        searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d("ex", "onQueryTextSubmit: $query")
                val ex = query.toString()
                if (ex == "") {
                    Snackbar.make(rootView, "Please enter something", Snackbar.LENGTH_LONG).show()
                } else {
                    mainViewModel.getSongDetails(ex)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
        // Configure the search info and add any event listeners.
        return super.onCreateOptionsMenu(menu)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}