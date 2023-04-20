package com.example.movieapp.data.network.api

import com.example.movieapp.data.model.MovieDetails
import com.example.movieapp.data.model.MovieResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    //https://api.themoviedb.org/3/movie/popular?api_key=<<api_key>>&page=1
    //https://api.themoviedb.org/3/movie/{movie_id}?api_key=<<api_key>>
    //https://api.themoviedb.org/3/

    @GET("movie/popular")
    fun getPopularMovies(@Query("page") page: Int): Single<MovieResponse>


    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id")id:Int):Single<MovieDetails>
}