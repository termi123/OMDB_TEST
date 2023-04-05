package com.example.omdb.repository

import com.example.omdb.data.SearchMovieResults
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    fun getSearchResultData(
        searchTitle: String, apiKey: String, pageIndex: Int
    ): Flow<SearchMovieResults>

}