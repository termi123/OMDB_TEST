package com.example.omdb.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.omdb.data.Movie
import com.example.omdb.data.MovieLoadMore
import com.example.omdb.databinding.ItemLoadingBinding
import com.example.omdb.databinding.ItemMovieBinding

class SearchResultAdapter(callback: DiffUtil.ItemCallback<Movie>) :
    ListAdapter<Movie, RecyclerView.ViewHolder>(callback) {

    companion object {
        internal const val VIEW_TYPE_ITEM = 0
        internal const val VIEW_TYPE_LOADING = 1
    }

    lateinit var listener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(movie: Movie)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ITEM) {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemMovieBinding.inflate(
                layoutInflater, parent, false
            )
            SearchResultViewHolder(binding)
        } else {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemLoadingBinding.inflate(
                layoutInflater, parent, false
            )
            LoadingViewHolder(binding)
        }
    }

    override fun getItemCount() = currentList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SearchResultViewHolder) {
            holder.bind(searchItem = currentList[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (currentList[position] is MovieLoadMore) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    fun updateItems(newList: ArrayList<Movie>?) {
        newList?.let { it ->
            val list = arrayListOf<Movie>()
            list.addAll(currentList)
            list.addAll(it)
            list.removeAll { item -> item is MovieLoadMore }
            submitList(ArrayList(list.toSet()))
        } ?: kotlin.run {
            submitList(arrayListOf())
        }
    }

    fun updateLoadMore() {
        val list = arrayListOf<Movie?>()
        list.addAll(currentList)
        list.add(MovieLoadMore())
        submitList(ArrayList(list))
    }

    fun removeLoadMore() {
        val list = arrayListOf<Movie?>()
        list.addAll(currentList)
        list.removeAll { item -> item is MovieLoadMore }
        submitList(ArrayList(list))
    }

    inner class SearchResultViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(searchItem: Movie?) {
            searchItem?.let { movie ->
                binding.movie = searchItem
                binding.mcvItemLayout.setOnClickListener {
                    listener.onItemClick(movie)
                }
            }
        }

    }

    inner class LoadingViewHolder(binding: ItemLoadingBinding) :
        RecyclerView.ViewHolder(binding.root)

    class MovieDiffUtil : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: Movie,
            newItem: Movie
        ): Boolean {
            return oldItem.imdbID.isNotEmpty() && newItem.imdbID.isNotEmpty() && (oldItem.imdbID == newItem.imdbID)
        }
    }
}