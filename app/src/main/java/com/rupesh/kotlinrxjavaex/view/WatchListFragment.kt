package com.rupesh.kotlinrxjavaex.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rupesh.kotlinrxjavaex.adapter.WatchListAdapter
import com.rupesh.kotlinrxjavaex.databinding.FragmentWatchListBinding
import com.rupesh.kotlinrxjavaex.db.MovieDB
import com.rupesh.kotlinrxjavaex.db.entity.DbMovie
import com.rupesh.kotlinrxjavaex.repository.DbMovieRepository
import com.rupesh.kotlinrxjavaex.viewmodel.DbMovieViewModel
import com.rupesh.kotlinrxjavaex.viewmodelfactory.DbMovieVMFactory

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
    private lateinit var dbMovieViewModel: DbMovieViewModel
    private var dbMovies = ArrayList<DbMovie>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        watchListItemBinding = FragmentWatchListBinding.inflate(inflater, container, false)
        val view: View = watchListItemBinding!!.root

        val dbMovieVMFactory: DbMovieVMFactory = DbMovieVMFactory(DbMovieRepository(requireContext(), MovieDB.getDB(requireContext())))
        dbMovieViewModel = ViewModelProvider(this, dbMovieVMFactory)[DbMovieViewModel::class.java]

        getMovieListFromDb()

        return view
    }


    fun getMovieListFromDb() {
        dbMovieViewModel.getAllMovieFromDb()
        dbMovieViewModel.dbMovieMutableLiveData.observe(viewLifecycleOwner, Observer {
            dbMovies = it as ArrayList<DbMovie>
            initRecyclerView()
        })
    }


    fun onRemoveButtonClicked(dbMovie: DbMovie) {
        dbMovieViewModel.deleteMovieFromDB(dbMovie)
    }

    /**
     * Initialize thisFragment's RecyclerView
     * RecyclerView uses LinearLayout
     */
    private fun initRecyclerView() {
        recyclerView = watchListItemBinding!!.rvWatchList.also {
            watchListAdapter = WatchListAdapter(requireContext(), dbMovies) {item -> onRemoveButtonClicked(item)}
            it.layoutManager = LinearLayoutManager(requireContext())
            it.adapter = watchListAdapter
        }

        watchListAdapter.notifyDataSetChanged()
    }

    /**
     * Clears the connection between Observables and Observers
     * added in CompositeDisposables
     * And clear the bindings
     */
    override fun onDestroy() {
        super.onDestroy()
        watchListItemBinding = null
        dbMovieViewModel.clear()
    }
}