package com.example.movieapp.data.repository.datasource


import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.movieapp.data.network.api.ApiService
import com.example.movieapp.data.model.Movie
import com.example.movieapp.data.repository.datasource.PopularMoviesDataSource
import io.reactivex.disposables.CompositeDisposable

class MovieDataSourceFactory(private val apiService: ApiService, private val compositeDisposable: CompositeDisposable) :androidx.paging.DataSource.Factory<Int,Movie>() {
    val moviesLiveDataSource = MutableLiveData<PopularMoviesDataSource>()
    override fun create(): DataSource<Int, Movie> {
        val popularMoviesDataSource= PopularMoviesDataSource(apiService,compositeDisposable)
        moviesLiveDataSource.postValue(popularMoviesDataSource)
        return popularMoviesDataSource
    }
}