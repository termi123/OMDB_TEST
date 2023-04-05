package com.example.omdb.repository.remote

import com.example.omdb.data.SearchMovieResults
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieServices {

    @GET("?type=movie")
    suspend fun getSearchResultData(
        @Query(value = "s") searchTitle: String,
        @Query(value = "apiKey") apiKey: String,
        @Query(value = "page") pageIndex: Int
    ): SearchMovieResults
}