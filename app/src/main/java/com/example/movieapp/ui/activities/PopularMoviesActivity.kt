package com.example.movieapp.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar


import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movieapp.R
import com.example.movieapp.data.network.api.ApiClient
import com.example.movieapp.data.network.NetworkState

import com.example.movieapp.ui.adapters.MoviePageListAdapter
import com.example.movieapp.viewmodel.PopularMoviesViewModel
import com.example.movieapp.data.repository.PopularMoviesPagedListRepository
import com.example.movieapp.databinding.ActivityMoviesPopularBinding
import com.google.android.material.elevation.SurfaceColors

class PopularMoviesActivity : AppCompatActivity() {
    lateinit var binding: ActivityMoviesPopularBinding
    private lateinit var navController: NavController
  /*  private lateinit var viewModel: PopularMoviesViewModel
    lateinit var movieRepository: PopularMoviesPagedListRepository*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMoviesPopularBinding.inflate(layoutInflater)
        var view=binding.root
        setSupportActionBar(binding.toolbar)
        setContentView(view)

        setToolbar()
    }

    private fun setToolbar() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf(R.id.popularMoviesFragment),
            fallbackOnNavigateUpListener = ::onSupportNavigateUp
        )
        findViewById<Toolbar>(R.id.toolbar)
            .setupWithNavController(navController, appBarConfiguration)
    }


//    override fun onSupportNavigateUp(): Boolean {
//        return navController.navigateUp() || super.onSupportNavigateUp()
//    }
}