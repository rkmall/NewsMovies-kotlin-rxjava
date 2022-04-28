package com.rupesh.kotlinrxjavaex.presentation.util

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.rupesh.kotlinrxjavaex.BuildConfig
import com.rupesh.kotlinrxjavaex.R
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class RowBindingAdapters {

    companion object {

        @BindingAdapter("loadNewsImageFromUrl")
        @JvmStatic
        fun loadNewsImageFromUrl(imageView: ImageView, imageUrl: String?) {
            Glide.with(imageView.context)
                .load(imageUrl)
                .error(R.drawable.ic_error_placeholder)
                .placeholder(R.drawable.ic_error_placeholder)
                .into(imageView)
        }

        @BindingAdapter("loadMovieImageFromUrl")
        @JvmStatic
        fun loadMovieImageFromUrl(imageView: ImageView, imageUrl: String) {
            Glide.with(imageView.context)
                .load("${BuildConfig.MOVIE_POSTER_PATH}${imageUrl}")
                .error(R.drawable.ic_error_placeholder)
                .placeholder(R.drawable.ic_error_placeholder)
                .into(imageView)
        }

        @BindingAdapter("newsTimeStampToDateTime")
        @JvmStatic
        fun newsTimeStampToDateTime(textView: TextView, timeStamp: String) {
            val instant = Instant.parse(timeStamp)
            val zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.of("Europe/London"))
            textView.text = DateTimeFormatter.ofPattern("EEE, d MMM yyyy HH:mm").format(zonedDateTime)
        }
    }
}