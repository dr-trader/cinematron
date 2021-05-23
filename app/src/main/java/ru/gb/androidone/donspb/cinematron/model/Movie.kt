package ru.gb.androidone.donspb.cinematron.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(
    val movieTitle: String = "Discovering Kotlin",
    val movieYear: Int = 2021,
    val movieDescr: String = "It\'s a long story about hard studying and some coding.",
) : Parcelable

fun getMoviesList() : List<Movie> {
    return listOf(
        Movie(),
        Movie(),
        Movie(),
        Movie(),
        Movie(),
        Movie(),
        Movie(),
        Movie(),
        Movie(),
        Movie(),
    )
}