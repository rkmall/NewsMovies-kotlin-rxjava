package com.rupesh.kotlinrxjavaex.view

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.rupesh.kotlinrxjavaex.R
import com.rupesh.kotlinrxjavaex.adapter.MainActivityFragmentsAdapter
import com.rupesh.kotlinrxjavaex.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var viewPager2: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var mainActivityFragmentsAdapter: MainActivityFragmentsAdapter
    private lateinit var viewPagerCallBack: ViewPager2.OnPageChangeCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = binding.toolbarMainActivity
        setSupportActionBar(toolbar)
        toolbar.title = "Movie Gallery"

        tabLayout = binding.mainTabLayout
        viewPager2 = binding.mainViewPager

        // Set ViewPager2 with MainActivityFragmentsAdapter
        val fragmentManager: FragmentManager = supportFragmentManager
        mainActivityFragmentsAdapter = MainActivityFragmentsAdapter(fragmentManager, lifecycle)
        viewPager2.adapter = mainActivityFragmentsAdapter

        // Set TabLayout
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

    override fun onDestroy() {
        super.onDestroy()
        viewPager2.unregisterOnPageChangeCallback(viewPagerCallBack)
    }
}