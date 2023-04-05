package com.example.omdb.ui.detail

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.omdb.R
import com.example.omdb.data.Movie
import com.example.omdb.databinding.DetailFragmentBinding

class DetailFragment : Fragment(R.layout.detail_fragment) {

    private lateinit var binding: DetailFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DetailFragmentBinding.bind(view)
        setupData()
    }

    private fun setupData() {
        val movie = if (Build.VERSION.SDK_INT >= 33) {
            arguments?.getParcelable("movie", Movie::class.java)
        } else {
            arguments?.getParcelable("movie")
        }
        movie?.let {
            setupUI(it)
        } ?: kotlin.run {
            findNavController().popBackStack()
        }
    }

    private fun setupUI(it: Movie) {
        updatePostAndTitle(it)
        setupBackButton()
    }

    private fun setupBackButton() {
        binding.ivBackButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun updatePostAndTitle(movie: Movie) {
        Glide
            .with(this)
            .load(movie.poster)
            .centerCrop()
            .placeholder(R.drawable.ic_launcher_foreground)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(binding.ivPoster)
        binding.tvTitle.text = movie.title
        binding.tvYear.text = movie.year
    }
}