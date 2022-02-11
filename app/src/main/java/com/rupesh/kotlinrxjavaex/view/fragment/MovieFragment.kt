package com.rupesh.kotlinrxjavaex.view.fragment

import android.app.Dialog
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent
import com.rupesh.kotlinrxjavaex.R
import com.rupesh.kotlinrxjavaex.data.movie.model.Movie
import com.rupesh.kotlinrxjavaex.databinding.FragmentMovieBinding
import com.rupesh.kotlinrxjavaex.databinding.LayoutAddMovieDialogBinding
import com.rupesh.kotlinrxjavaex.presentation.adapter.MovieAdapter
import com.rupesh.kotlinrxjavaex.presentation.viewmodel.DbMovieViewModel
import com.rupesh.kotlinrxjavaex.presentation.viewmodel.MovieViewModel
import com.rupesh.kotlinrxjavaex.view.activity.MainActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * A simple [Fragment] subclass.
 * This Fragment displays [com.rupesh.kotlinrxjavaex.model.Movie] information
 * using a Recycler View.
 * @author Rupesh Mall
 * @since 1.0
 */

class MovieFragment : Fragment() {

    private lateinit var movieFragmentBinding: FragmentMovieBinding
    private lateinit var addMovieDialogBinding: LayoutAddMovieDialogBinding
    private lateinit var movieViewModel: MovieViewModel
    private lateinit var dbMovieViewModel: DbMovieViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var movieAdapter: MovieAdapter
    private var movies = ArrayList<Movie>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        movieFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_movie, container, false)

        return movieFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieViewModel = (activity as MainActivity).movieViewModel

        dbMovieViewModel = (activity as MainActivity).dbMovieViewModel

        initRecyclerView()

        observeMoviesList()

        displayToastMessage()
    }

    // Get a list of Movie and observe the LiveData<List<Movie>>
    private fun observeMoviesList() {
        movieViewModel.movieLiveDataResult.observe(viewLifecycleOwner, Observer() {
            movies = it as ArrayList<Movie>
            movieAdapter.setList(movies)
        })
    }

    private fun displayToastMessage() {
        movieViewModel.statusMessageResult.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        })
    }

    /**
     * Initialize thisFragment's RecyclerView
     * RecyclerView uses GridLayoutManager
     * For Portrait view, the span is 2
     * For Landscape view, the span is 4
     */
    private fun initRecyclerView() {
        recyclerView = movieFragmentBinding.layoutContentMovieFragment.rvMovieFragment.also {

            if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
                it.layoutManager = GridLayoutManager(requireContext(), 2)
            else
                it.layoutManager = GridLayoutManager(requireContext(), 4)

            movieAdapter = MovieAdapter(requireContext(), {item -> onItemClick(item)}, {item -> onItemLongClick(item)})

            it.itemAnimator = DefaultItemAnimator()
            it.adapter = movieAdapter
        }
    }

    private fun onItemClick(movie: Movie) {
        val movieDetailFragment = MovieDetailFragment()

        val bundle = Bundle()
        bundle.putParcelable("movie", movie)
        movieDetailFragment.arguments = bundle

        val fm = activity?.supportFragmentManager
        val fragmentTransaction = fm?.beginTransaction()
        fragmentTransaction?.replace(R.id.frame_layout_main, movieDetailFragment)
        fragmentTransaction?.addToBackStack(null)
        fragmentTransaction?.commit()
    }

    /**
     * Display a dialog to add Movie into the Watch List observed by
     * [com.rupesh.kotlinrxjavaex.view.WatchListFragment]
     * Callback method that handles the database operation to add DbMovie
     * [com.rupesh.kotlinrxjavaex.db.entity.DbMovie]
     * into App's database
     * @param movie the Movie [com.rupesh.kotlinrxjavaex.model.Movie]
     */
    private fun onItemLongClick(movie: Movie) {
        addMovieDialogBinding = LayoutAddMovieDialogBinding.inflate(LayoutInflater.from(requireContext()))
        val dialog: Dialog = Dialog(requireContext())
        dialog.setContentView(addMovieDialogBinding.root)

        addMovieDialogBinding.let {
            it.tvAddMovieName.text = movie.original_title
            it.ok.setOnClickListener {
                val title: String = movie.original_title
                val rating: Double = movie.vote_average
                val overview: String = movie.overview
                val releaseDate: String = movie.release_date
                val posterPath: String = movie.poster_path
                dbMovieViewModel.addMovieToDB(0L, title, rating, overview, releaseDate, posterPath)
                dialog.dismiss()
            }

            it.cancel.setOnClickListener {
                dialog.dismiss()
            }

            dialog.setCancelable(true)
            dialog.show()
        }
    }

    /**
     * Clears the connection between Observables and Observers
     * added in CompositeDisposables
     * And nullify the bindings
     */
    override fun onDestroy() {
        super.onDestroy()
        movieViewModel.clear()
    }
}