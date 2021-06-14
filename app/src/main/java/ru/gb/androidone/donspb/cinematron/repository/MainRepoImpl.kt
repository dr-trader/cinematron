package ru.gb.androidone.donspb.cinematron.repository

import retrofit2.Callback
import ru.gb.androidone.donspb.cinematron.model.MovieList

class MainRepoImpl(private val remoteDataSource: RemoteDataSource) : MainRepo {

    override fun getMovieListFromServer(listType: String, callback: Callback<MovieList>) {
        remoteDataSource.getMovieList(listType, callback)
    }
}