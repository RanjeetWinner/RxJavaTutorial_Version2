package com.example.movieapp.data.repository.datasource

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.movieapp.data.network.api.ApiService
import com.example.movieapp.data.model.MovieDetails
import com.example.movieapp.data.network.NetworkState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception

class SingleMovieDataSource(private val apiService: ApiService, private val compositeDisposable: CompositeDisposable) {
    private val _networkState=MutableLiveData<NetworkState>()
    val networkState : LiveData<NetworkState>
        get() = _networkState

    private val _movideDetailResponse = MutableLiveData<MovieDetails>()
    val movieDetailResponse : LiveData<MovieDetails>
        get() = _movideDetailResponse

    fun fetchMovieDetail(movieId:Int){
        _networkState.postValue(NetworkState.LOADING)

        try {
            compositeDisposable.add(
                apiService.getMovieDetails(movieId)
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        _movideDetailResponse.postValue(it)
                        _networkState.postValue(NetworkState.LOADED)
                    },{
                        _networkState.postValue(NetworkState.ERROR)
                        Log.e("Moviedatasource",it.message!!)
                    })
            )
        }catch (e: Exception)
        {
            Log.e("Moviedatasource",e.message!!)
        }
    }
}