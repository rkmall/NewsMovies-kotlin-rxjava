package com.rupesh.kotlinrxjavaex.view

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
import com.rupesh.kotlinrxjavaex.model.Movie
import com.rupesh.kotlinrxjavaex.repository.MovieRepository
import com.rupesh.kotlinrxjavaex.service.RetrofitInstance
import com.rupesh.kotlinrxjavaex.viewmodel.MovieViewModel
import com.rupesh.kotlinrxjavaex.viewmodelfactory.MovieVMFactory

/**
 * A simple [Fragment] subclass.
 * Use the [MovieFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MovieFragment : Fragment() {

    private var movieFragmentBinding: FragmentMovieBinding? = null

    private lateinit var movieViewModel: MovieViewModel

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

        val movieViewModelFactory: MovieVMFactory = MovieVMFactory(MovieRepository(RetrofitInstance.instance))
        movieViewModel = ViewModelProvider(this, movieViewModelFactory).get(MovieViewModel::class.java)

        // API call
        getMovieList()

        return view
    }

    fun getMovieList() {
        movieViewModel.getMovieList()
        movieViewModel.movieLiveData.observe(viewLifecycleOwner, Observer() {
            movies = it as ArrayList<Movie>
            initRecyclerView()
        })
    }

    fun initRecyclerView() {
        recyclerView = movieFragmentBinding!!.layoutContentMovieFragment.rvMovieFragment

        movieAdapter = MovieAdapter(requireContext(), movies)

        if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
            recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        else
            recyclerView.layoutManager = GridLayoutManager(requireContext(), 4)

        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = movieAdapter
        movieAdapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        movieViewModel.clear()
        movieFragmentBinding = null
    }
}