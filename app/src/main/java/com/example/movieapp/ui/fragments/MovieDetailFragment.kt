package com.example.movieapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.movieapp.data.model.MovieDetails
import com.example.movieapp.data.network.NetworkState
import com.example.movieapp.data.network.api.ApiClient
import com.example.movieapp.data.network.api.ApiService
import com.example.movieapp.data.network.api.POSTER_BASE_URL
import com.example.movieapp.data.repository.SingleMovieRepository
import com.example.movieapp.databinding.FragmentMovieDetailBinding
import com.example.movieapp.viewmodel.MovieDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject


@AndroidEntryPoint
class MovieDetailFragment () : Fragment() {
    lateinit var binding: FragmentMovieDetailBinding
    lateinit var movieDetailViewModel: MovieDetailViewModel
    //private lateinit var singleMovieRepository: SingleMovieRepository
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return setContentView(inflater,container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        val movieId = bundle?.getInt("id")
        //val movieId:Int?=activity?.intent?.getIntExtra("id",1)
        setup(movieId)
        setupObservers()
    }

    private fun setupObservers() {
        movieDetailViewModel.movieDetails.observe(viewLifecycleOwner, Observer {
            bindUI(it)
        })

        movieDetailViewModel.networkState.observe(viewLifecycleOwner, Observer {
            binding.progressBar.visibility = if(it== NetworkState.LOADING) View.VISIBLE else View.GONE
            binding.txtError.visibility= if(it== NetworkState.ERROR) View.VISIBLE else View.GONE
        })
    }

    private fun bindUI(movieDetails: MovieDetails) {
        binding.movieTitle.text=movieDetails.title
        binding.movieTagline.text=movieDetails.tagline
        binding.movieReleaseDate.text=movieDetails.releaseDate
        binding.movieRating.text=movieDetails.rating.toString()
        binding.movieRuntime.text=movieDetails.runtime.toString()
        binding.movieOverview.text=movieDetails.overview

        val formatCurrency = NumberFormat.getCurrencyInstance(Locale.US)
        binding.movieBudget.text=formatCurrency.format(movieDetails.budget)
        binding.movieRevenue.text=formatCurrency.format(movieDetails.revenue)


        val moviePosterUrl= POSTER_BASE_URL +movieDetails.posterPath
        Glide.with(this)
            .load(moviePosterUrl)
            .into(binding.ivMoviePoster)
        // movie_title.text=movieDetails.title
    }

    private fun setup(movieId: Int?) {
//        val apiService: ApiService = ApiClient.getClient()
//        singleMovieRepository= SingleMovieRepository(apiService)

        movieDetailViewModel=getViewModel()
        //setup(movieId)
    }

    private fun setContentView(inflater: LayoutInflater, container: ViewGroup?):View{
        binding= FragmentMovieDetailBinding.inflate(inflater,container,false)
        return binding.root
    }

    private fun getViewModel(): MovieDetailViewModel {
        /*return ViewModelProvider(this,object : ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MovieDetailViewModel(singleMovieRepository) as T
            }
        })[MovieDetailViewModel::class.java]*/
        return ViewModelProvider(this).get(MovieDetailViewModel::class.java)
    }

}