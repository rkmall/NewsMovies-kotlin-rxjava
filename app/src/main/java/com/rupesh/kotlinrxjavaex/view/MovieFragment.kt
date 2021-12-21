package com.rupesh.kotlinrxjavaex.view

import android.app.Dialog
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rupesh.kotlinrxjavaex.adapter.MovieAdapter
import com.rupesh.kotlinrxjavaex.databinding.FragmentMovieBinding
import com.rupesh.kotlinrxjavaex.databinding.LayoutAddMovieDialogBinding
import com.rupesh.kotlinrxjavaex.db.MovieDB
import com.rupesh.kotlinrxjavaex.model.Movie
import com.rupesh.kotlinrxjavaex.repository.DbMovieRepository
import com.rupesh.kotlinrxjavaex.repository.MovieRepository
import com.rupesh.kotlinrxjavaex.service.RetrofitInstance
import com.rupesh.kotlinrxjavaex.viewmodel.DbMovieViewModel
import com.rupesh.kotlinrxjavaex.viewmodel.MovieViewModel
import com.rupesh.kotlinrxjavaex.viewmodelfactory.DbMovieVMFactory
import com.rupesh.kotlinrxjavaex.viewmodelfactory.MovieVMFactory

/**
 * A simple [Fragment] subclass.
 * This Fragment displays [com.rupesh.kotlinrxjavaex.model.Movie] information
 * using a Recycler View.
 * @author Rupesh Mall
 * @since 1.0
 */

class MovieFragment : Fragment() {

    private var movieFragmentBinding: FragmentMovieBinding? = null

    private var addMovieDialogBinding: LayoutAddMovieDialogBinding? = null

    private lateinit var movieViewModel: MovieViewModel

    private lateinit var dbMovieViewModel: DbMovieViewModel

    private lateinit var recyclerView: RecyclerView

    private lateinit var movieAdapter: MovieAdapter

    private var movies = ArrayList<Movie>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        movieFragmentBinding = FragmentMovieBinding.inflate(inflater, container, false)
        val view: View = movieFragmentBinding!!.root

        val movieVMFactory: MovieVMFactory = MovieVMFactory(MovieRepository(RetrofitInstance.instance))
        movieViewModel = ViewModelProvider(this, movieVMFactory)[MovieViewModel::class.java]

        val dbMovieVMFactory: DbMovieVMFactory = DbMovieVMFactory(DbMovieRepository(requireContext(), MovieDB.getDB(requireContext())))
        dbMovieViewModel = ViewModelProvider(this, dbMovieVMFactory)[DbMovieViewModel::class.java]

        // API call
        getMovieList()

        return view
    }

    // Get a list of Movie and observe the LiveData<List<Movie>>
    fun getMovieList() {
        movieViewModel.getMovieList()
        movieViewModel.movieLiveData.observe(viewLifecycleOwner, Observer() {
            movies = it as ArrayList<Movie>
            initRecyclerView()
        })
    }

    /**
     * Initialize thisFragment's RecyclerView
     * RecyclerView uses GridLayoutManager
     * For Portrait view, the span is 2
     * For Landscape view, the span is 4
     */
    private fun initRecyclerView() {
        recyclerView = movieFragmentBinding!!.layoutContentMovieFragment.rvMovieFragment.also {

            if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
                it.layoutManager = GridLayoutManager(requireContext(), 2)
            else
                it.layoutManager = GridLayoutManager(requireContext(), 4)

            movieAdapter = MovieAdapter(requireContext(), movies) {item -> onItemClick(item)}

            it.itemAnimator = DefaultItemAnimator()
            it.adapter = movieAdapter
        }

        movieAdapter.notifyDataSetChanged()
    }

    /**
     * Display a dialog to add Movie into the Watch List observed by
     * [com.rupesh.kotlinrxjavaex.view.WatchListFragment]
     * Callback method that handles the database operation to add DbMovie
     * [com.rupesh.kotlinrxjavaex.db.entity.DbMovie]
     * into App's database
     * @param movie the Movie [com.rupesh.kotlinrxjavaex.model.Movie]
     */
    fun onItemClick(movie: Movie) {
        addMovieDialogBinding = LayoutAddMovieDialogBinding.inflate(LayoutInflater.from(requireContext()))
        val dialog: Dialog = Dialog(requireContext())
        dialog.setContentView(addMovieDialogBinding!!.root)

        addMovieDialogBinding?.let {

            it.tvAddMovieName.text = movie.original_title

            it.ok.setOnClickListener {
                val id: Long = 0L
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
        movieFragmentBinding = null
    }
}