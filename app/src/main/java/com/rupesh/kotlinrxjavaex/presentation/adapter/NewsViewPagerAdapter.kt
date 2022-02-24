package com.rupesh.kotlinrxjavaex.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.rupesh.kotlinrxjavaex.view.fragment.NewsFragment
import com.rupesh.kotlinrxjavaex.view.fragment.SavedNewsFragment

class NewsViewPagerAdapter(
    fm: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fm, lifecycle) {

    private val fragmentList = arrayListOf(
        NewsFragment(),
        SavedNewsFragment()
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