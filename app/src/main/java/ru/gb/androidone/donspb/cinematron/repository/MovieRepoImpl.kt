package ru.gb.androidone.donspb.cinematron.repository

import okhttp3.Callback
import ru.gb.androidone.donspb.cinematron.model.OneMovie

class MovieRepoImpl(private val remoteDataSource: RemoteDataSource) : MovieRepo {

    override fun getMovieDetailsFromServer(movieID: Int, callback: retrofit2.Callback<OneMovie>) {
        remoteDataSource.getMovieDetails(movieID, callback)
    }

}