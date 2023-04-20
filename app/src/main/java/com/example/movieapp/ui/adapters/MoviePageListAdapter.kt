package com.example.movieapp.ui.adapters



import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.data.model.Movie
import com.example.movieapp.data.network.NetworkState
import com.example.movieapp.data.network.api.POSTER_BASE_URL
import com.example.movieapp.ui.fragments.MyInterface


class MoviePageListAdapter(val context: Context,val myInterface: MyInterface) : PagedListAdapter<Movie,RecyclerView.ViewHolder>(
    MovieDiffCallback()
) {

    val MOVIE_VIEW_TYPE=1
    val NETWORK_VIEW_TYPE=2

    private var networkState :NetworkState?=null
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(getItemViewType(position) == MOVIE_VIEW_TYPE){
            (holder as MovieItemViewHolder).bind(getItem(position), context =context,myInterface )
        }else
        {
            (holder as NetworkStateViewHolder).bind(networkState = networkState)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater=LayoutInflater.from(parent.context)
        val view:View

        if (viewType == MOVIE_VIEW_TYPE) {
            view = layoutInflater.inflate(
                R.layout.movie_list_item, parent, false
            )
            return MovieItemViewHolder(view)
        } else {
            view = layoutInflater.inflate(
                R.layout.network_state_item, parent, false
            )
            return NetworkStateViewHolder(view)
        }
    }

    private fun hasExtraRow():Boolean{
        return networkState!=null && networkState!=NetworkState.LOADED
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if(hasExtraRow() && position==itemCount-1){
            NETWORK_VIEW_TYPE
        }else{
            MOVIE_VIEW_TYPE
        }
    }

    fun setNetworkState(networkState: NetworkState){
        val previousState =this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState=networkState
        val hasExtraRow=hasExtraRow()

        if(hasExtraRow!=hasExtraRow){
            if(hadExtraRow)
            {
                notifyItemRemoved(super.getItemCount())
            }else
            {
                notifyItemInserted(super.getItemCount())
            }
        }else if(hasExtraRow && previousState !=networkState){
            notifyItemChanged(itemCount-1)
        }

    }

    class MovieDiffCallback : DiffUtil.ItemCallback<Movie>(){
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id ==newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
         }
    }

    class MovieItemViewHolder(view: View) : RecyclerView.ViewHolder(view){

        var movieTitle=itemView.findViewById<TextView>(R.id.cv_movie_title)
        var movieReleaseDate=itemView.findViewById<TextView>(R.id.cv_movie_release_date)
        var moviePoster=itemView.findViewById<ImageView>(R.id.cv_iv_movie_poster)
        fun bind(movie: Movie?, context: Context, myInterface: MyInterface){
            movieTitle.text=movie?.title
            movieReleaseDate.text=movie?.releaseDate

            val moviePosterUrl= POSTER_BASE_URL +movie?.posterPath

            Glide.with(itemView.context)
                .load(moviePosterUrl)
                .into(moviePoster)

/*            itemView.setOnClickListener{
                val intent= Intent(context, SingleMovie::class.java)
                intent.putExtra("id",movie?.id)
                context.startActivity(intent)
            }*/

/*            itemView.setOnClickListener{
                myInterface.onClick(movie?.id?:1)
            }*/
            //val bundle= bundleOf("id" to movie?.id)

            itemView.setOnClickListener{
               // val bundle = bundleOf("id" to movie?.id)
                val bundle : Bundle?=Bundle()
                bundle?.putInt("id",movie?.id?:1)
                it.findNavController().navigate(R.id.action_popularMoviesFragment_to_movieDetailFragment,bundle)
            }

/*            itemView.setOnClickListener{
                val mFrag = MovieDetailFragment()
                val bundle  = Bundle()
                bundle.putInt("id",movie?.id?:1)

                mFrag.setArguments(bundle)

                (itemView.getContext() as FragmentActivity).getSupportFragmentManager().beginTransaction().replace(
                    com.example.movieapp.R.id.nav_host, mFrag)
                    .commit()
            }*/

        }
    }

    class NetworkStateViewHolder(view: View): RecyclerView.ViewHolder(view){
        var proressBar=itemView.findViewById<ProgressBar>(R.id.progress_bar_item)
        var errorMsgItem=itemView.findViewById<TextView>(R.id.error_msg_item)
        fun bind(networkState: NetworkState?)
        {
            if(networkState!=null && networkState==NetworkState.LOADING){
                proressBar.visibility=View.VISIBLE
            }else{
                proressBar.visibility=View.GONE
            }

            if(networkState!=null && networkState==NetworkState.ERROR){
                errorMsgItem.visibility=View.VISIBLE
                errorMsgItem.text=networkState.msg
            }
            else if (networkState!=null && networkState==NetworkState.ENDOFLIST){
                errorMsgItem.visibility=View.VISIBLE
                errorMsgItem.text=networkState.msg
            }
            else{
                errorMsgItem.visibility=View.GONE
            }
        }
    }




}