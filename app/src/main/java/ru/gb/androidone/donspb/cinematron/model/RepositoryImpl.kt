package ru.gb.androidone.donspb.cinematron.model

class RepositoryImpl : Repository {
    override fun getMovieFromServer(): Movie {
        return Movie()
    }

    override fun getMoviesLocalList(): List<Movie> {
        return getMoviesList()
    }
}