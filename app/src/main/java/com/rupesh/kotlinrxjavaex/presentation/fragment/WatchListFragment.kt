package com.rupesh.kotlinrxjavaex.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.rupesh.kotlinrxjavaex.R
import com.rupesh.kotlinrxjavaex.data.movie.model.Movie
import com.rupesh.kotlinrxjavaex.databinding.FragmentWatchListBinding
import com.rupesh.kotlinrxjavaex.presentation.adapter.WatchListAdapter
import com.rupesh.kotlinrxjavaex.presentation.viewmodel.MovieViewModel
import com.rupesh.kotlinrxjavaex.presentation.activity.MainActivity
import kotlinx.android.synthetic.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 * This Fragment displays [com.rupesh.kotlinrxjavaex.db.entity.DbMovie]information
 * using a Recycler View.
 * @author Rupesh Mall
 * @since 1.0
 */
class WatchListFragment : Fragment() {

    private var watchListItemBinding: FragmentWatchListBinding? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var watchListAdapter: WatchListAdapter
    private lateinit var movieViewModel: MovieViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        watchListItemBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_watch_list, container, false)
        return watchListItemBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieViewModel = (activity as MainActivity).movieViewModel

        initRecyclerView()
        observeDbMovieList()
        observeStatusMessage()
    }

    private fun observeDbMovieList() {
        movieViewModel.dbMovieListResult.observe(requireParentFragment().viewLifecycleOwner) {
            watchListAdapter.setList(it)
        }
    }

    private fun observeStatusMessage() {
        movieViewModel.statusMessageResult.observe(requireParentFragment().viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { message ->
                Snackbar.make(requireActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    /**
     * Remove DbMovie from the local database on Remove button clicked
     */
    private fun onRemoveButtonClicked(movie: Movie) {
        movieViewModel.deleteMovieFromDB(movie.id)
    }

    /**
     * Initialize thisFragment's RecyclerView
     * RecyclerView uses LinearLayout
     */
    private fun initRecyclerView() {
        recyclerView = watchListItemBinding!!.rvWatchList.also {
            watchListAdapter = WatchListAdapter(requireContext()) {item -> onRemoveButtonClicked(item)}
            it.layoutManager = LinearLayoutManager(requireContext())
            it.adapter = watchListAdapter
        }
    }

    /**
     * Clears the connection between Observables and Observers
     * added in CompositeDisposables
     * And clear the bindings
     */
    override fun onDestroy() {
        super.onDestroy()
        watchListItemBinding = null
        movieViewModel.clear()
    }
}