package ru.gb.androidone.donspb.cinematron.model

class RepositoryImpl : Repository {
    override fun getMovieFromServer() = Movie()

    override fun getMoviesLocalList() = getMoviesList()
}