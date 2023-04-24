package com.example.movieapp.data.repository


import androidx.lifecycle.LiveData
import androidx.lifecycle.switchMap
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.movieapp.data.network.api.ApiService
import com.example.movieapp.data.network.api.POST_PER_PAGE
import com.example.movieapp.data.model.Movie
import com.example.movieapp.data.network.NetworkState
import com.example.movieapp.data.repository.datasource.MovieDataSourceFactory
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class PopularMoviesPagedListRepository @Inject constructor(private val apiService: ApiService) {

    lateinit var moviesPagedList : LiveData<PagedList<Movie>>
    lateinit var movieDataSourceFactory: MovieDataSourceFactory

    fun fetchLiveMoviePagedList(compositeDisposable: CompositeDisposable) : LiveData<PagedList<Movie>>{
        movieDataSourceFactory= MovieDataSourceFactory(apiService,compositeDisposable)

        val config=PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        moviesPagedList=LivePagedListBuilder(movieDataSourceFactory,config).build()
        return moviesPagedList
    }


    fun getNetworkState() : LiveData<NetworkState> {
        return movieDataSourceFactory.moviesLiveDataSource.switchMap { movieDataSource -> movieDataSource.networkState  }
    }
}