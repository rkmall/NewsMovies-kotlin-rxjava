package com.rupesh.kotlinrxjavaex.presentation.ui.features.movie.fragment

import android.app.Dialog
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rupesh.kotlinrxjavaex.R
import com.rupesh.kotlinrxjavaex.data.movie.model.Movie
import com.rupesh.kotlinrxjavaex.databinding.FragmentMovieBinding
import com.rupesh.kotlinrxjavaex.databinding.LayoutAddMovieDialogBinding
import com.rupesh.kotlinrxjavaex.presentation.ui.features.BaseFragment
import com.rupesh.kotlinrxjavaex.presentation.ui.features.movie.adapter.MovieAdapter
import com.rupesh.kotlinrxjavaex.presentation.ui.viewmodel.MovieViewModel
import com.rupesh.kotlinrxjavaex.presentation.util.*

/**
 * A simple [Fragment] subclass.
 * This Fragment displays [com.rupesh.kotlinrxjavaex.model.Movie] information
 * using a Recycler View.
 * @author Rupesh Mall
 * @since 1.0
 */

class MovieFragment : BaseFragment<FragmentMovieBinding>() {

    private val movieViewModel: MovieViewModel by viewModels(ownerProducer = {requireParentFragment()})

    private var addMovieDialogBinding: LayoutAddMovieDialogBinding? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var movieAdapter: MovieAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        observeMoviesList()
        observeStatusMessage()
    }

    private fun observeMoviesList() {
        movieViewModel.popularMovies.observe(requireParentFragment().viewLifecycleOwner) {
            if (it != null) {
                render(it)
            }
        }
    }

    private fun render(resource: Resource<List<Movie>>) {
        when(resource) {
            is Resource.Loading -> onLoad()
            is Resource.Success -> onSuccess(resource)
            is Resource.Error -> onError(resource)
        }
    }

    private fun onLoad() = with(binding) {
        progressBarMovie.setVisible()
    }

    private fun onSuccess(resource: Resource<List<Movie>>) = with(binding) {
        progressBarMovie.setVisibleGone()
        movieAdapter.setList(resource.data!!)
    }

    private fun onError(resource: Resource<List<Movie>>) = with(binding) {
        progressBarMovie.setVisibleGone()
        requireParentFragment().requireView().snackBar(resource.message as String)
    }

    /**
     * Initialize thisFragment's RecyclerView
     * RecyclerView uses GridLayoutManager
     * For Portrait view, the span is 2
     * For Landscape view, the span is 4
     */
    private fun initRecyclerView() {
        recyclerView = binding.rvMovieFragment.also {
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
        movieViewModel.eventMessage.observe(requireParentFragment().viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { message ->
                requireParentFragment().requireView().snackBar(message)
            }
        }
    }

    private fun onItemClick(movie: Movie) {
        val movieDetailFragment = MovieDetailFragment()
        val bundle = Bundle()
        bundle.putParcelable("movie", movie)
        movieDetailFragment.arguments = bundle
        activity?.transaction(R.id.frame_layout_main, movieDetailFragment)
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
        dialog.setContentView(addMovieDialogBinding!!.root)

        addMovieDialogBinding?.let {
            it.tvAddMovieName.text = movie.originalTitle
            it.ok.setOnClickListener {
                movieViewModel.saveMovie(movie)
                dialog.dismiss()
            }

            it.cancel.setOnClickListener {
                dialog.dismiss()
            }

            dialog.setCancelable(true)
            dialog.show()
        }
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentMovieBinding {
        return DataBindingUtil.inflate(inflater, R.layout.fragment_movie, container, false)
    }

    /**
     * Clears the connection between Observables and Observers
     * added in CompositeDisposables
     * And nullify the bindings
     */
    override fun onDestroy() {
        super.onDestroy()
        addMovieDialogBinding = null
    }
}