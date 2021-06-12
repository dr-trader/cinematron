package ru.gb.androidone.donspb.cinematron.repository

import ru.gb.androidone.donspb.cinematron.model.MovieList

interface MainRepo {
//    fun getMovieFromServer(): Movie
//    fun getMoviesLocalList(): List<Movie>

    fun getMovieListFromServer(listType: String, callback: retrofit2.Callback<MovieList>)
}