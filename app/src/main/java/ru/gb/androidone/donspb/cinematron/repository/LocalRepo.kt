package ru.gb.androidone.donspb.cinematron.repository

import ru.gb.androidone.donspb.cinematron.model.MovieListItem

interface LocalRepo {
    fun getLocalList(): List<MovieListItem>
    fun saveEntity(movieItem: MovieListItem)
}