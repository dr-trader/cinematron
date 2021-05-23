package ru.gb.androidone.donspb.cinematron.model

interface Repository {
    fun getMovieFromServer(): Movie
    fun getMoviesLocalList(): List<Movie>
}