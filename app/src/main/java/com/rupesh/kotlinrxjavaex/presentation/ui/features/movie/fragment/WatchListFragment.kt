package com.rupesh.kotlinrxjavaex.presentation.ui.features.movie.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rupesh.kotlinrxjavaex.R
import com.rupesh.kotlinrxjavaex.data.movie.model.Movie
import com.rupesh.kotlinrxjavaex.databinding.FragmentWatchListBinding
import com.rupesh.kotlinrxjavaex.presentation.ui.features.BaseFragment
import com.rupesh.kotlinrxjavaex.presentation.ui.features.movie.adapter.WatchListAdapter
import com.rupesh.kotlinrxjavaex.presentation.ui.viewmodel.MovieViewModel
import com.rupesh.kotlinrxjavaex.presentation.util.snackBar
import java.util.*

/**
 * A simple [Fragment] subclass.
 * This Fragment displays [com.rupesh.kotlinrxjavaex.db.entity.DbMovie]information
 * using a Recycler View.
 * @author Rupesh Mall
 * @since 1.0
 */
class WatchListFragment : BaseFragment<FragmentWatchListBinding>() {

    private val movieViewModel: MovieViewModel by viewModels(ownerProducer = {requireParentFragment()})

    private lateinit var recyclerView: RecyclerView
    private lateinit var watchListAdapter: WatchListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        observeDbMovieList()
        observeStatusMessage()
    }

    private fun observeDbMovieList() {
        movieViewModel.savedMovies.observe(requireParentFragment().viewLifecycleOwner) {
            watchListAdapter.setList(it)
        }
    }

    private fun observeStatusMessage() {
        movieViewModel.eventMessage.observe(requireParentFragment().viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { message ->
                requireParentFragment().requireView().snackBar(message)
            }
        }
    }

    /**
     * Remove DbMovie from the local database on Remove button clicked
     */
    private fun onRemoveButtonClicked(movie: Movie) {
        movieViewModel.deleteMovie(movie.id)
    }

    /**
     * Initialize thisFragment's RecyclerView
     * RecyclerView uses LinearLayout
     */
    private fun initRecyclerView() {
        recyclerView = binding.rvWatchList.also {
            watchListAdapter = WatchListAdapter(requireContext()) {item -> onRemoveButtonClicked(item)}
            it.layoutManager = LinearLayoutManager(requireContext())
            it.adapter = watchListAdapter
        }
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentWatchListBinding {
        return DataBindingUtil.inflate(inflater, R.layout.fragment_watch_list, container, false)
    }
}