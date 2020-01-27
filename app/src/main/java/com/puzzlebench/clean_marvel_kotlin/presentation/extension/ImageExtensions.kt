package com.puzzlebench.clean_marvel_kotlin.presentation.extension

import android.widget.ImageView
import com.puzzlebench.clean_marvel_kotlin.R
import com.squareup.picasso.Picasso

fun ImageView.getImageByUrl(url: String) {
    Picasso.with(context)
            .load(url)
            .fit()
            .centerCrop()
            .placeholder(R.drawable.ic_marvel_logo)
            .into(this)
}
