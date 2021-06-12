package ru.gb.androidone.donspb.cinematron.viewmodel

import ru.gb.androidone.donspb.cinematron.model.MovieList
import ru.gb.androidone.donspb.cinematron.model.OneMovie

sealed class AppState {
    data class Starting(val movieList: MovieList) : AppState()
    data class Success(val movieData: OneMovie) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}