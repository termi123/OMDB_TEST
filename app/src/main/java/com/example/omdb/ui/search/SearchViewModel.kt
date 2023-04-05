package com.example.omdb.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.omdb.data.Movie
import com.example.omdb.data.ResponseType
import com.example.omdb.data.Status
import com.example.omdb.repository.MovieRepository
import com.example.omdb.utils.API_KEY
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class SearchViewModel(private val movieRepository: MovieRepository) : ViewModel() {

    private val _searchResult = MutableLiveData<ResponseType<ArrayList<Movie>>>()
    val searchResult: LiveData<ResponseType<ArrayList<Movie>>> = _searchResult

    fun isLoading(): Boolean = searchResult.value?.status == Status.LOADING

    private var pageIndex = 1

    fun getMoreSearchResultData(searchTitle: String) {
        pageIndex++
        searchResultData(searchTitle)
    }

    fun getSearchResultData(searchTitle: String, page: Int = 1) {
        pageIndex = page
        searchResultData(searchTitle)
    }

    private fun searchResultData(searchTitle: String) {
        viewModelScope.launch {
            movieRepository
                .getSearchResultData(searchTitle, API_KEY, pageIndex)
                .onStart {
                    _searchResult.postValue(ResponseType.Loading(true))
                }
                .catch { exception ->
                    _searchResult.postValue(ResponseType.Error(exception))
                }
                .collect { _moviesList ->
                    _moviesList.resultData?.let { listOfMovies ->
                        val moviesList = arrayListOf<Movie>()
                        moviesList.addAll(listOfMovies.filterNotNull())
                        moviesList.sortByDescending {
                            it.year
                        }
                        _searchResult.postValue(ResponseType.Success(moviesList))
                    }
                }
        }
    }
}