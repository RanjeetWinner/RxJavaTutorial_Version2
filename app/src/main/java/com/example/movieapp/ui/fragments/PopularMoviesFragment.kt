package com.example.movieapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movieapp.R
import com.example.movieapp.data.network.NetworkState
import com.example.movieapp.data.network.api.ApiClient
import com.example.movieapp.data.repository.PopularMoviesPagedListRepository
import com.example.movieapp.databinding.FragmentPopularMoviesBinding
import com.example.movieapp.ui.adapters.MoviePageListAdapter
import com.example.movieapp.viewmodel.PopularMoviesViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PopularMoviesFragment ( ) : Fragment(),MyInterface {
    lateinit var binding: FragmentPopularMoviesBinding
    lateinit var popularmoviesViewModel: PopularMoviesViewModel
    lateinit var movieRepository: PopularMoviesPagedListRepository
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return setContentView(inflater,container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
        val movieAdapter= MoviePageListAdapter(requireContext(),this)
        setupRecyclerView(movieAdapter)
        setupObservers(movieAdapter)
    }

    private fun setupObservers(movieAdapter: MoviePageListAdapter) {
        popularmoviesViewModel.moviePagedList.observe(viewLifecycleOwner, Observer {
            movieAdapter.submitList(it)
        })

        popularmoviesViewModel.networkState.observe(viewLifecycleOwner, Observer {
            binding.progressBarPopular.visibility = if(popularmoviesViewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            binding.txtErrorPopular.visibility = if(popularmoviesViewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE

            if(!popularmoviesViewModel.listIsEmpty()){
                movieAdapter.setNetworkState(it)
            }
        })
    }

    private fun setupRecyclerView(movieAdapter: MoviePageListAdapter) {

        val gridLayoutManager= GridLayoutManager(requireContext(),3)

        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {
                val viewType=movieAdapter.getItemViewType(position)
                if(viewType ==  movieAdapter.MOVIE_VIEW_TYPE) return 1
                else return 3
            }
        }

        binding.rvMovieList.layoutManager=gridLayoutManager
        binding.rvMovieList.setHasFixedSize(true)
        binding.rvMovieList.adapter=movieAdapter
    }

    private fun setup() {
//        val apiService= ApiClient.getClient()
//        movieRepository= PopularMoviesPagedListRepository(apiService)
        popularmoviesViewModel=getViewModel()
    }


    private fun setContentView(inflater: LayoutInflater, container: ViewGroup?):View{
        binding= FragmentPopularMoviesBinding.inflate(inflater,container,false)
        return binding.root
    }

    private fun getViewModel() : PopularMoviesViewModel {/*
        return ViewModelProvider(this,object : ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return PopularMoviesViewModel(movieRepository) as T
            }
        })[PopularMoviesViewModel::class.java]*/
        return ViewModelProvider(this).get(PopularMoviesViewModel::class.java)
    }

    override fun onClick(moviId:Int) {
        val bundle = bundleOf("id" to moviId)
        findNavController().navigate(
            R.id.action_popularMoviesFragment_to_movieDetailFragment,bundle
        )
    }

}

open interface MyInterface{
    fun onClick(moviId: Int)
}