package ru.gb.androidone.donspb.cinematron.repository

import ru.gb.androidone.donspb.cinematron.model.Movie
import ru.gb.androidone.donspb.cinematron.model.getMoviesList

class MainRepoImpl : MainRepo {
    override fun getMovieFromServer() = Movie()

    override fun getMoviesLocalList() = getMoviesList()
}