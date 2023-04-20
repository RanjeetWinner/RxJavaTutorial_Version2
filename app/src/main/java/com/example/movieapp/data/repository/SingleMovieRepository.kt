package com.example.movieapp.data.repository

import androidx.lifecycle.LiveData
import com.example.movieapp.data.network.api.ApiService
import com.example.movieapp.data.model.MovieDetails
import com.example.movieapp.data.network.NetworkState
import com.example.movieapp.data.repository.datasource.SingleMovieDataSource
import io.reactivex.disposables.CompositeDisposable

class SingleMovieRepository(private val apiService: ApiService) {
    lateinit var apiDataSource: SingleMovieDataSource

    fun fetchSingleMovieDetail(compositeDisposable: CompositeDisposable,movieId :Int) : LiveData<MovieDetails>{
        apiDataSource= SingleMovieDataSource(apiService,compositeDisposable)
        apiDataSource.fetchMovieDetail(movieId)

        return apiDataSource.movieDetailResponse
    }


    fun getNetworkState() : LiveData<NetworkState>{
        return apiDataSource.networkState
    }
}