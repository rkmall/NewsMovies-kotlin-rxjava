package com.rupesh.kotlinrxjavaex.presentation.util

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.snackbar.Snackbar


fun FragmentActivity.transaction(layout: Int, fragment: Fragment) {
    val ft = this.supportFragmentManager.beginTransaction()
    ft.replace(layout, fragment)
    ft.addToBackStack(null)
    ft.commit()
}

fun Context?.toast(text: CharSequence, duration: Int = Toast.LENGTH_LONG) {
    this?.let { Toast.makeText(it, text, duration).show() }
}

fun View.snackBar(text: CharSequence, duration: Int = Snackbar.LENGTH_LONG) {
    this.let { Snackbar.make(it, text, duration).show() }
}

fun View.setVisible() {
    this.visibility = View.VISIBLE
}

fun View.setVisibleGone() {
    this.visibility = View.GONE
}




