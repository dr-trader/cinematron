package ru.gb.androidone.donspb.cinematron.repository

import ru.gb.androidone.donspb.cinematron.model.Movie

interface MainRepo {
    fun getMovieFromServer(): Movie
    fun getMoviesLocalList(): List<Movie>
}