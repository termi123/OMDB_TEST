package com.example.omdb.ui.search

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.omdb.R
import com.example.omdb.data.Movie
import com.example.omdb.data.Status
import com.example.omdb.databinding.SearchFragmentBinding
import com.example.omdb.ui.search.adapter.SearchResultAdapter
import com.example.omdb.utils.NoConnectivityException
import com.example.omdb.utils.dismissKeyboard
import com.example.omdb.utils.showToast
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchFragment : Fragment(R.layout.search_fragment), SearchResultAdapter.OnItemClickListener {

    private val viewModel: SearchViewModel by viewModel()
    private lateinit var binding: SearchFragmentBinding

    private var searchResultAdapter = SearchResultAdapter(SearchResultAdapter.MovieDiffUtil())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
        setupObserve()
    }

    private fun setupObserve() {
        viewModel.searchResult.removeObservers(viewLifecycleOwner)
        viewModel.searchResult.observe(viewLifecycleOwner) { _result ->
            when (_result.status) {
                Status.SUCCESS -> {
                    showLoading(false)
                    _result._data?.let {
                        searchResultAdapter.updateItems(it)
                        Log.d(this.javaClass.simpleName, "${it.size}")
                    } ?: run {
                        searchResultAdapter.updateItems(null)
                        showError(getString(R.string.no_movie_found))
                    }
                }
                Status.LOADING -> {
                    showLoading(true)
                }
                Status.ERROR -> {
                    showLoading(false)
                    _result.exception?.let {
                        if (it is NoConnectivityException) {
                            context?.showToast(getString(R.string.no_network))
                        } else {
                            context?.showToast(
                                it.message ?: getString(R.string.something_when_wrong)
                            )
                        }
                    } ?: run {
                        context?.showToast(getString(R.string.something_when_wrong))
                    }
                    searchResultAdapter.removeLoadMore()
                }
            }
        }
    }

    private fun setupView(view: View) {
        binding = SearchFragmentBinding.bind(view)
        binding.rvSearchResult.apply {
            itemAnimator = null
            layoutManager = GridLayoutManager(context, 2)
            this.adapter = searchResultAdapter
        }
        binding.rvSearchResult.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as GridLayoutManager?
                if (!viewModel.isLoading()) {
                    if (layoutManager != null &&
                        searchResultAdapter.currentList.size > 0 &&
                        layoutManager.findLastCompletelyVisibleItemPosition() == searchResultAdapter.currentList.size - 1
                    ) {
                        searchResultAdapter.updateLoadMore()
                        viewModel.getMoreSearchResultData(binding.searchView.query.toString())
                    }
                }
            }
        })
        searchResultAdapter.listener = this
        binding.searchView.setIconifiedByDefault(true)
        binding.searchView.isFocusable = true
        binding.searchView.isIconified = false
        binding.searchView.clearFocus()
        binding.searchView.requestFocusFromTouch()
        binding.searchView.setOnCloseListener {
            searchResultAdapter.updateItems(null)
            true
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    searchItem(it)
                    context?.dismissKeyboard(binding.searchView)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchResultAdapter.updateItems(null)
                return true
            }

        })
        binding.swipeRefreshLayout.setOnRefreshListener {
            if (viewModel.isLoading()) return@setOnRefreshListener
            searchItem(binding.searchView.query.toString())
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.searchView.isEnabled = false
            binding.tvError.isVisible = false
            return
        }
        binding.searchView.isEnabled = true
        binding.swipeRefreshLayout.isRefreshing = false
    }


    private fun searchItem(searchText: String) {
        if (searchText.length > 3) {
            searchResultAdapter.updateItems(null)
            binding.swipeRefreshLayout.isRefreshing = true
            viewModel.getSearchResultData(searchText)
        } else {
            showLoading(false)
            showError(getString(R.string.keyword_error))
        }
    }

    private fun showError(error: String) {
        binding.tvError.isVisible = true
        binding.tvError.text = error
    }

    override fun onItemClick(movie: Movie) {
        findNavController().navigate(
            R.id.action_searchFragment_to_detailFragment, bundleOf(
                "movie" to movie,
            )
        )
    }
}