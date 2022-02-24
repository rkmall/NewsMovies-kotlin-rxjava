package com.rupesh.kotlinrxjavaex.view.activity

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.rupesh.kotlinrxjavaex.R
import com.rupesh.kotlinrxjavaex.data.util.AppConstantsData
import com.rupesh.kotlinrxjavaex.databinding.ActivityMainBinding
import com.rupesh.kotlinrxjavaex.presentation.util.AppConstantsPresentation
import com.rupesh.kotlinrxjavaex.presentation.util.AppPreferenceHelper
import com.rupesh.kotlinrxjavaex.presentation.util.NetworkChecker
import com.rupesh.kotlinrxjavaex.presentation.viewmodel.MovieViewModel
import com.rupesh.kotlinrxjavaex.presentation.viewmodel.NewsViewModel
import com.rupesh.kotlinrxjavaex.presentation.viewmodelfactory.MovieVMFactory
import com.rupesh.kotlinrxjavaex.presentation.viewmodelfactory.NewsVMFactory
import com.rupesh.kotlinrxjavaex.view.fragment.MovieContainerFragment
import com.rupesh.kotlinrxjavaex.view.fragment.NewsContainerFragment
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
    lateinit var newsVMFactory: NewsVMFactory
    lateinit var newsViewModel: NewsViewModel

    private lateinit var preferenceHelper: AppPreferenceHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val sharedPrefFirstRun = getSharedPreferences(AppConstantsPresentation.FIRST_RUN, Context.MODE_PRIVATE)
        val sharedPrefTimePeriod = getSharedPreferences(AppConstantsPresentation.TIME_PERIOD, Context.MODE_PRIVATE)
        preferenceHelper = AppPreferenceHelper(sharedPrefFirstRun, sharedPrefTimePeriod)

        movieViewModel = ViewModelProvider(this, movieVMFactory)[MovieViewModel::class.java]
        newsViewModel = ViewModelProvider(this, newsVMFactory)[NewsViewModel::class.java]

        checkNetwork()
        replaceFragment(NewsContainerFragment())
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
                    replaceFragment(NewsContainerFragment())
                    true
                }

                R.id.movies -> {
                    replaceFragment(MovieContainerFragment())
                    true
                }

                else -> {
                    replaceFragment(NewsContainerFragment())
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
        newsViewModel.getNewsList(AppConstantsData.DEFAULT_COUNTRY_NEWS, AppConstantsData.DEFAULT_PAGE_NEWS)
        newsViewModel.getSavedNewsArticles()
        movieViewModel.getMovieList()
        movieViewModel.getAllMovieFromDb()
        preferenceHelper.storeFirstRun()
        preferenceHelper.storeCurrentTime(System.currentTimeMillis())
    }
}