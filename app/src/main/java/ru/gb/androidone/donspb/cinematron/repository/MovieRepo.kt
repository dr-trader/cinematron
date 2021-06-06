package ru.gb.androidone.donspb.cinematron.repository

import ru.gb.androidone.donspb.cinematron.model.OneMovie

interface MovieRepo {
    fun getMovieDetailsFromServer(movieID: Int, callback: retrofit2.Callback<OneMovie>)
}