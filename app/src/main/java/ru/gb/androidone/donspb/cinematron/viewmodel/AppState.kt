package ru.gb.androidone.donspb.cinematron.viewmodel

import ru.gb.androidone.donspb.cinematron.model.Movie

sealed class AppState {
    data class Success(val movieData: Movie) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}