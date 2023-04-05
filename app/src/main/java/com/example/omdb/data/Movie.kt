package com.example.omdb.data

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
open class Movie : Parcelable {

    @SerializedName("Title")
    val title: String = ""

    @SerializedName("Year")
    val year: String = ""

    @SerializedName("imdbID")
    val imdbID: String = ""

    @SerializedName("Type")
    val type: String = ""

    @SerializedName("Poster")
    val poster: String = ""

}

@Keep
@Parcelize
class MovieLoadMore : Movie(), Parcelable {
    val id = 0
}
