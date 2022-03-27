package ru.gb.androidone.donspb.cinematron.model

data class MovieList(
    val results: List<MovieListItem>?
)

data class MovieListItem(
    val title: String,
    val id: Int,
    val release_date: String,
    val vote_average: Float,
    val poster_path: String?
)
