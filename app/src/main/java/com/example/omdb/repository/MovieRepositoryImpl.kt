package com.example.omdb.repository

import com.example.omdb.data.SearchMovieResults
import com.example.omdb.repository.remote.MovieServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class MovieRepositoryImpl(private val movieServices: MovieServices) : MovieRepository {

    override fun getSearchResultData(
        searchTitle: String,
        apiKey: String,
        pageIndex: Int
    ): Flow<SearchMovieResults> {
        return flow {
            val searchResult = movieServices.getSearchResultData(
                searchTitle, apiKey, pageIndex
            )
            emit(searchResult)
        }.flowOn(Dispatchers.IO)
    }
}