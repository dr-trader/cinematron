package ru.gb.androidone.donspb.cinematron.repository

import com.google.gson.GsonBuilder
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.gb.androidone.donspb.cinematron.Consts
import ru.gb.androidone.donspb.cinematron.Consts.BASE_URL
import ru.gb.androidone.donspb.cinematron.model.MovieList
import ru.gb.androidone.donspb.cinematron.model.OneMovie

class RemoteDataSource {

    private val movieApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().setLenient().create()
            )
        )
        .build().create(TmdbApi::class.java)

    private val movieListApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().setLenient().create()
            )
        )
        .build().create(TmdbListApi::class.java)

    fun getMovieDetails(movieID: Int, callback: Callback<OneMovie>) {
        movieApi.getMovie(movieID, Consts.API_KEY).enqueue(callback)
    }

    fun getMovieList(listType: String, callback: Callback<MovieList>, pageNumber: Int) {
        movieListApi.getMovieList(listType, Consts.API_KEY, pageNumber).enqueue(callback)
    }
}