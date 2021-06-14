package ru.gb.androidone.donspb.cinematron.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.gb.androidone.donspb.cinematron.App.Companion.getRecentDao
import ru.gb.androidone.donspb.cinematron.R
import ru.gb.androidone.donspb.cinematron.model.MovieListItem
import ru.gb.androidone.donspb.cinematron.model.OneMovie
import ru.gb.androidone.donspb.cinematron.repository.*

class MovieModelView(
    val movieLiveData: MutableLiveData<AppState> = MutableLiveData(),
    private val movieRepoImpl: MovieRepo = MovieRepoImpl(RemoteDataSource())
) : ViewModel() {

    fun getMovieFromRemoteSource(movieID: Int) {
        movieLiveData.value = AppState.Loading
        movieRepoImpl.getMovieDetailsFromServer(movieID, callBack)
    }

    private val callBack = object : Callback<OneMovie> {
        override fun onFailure(call: Call<OneMovie>, t: Throwable) {
            movieLiveData.postValue(AppState.Error(Throwable(t.message ?: R.string.request_error.toString())))
        }

        override fun onResponse(call: Call<OneMovie>, response: Response<OneMovie>) {
            val serverResponse: OneMovie? =response.body()
            movieLiveData.postValue(
                if (response.isSuccessful && serverResponse != null) {
                    AppState.Success(serverResponse)
                } else {
                    AppState.Error(Throwable(R.string.server_error.toString()))
                }
            )
        }
    }
}