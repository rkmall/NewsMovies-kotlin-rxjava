package com.rupesh.kotlinrxjavaex.view.activity

import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.rupesh.kotlinrxjavaex.R
import com.rupesh.kotlinrxjavaex.databinding.ActivityMainBinding
import com.rupesh.kotlinrxjavaex.presentation.util.AppPreferenceHelper
import com.rupesh.kotlinrxjavaex.presentation.util.NetworkChecker
import com.rupesh.kotlinrxjavaex.presentation.viewmodel.DbMovieViewModel
import com.rupesh.kotlinrxjavaex.presentation.viewmodel.MovieViewModel
import com.rupesh.kotlinrxjavaex.presentation.viewmodel.NewsViewModel
import com.rupesh.kotlinrxjavaex.presentation.viewmodelfactory.DbMovieVMFactory
import com.rupesh.kotlinrxjavaex.presentation.viewmodelfactory.MovieVMFactory
import com.rupesh.kotlinrxjavaex.presentation.viewmodelfactory.NewsVMFactory
import com.rupesh.kotlinrxjavaex.view.fragment.MovieContainerFragment
import com.rupesh.kotlinrxjavaex.view.fragment.NewsFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * A simple [AppCompatActivity] subclass.
 * The main Activity and the entry point of the App
 * @author Rupesh Mall
 * @since 1.0
 */

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var movieVMFactory: MovieVMFactory
    lateinit var movieViewModel: MovieViewModel

    @Inject
    lateinit var dbMovieVMFactory: DbMovieVMFactory
    lateinit var dbMovieViewModel: DbMovieViewModel

    @Inject
    lateinit var newsVMFactory: NewsVMFactory
    lateinit var newsViewModel: NewsViewModel

    private lateinit var preferenceHelper: AppPreferenceHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        preferenceHelper = AppPreferenceHelper(this)

        movieViewModel = ViewModelProvider(this, movieVMFactory)[MovieViewModel::class.java]
        dbMovieViewModel = ViewModelProvider(this, dbMovieVMFactory)[DbMovieViewModel::class.java]
        newsViewModel = ViewModelProvider(this, newsVMFactory)[NewsViewModel::class.java]

        checkNetwork()

        replaceFragment(NewsFragment())

        initBottomNavView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    private fun checkNetwork() {
        if(NetworkChecker.isNetWorkAvailable(this)) {
            appLaunchInitiate()
        } else {
            Toast.makeText(this, "Please turn on mobile network", Toast.LENGTH_LONG).show()
        }
    }

    private fun initBottomNavView() {
        binding.bnvMain.setOnItemSelectedListener {

            when(it.itemId) {
                R.id.news -> {
                    replaceFragment(NewsFragment())
                    true
                }

                R.id.movies -> {
                    replaceFragment(MovieContainerFragment())
                    true
                }

                else -> {
                    replaceFragment(NewsFragment())
                    true
                }
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fm = supportFragmentManager
        val fragmentTransaction = fm.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout_main, fragment)
        fragmentTransaction.commit()
    }

    /**
     * Load all the necessary data
     */
    private fun appLaunchInitiate() {
        newsViewModel.getNewsList("us", 1)
        movieViewModel.getMovieList()
        dbMovieViewModel.getAllMovieFromDb()
        preferenceHelper.storeFirstRun()
        preferenceHelper.storeCurrentTime(System.currentTimeMillis())
    }
}