package com.rupesh.kotlinrxjavaex.view

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.rupesh.kotlinrxjavaex.R
import com.rupesh.kotlinrxjavaex.presentation.adapter.MainActivityFragmentsAdapter
import com.rupesh.kotlinrxjavaex.databinding.ActivityMainBinding
import com.rupesh.kotlinrxjavaex.presentation.viewmodel.DbMovieViewModel
import com.rupesh.kotlinrxjavaex.presentation.viewmodel.MovieViewModel
import com.rupesh.kotlinrxjavaex.presentation.viewmodelfactory.DbMovieVMFactory
import com.rupesh.kotlinrxjavaex.presentation.viewmodelfactory.MovieVMFactory
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

    private lateinit var viewPager2: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var mainActivityFragmentsAdapter: MainActivityFragmentsAdapter
    private lateinit var viewPagerCallBack: ViewPager2.OnPageChangeCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setToolbar()

        setViewPager()

        setTabLayout()

        movieViewModel = ViewModelProvider(this, movieVMFactory)[MovieViewModel::class.java]

        dbMovieViewModel = ViewModelProvider(this, dbMovieVMFactory)[DbMovieViewModel::class.java]

        /**
         * On ViewPager2 page change, gets the tabLayout position
         */
        viewPagerCallBack = object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    private fun setToolbar() {
        val toolbar: Toolbar = binding.toolbarMainActivity
        setSupportActionBar(toolbar)
    }

    // Set ViewPager2 with MainActivityFragmentsAdapter
    private fun setViewPager() {
        viewPager2 = binding.mainViewPager
        val fragmentManager: FragmentManager = supportFragmentManager
        mainActivityFragmentsAdapter = MainActivityFragmentsAdapter(fragmentManager, lifecycle)
        viewPager2.adapter = mainActivityFragmentsAdapter
    }

    // Set TabLayout

    private fun setTabLayout() {
        tabLayout = binding.mainTabLayout
        tabLayout.addTab(tabLayout.newTab().setText("Movie Info"))
        tabLayout.addTab(tabLayout.newTab().setText("Watch List"))
        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewPager2.currentItem = tab!!.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }

    /**
     * Unregister ViewPager2 callback
     */
    override fun onDestroy() {
        super.onDestroy()
        viewPager2.unregisterOnPageChangeCallback(viewPagerCallBack)
    }
}