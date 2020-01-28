package com.puzzlebench.clean_marvel_kotlin.presentation.extension

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.puzzlebench.clean_marvel_kotlin.R

fun ImageView.getImageByUrl(url: String) {
    Glide.with(context)
            .load(url)
            .centerCrop()
            .placeholder(R.drawable.ic_marvel_logo)
            .into(this)
}
