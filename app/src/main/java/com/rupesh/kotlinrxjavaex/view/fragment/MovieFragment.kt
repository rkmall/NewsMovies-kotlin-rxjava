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
import com.google.android.material.snackbar.Snackbar
import com.rupesh.kotlinrxjavaex.R
import com.rupesh.kotlinrxjavaex.data.movie.db.entity.DbMovie
import com.rupesh.kotlinrxjavaex.data.movie.model.Movie
import com.rupesh.kotlinrxjavaex.presentation.util.Resource
import com.rupesh.kotlinrxjavaex.databinding.FragmentMovieBinding
import com.rupesh.kotlinrxjavaex.databinding.LayoutAddMovieDialogBinding
import com.rupesh.kotlinrxjavaex.presentation.adapter.MovieAdapter
import com.rupesh.kotlinrxjavaex.presentation.viewmodel.MovieViewModel
import com.rupesh.kotlinrxjavaex.view.activity.MainActivity

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
    private lateinit var recyclerView: RecyclerView
    private lateinit var movieAdapter: MovieAdapter
    private var movies = ArrayList<Movie>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        movieFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_movie, container, false)

        return movieFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieViewModel = (activity as MainActivity).movieViewModel

        initRecyclerView()
        observeMoviesList()
        observeStatusMessage()
    }

    // Get a list of Movie and observe the LiveData<List<Movie>>
    private fun observeMoviesList() {
        movieViewModel.movieLiveDataResult.observe(requireParentFragment().viewLifecycleOwner, Observer() {
            when(it) {
                is Resource.Success -> {
                    movies = it.data as ArrayList<Movie>
                    movieAdapter.setList(movies)
                }
                is Resource.Error -> Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                else -> Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
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

    private fun observeStatusMessage() {
        movieViewModel.statusMessageResult.observe(requireParentFragment().viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { message ->
                Snackbar.make(requireActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun onItemClick(movie: Movie) {
        val movieDetailFragment = MovieDetailFragment()

        val bundle = Bundle()
        bundle.putParcelable("movie", movie)
        movieDetailFragment.arguments = bundle

        val ft = activity?.supportFragmentManager?.beginTransaction()
        ft?.replace(R.id.frame_layout_main, movieDetailFragment)
        ft?.addToBackStack(null)
        ft?.commit()
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
        val dialog = Dialog(requireContext())
        dialog.setContentView(addMovieDialogBinding.root)

        addMovieDialogBinding.let {
            it.tvAddMovieName.text = movie.originalTitle
            it.ok.setOnClickListener {
                val movieToBeSaved = DbMovie(
                    0L,
                    movie.originalTitle,
                    movie.voteAverage,
                    movie.overview,
                    movie.releaseDate,
                    movie.posterPath
                )

                movieViewModel.addMovieToDB(movieToBeSaved)
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