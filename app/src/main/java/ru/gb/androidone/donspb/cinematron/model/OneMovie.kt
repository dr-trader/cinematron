package ru.gb.androidone.donspb.cinematron.model

data class OneMovie(
val title: String,
val genres: List<Genre>,
val id: Int,
val overview: String?,
val release_date: String,
val vote_average: Float,
val poster_path: String?,
val backdrop_path: String?,
val runtime: Int
)

data class Genre(
    val id: Int,
    val name: String
)