package com.rupesh.kotlinrxjavaex.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent
import com.rupesh.kotlinrxjavaex.R
import com.rupesh.kotlinrxjavaex.data.movie.db.entity.DbMovie
import com.rupesh.kotlinrxjavaex.databinding.FragmentWatchListBinding
import com.rupesh.kotlinrxjavaex.presentation.adapter.WatchListAdapter
import com.rupesh.kotlinrxjavaex.presentation.viewmodel.DbMovieViewModel
import com.rupesh.kotlinrxjavaex.view.activity.MainActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

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
    private var disposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        watchListItemBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_watch_list, container, false)
        val view: View = watchListItemBinding!!.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbMovieViewModel = (activity as MainActivity).dbMovieViewModel

        initRecyclerView()
        observeDbMovieList()
        filterMovieList()
        displayToastMessage()
    }

    private fun observeDbMovieList() {
        dbMovieViewModel.dbMovieListResult.observe(viewLifecycleOwner, Observer {
            dbMovies.clear()
            dbMovies.addAll(it)
            //dbMovies = it as ArrayList<DbMovie>
            Log.i("FilterListObs", dbMovies.toString())
            //initRecyclerView()
            watchListAdapter.setList(dbMovies)
        })
    }

    private fun displayToastMessage() {
        dbMovieViewModel.statusMessageResult.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        })
    }

    /**
     * This method uses RxJava binding to attach listener to the Edit text to listen to
     * search string changes.
     */
    private fun filterMovieList() {
        disposable.add(
            RxTextView.textChangeEvents(watchListItemBinding!!.svWatchlistFragment)
                .skipInitialValue()
                .debounce(500, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableObserver<TextViewTextChangeEvent>() {
                    override fun onNext(t: TextViewTextChangeEvent) {
                        Log.i("FilterList", t.text().toString())
                        watchListAdapter.filter.filter(t.text())
                    }

                    override fun onError(e: Throwable) {
                        Log.i("FilterList", "onError: ${e.message}")
                    }

                    override fun onComplete() {
                        Log.i("FilterList", "onComplete")
                    }
                })
        )
    }

    /**
     * Remove DbMovie from the local database on Remove button clicked
     */
    private fun onRemoveButtonClicked(dbMovie: DbMovie) {
        dbMovieViewModel.deleteMovieFromDB(dbMovie)
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

    override fun onPause() {
        super.onPause()
        disposable.clear()
    }

    /**
     * Clears the connection between Observables and Observers
     * added in CompositeDisposables
     * And clear the bindings
     */
    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
        watchListItemBinding = null
        dbMovieViewModel.clear()
    }
}