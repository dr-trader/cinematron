package ru.gb.androidone.donspb.cinematron.model

import java.util.*

data class MovieList(
    val moviesList: List<MovieListItem>?
)

data class MovieListItem(
    val title: String?,
    val id: Int?,
    val release_date: Date?,
    val vote_average: Float?
)
