package com.rupesh.kotlinrxjavaex.presentation.ui.features.news.adapter

import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.rupesh.kotlinrxjavaex.presentation.ui.features.BaseFragment

class NewsViewPagerAdapter(
    _fragmentList: ArrayList<BaseFragment<out ViewDataBinding>>,
    fm: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fm, lifecycle) {

    private val fragmentList = _fragmentList

    // Returns Fragments count
    override fun getItemCount(): Int {
        return fragmentList.size
    }

    // Returns Fragment
    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}