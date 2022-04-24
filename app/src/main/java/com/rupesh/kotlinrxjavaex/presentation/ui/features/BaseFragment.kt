package com.rupesh.kotlinrxjavaex.presentation.ui.features

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BaseFragment<DB : ViewDataBinding> : Fragment() {

    private var mBinding: DB? = null
    val binding get() = mBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = getBinding(inflater, container)
        return mBinding!!.root
    }

    protected abstract fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): DB


    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}