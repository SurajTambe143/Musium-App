package com.example.musicapp.home

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.R
import com.example.musicapp.adapter.SongsAdapter
import com.example.musicapp.api_call.MusicApi
import com.example.musicapp.model_data.DetailsResponse
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    val songsAdapter = SongsAdapter {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra("songDetail", it)
        startActivity(intent)
    }
    lateinit var progressBar: ProgressBar
    lateinit var rv:RecyclerView
    lateinit var rv2:RecyclerView
    lateinit var rootView: View
    lateinit var shimmerView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.my_toolbar))

//        progressBar=findViewById(R.id.progressbar_main)
        rootView=findViewById(R.id.cl_main_activity_root)
        shimmerView=findViewById(R.id.shimmer_view_container)

        isOnline(this).let {
            if (it){
                Snackbar.make(rootView,"Online",Snackbar.LENGTH_SHORT).show()
                rv = findViewById(R.id.rv_container)
                rv.layoutManager = LinearLayoutManager(this@MainActivity)
                rv.adapter = songsAdapter
                getSong("Perfect")
            }else {
                Snackbar.make(rootView,"Offline",Snackbar.LENGTH_LONG).show()
            }
        }

        val searchView=findViewById<com.google.android.material.search.SearchView>(R.id.search_view)

        searchView.editText.setOnEditorActionListener(TextView.OnEditorActionListener { textView, i, keyEvent ->
            val t=textView.text.toString()
            if(i == KeyEvent.KEYCODE_ENTER || (keyEvent != null && keyEvent.action == KeyEvent.ACTION_DOWN)) {
                if (t==""){
                    Snackbar.make(rootView,"Sorry ! No result found ",Snackbar.LENGTH_SHORT).show()
                }
                else{
                    shimmerView.visibility=View.VISIBLE
                    rv2 = findViewById(R.id.rv_container1)
                    rv2.layoutManager = LinearLayoutManager(this@MainActivity)
                    rv2.adapter = songsAdapter
                    getSong(t)
                }
                return@OnEditorActionListener true
            } else {
                return@OnEditorActionListener false
            }
        }
        )

//        findViewById<Button>(R.id.search_btn).setOnClickListener {
//            val inputSong =findViewById<TextInputEditText>(R.id.input_song)
//            val song=inputSong.text.toString()
//            if (song==""){
//                Toast.makeText(this@MainActivity,"No result found",Toast.LENGTH_LONG).show()
//            }
//            else{
//                progressBar.visibility=View.VISIBLE
//                rv = findViewById(R.id.rv_container)
//                rv.layoutManager = LinearLayoutManager(this@MainActivity)
//                rv.adapter = songsAdapter
//                getSong(song)
//            }
//        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)

        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as androidx.appcompat.widget.SearchView

        searchView.setOnQueryTextListener(object: androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d("ex", "onQueryTextSubmit: $query")
                val ex=query.toString()
                rv2 = findViewById(R.id.rv_container1)
                rv2.layoutManager = LinearLayoutManager(this@MainActivity)
                rv2.adapter = songsAdapter
                getSong(ex)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        // Configure the search info and add any event listeners.
        return super.onCreateOptionsMenu(menu)
    }

    private fun getSong(songName:String) {

        lifecycleScope.launch(Dispatchers.IO){
            try {
                val songs = MusicApi.getMusicService().getSongDetails(songName)
                songs.enqueue(object : Callback<DetailsResponse> {
                    override fun onResponse(
                        call: Call<DetailsResponse>,
                        response: Response<DetailsResponse>
                    ) {
                        val song = response.body()
                        song?.let {
                            Log.e("Song Details",it.toString())
                            songsAdapter.updateList(it.hits)
                        }
                        shimmerView.visibility=View.GONE
                    }
                    override fun onFailure(call: Call<DetailsResponse>, t: Throwable) {
                        Log.e("Api Call", "Failure")
                        Snackbar.make(rootView,"Sorry ! No result found ",Snackbar.LENGTH_SHORT).show()
                        shimmerView.visibility=View.GONE
                    }

                })
            }catch (e:Exception){
                Log.e("Api Call","Error in API call")

            }

        }

    }
    private fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
        return false
    }
}