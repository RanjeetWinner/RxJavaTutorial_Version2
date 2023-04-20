package com.example.movieapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.movieapp.data.model.MovieDetails
import com.example.movieapp.data.network.NetworkState
import com.example.movieapp.data.repository.SingleMovieRepository
import io.reactivex.disposables.CompositeDisposable

class MovieDetailViewModel(private val singleMovieRepository: SingleMovieRepository, movieId:Int) :ViewModel() {

    private val compositeDisposable= CompositeDisposable()

    val movieDetails :LiveData<MovieDetails> by lazy {
        singleMovieRepository.fetchSingleMovieDetail(compositeDisposable,movieId)
    }

    val networkState : LiveData<NetworkState> by lazy {
        singleMovieRepository.getNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}