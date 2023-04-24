package com.example.movieapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.movieapp.data.model.MovieDetails
import com.example.movieapp.data.network.NetworkState
import com.example.movieapp.data.repository.SingleMovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(private val singleMovieRepository: SingleMovieRepository) :ViewModel() {

    private var movieId:Int=1
    fun setMovieId(movieId:Int){
        this.movieId=movieId
    }
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