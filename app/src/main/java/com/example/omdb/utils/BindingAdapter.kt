package com.example.omdb.utils

import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.example.omdb.R

@BindingAdapter("imgSrcGlide")
fun setImageGlide(image: AppCompatImageView, url: String?) {
    Glide.with(image.context).load(GlideUrl(url)).diskCacheStrategy(DiskCacheStrategy.DATA)
        .error(R.drawable.ic_launcher_foreground)
        .into(image)
}
