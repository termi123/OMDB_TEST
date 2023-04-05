package com.example.omdb.data

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class SearchMovieResults(
    @SerializedName("Response")
    val resultResponse: String? = "",
    @SerializedName("Search")
    val resultData: List<Movie?>? = listOf(),
    @SerializedName("totalResults")
    val resultTotal: String? = ""
) : Parcelable