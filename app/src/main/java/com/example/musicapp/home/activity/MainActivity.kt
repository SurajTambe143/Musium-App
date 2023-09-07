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
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.R
import com.example.musicapp.adapter.SongsAdapter
import com.example.musicapp.api_call.RetrofitHelper
import com.example.musicapp.api_call.SongService
import com.example.musicapp.repository.APIResponse
import com.example.musicapp.repository.SongRepository
import com.example.musicapp.utility.NetworkCheck
import com.example.musicapp.viewmodel.MainViewModel
import com.example.musicapp.viewmodel.MainViewModelFactory
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    lateinit var rv:RecyclerView
    lateinit var rv2:RecyclerView
    lateinit var rootView: View
    lateinit var shimmerView: View
    lateinit var mainViewModel: MainViewModel
    private val songsAdapter = SongsAdapter {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra("songDetail", it)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.my_toolbar))
        rootView=findViewById(R.id.cl_main_activity_root)
        shimmerView=findViewById(R.id.shimmer_view_container)
        rv = findViewById(R.id.rv_container)
        rv.layoutManager = LinearLayoutManager(this@MainActivity)
        rv.adapter = songsAdapter

        val songService= RetrofitHelper.getInstance().create(SongService::class.java)
        val repository = SongRepository(songService,applicationContext)
        mainViewModel= ViewModelProvider(this, MainViewModelFactory(repository)).get(MainViewModel::class.java)
        mainViewModel.setQuerry("Hit")
        mainViewModel.songs.observe(this, Observer {

            when(it){
                is APIResponse.Loading-> {
                    shimmerView.visibility=View.VISIBLE
                }
                is APIResponse.Success->{
                    it.data?.let {
                        songsAdapter.updateList(it.hits)
                        shimmerView.visibility=View.GONE
                    }
                }
                is APIResponse.Error->{
                    Snackbar.make(rootView,"Some Error Occured",Snackbar.LENGTH_LONG).show()
                    shimmerView.visibility=View.GONE
                }
            }
        })

        var networkCheck=NetworkCheck()

        if(!networkCheck.isOnline(applicationContext))Snackbar.make(rootView,"Offline ",Snackbar.LENGTH_LONG).show()


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
        
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)

        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as androidx.appcompat.widget.SearchView

        searchView.setOnQueryTextListener(object: androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d("ex", "onQueryTextSubmit: $query")
                val ex=query.toString()
                if (ex==""){
                Snackbar.make(rootView,"Please enter something",Snackbar.LENGTH_LONG).show()
                }
                else{
                    mainViewModel.setQuerry(ex)
                    mainViewModel.getSongDetails()
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
}