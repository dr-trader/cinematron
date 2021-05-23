package ru.gb.androidone.donspb.cinematron.viewmodel

import ru.gb.androidone.donspb.cinematron.model.Movie

sealed class AppState {
    data class Starting(val recentMovieData: List<Movie>, val newMovieData: List<Movie>, val topMovieData: List<Movie>) : AppState()
//    data class Success(val movieData: List<Movie>) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}