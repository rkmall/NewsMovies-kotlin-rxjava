package com.rupesh.kotlinrxjavaex.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.rupesh.kotlinrxjavaex.view.fragment.MovieFragment
import com.rupesh.kotlinrxjavaex.view.fragment.WatchListFragment

/**
 * A simple [FragmentStateAdapter] subclass.
 * This adapter manages the two Fragments:
 * [com.rupesh.kotlinrxjavaex.view.MovieFragment] and
 * [com.rupesh.kotlinrxjavaex.view.WatchListFragment]
 * @author Rupesh Mall
 * @since 1.0
 */
class MovieViewPagerAdapter(
    fm: FragmentManager,
    lifecycle: Lifecycle
): FragmentStateAdapter(fm, lifecycle) {

    private val fragmentList = arrayListOf(
        MovieFragment(),
        WatchListFragment()
    )

    // Returns Fragments count
    override fun getItemCount(): Int {
        return fragmentList.size
    }

    // Returns Fragment
    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}