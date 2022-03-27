package ru.gb.androidone.donspb.cinematron.viewmodel

import android.content.res.Resources
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.gb.androidone.donspb.cinematron.App
import ru.gb.androidone.donspb.cinematron.R
import ru.gb.androidone.donspb.cinematron.model.MovieList
import ru.gb.androidone.donspb.cinematron.model.MovieListItem
import ru.gb.androidone.donspb.cinematron.repository.LocalRepo
import ru.gb.androidone.donspb.cinematron.repository.LocalRepoImpl
import ru.gb.androidone.donspb.cinematron.repository.MainRepoImpl
import ru.gb.androidone.donspb.cinematron.repository.RemoteDataSource

class MainViewModel(
    val movieListDataNow: MutableLiveData<AppState> = MutableLiveData(),
    val movieListDataPop: MutableLiveData<AppState> = MutableLiveData(),
    val movieListDataTop: MutableLiveData<AppState> = MutableLiveData(),
    val movieListDataUp: MutableLiveData<AppState> = MutableLiveData(),
    private val repositoryImpl: MainRepoImpl = MainRepoImpl(RemoteDataSource())
//    private val recentRepository: LocalRepo = LocalRepoImpl(App.getRecentDao())
) : ViewModel() {

    fun getMovieListFromRemote(listType: MovieListsEnum) {
        when (listType.listNameId) {
            R.string.now_playing_list -> {
                movieListDataNow.value = AppState.Loading
                repositoryImpl.getMovieListFromServer(listType.pathPart, CallBack(listType.listNameId), pageNumber = 1)
            }
            R.string.popular_list -> {
                movieListDataPop.value = AppState.Loading
                repositoryImpl.getMovieListFromServer(listType.pathPart, CallBack(listType.listNameId), pageNumber = 1)
            }
            R.string.top_rated_list -> {
                movieListDataTop.value = AppState.Loading
                repositoryImpl.getMovieListFromServer(listType.pathPart, CallBack(listType.listNameId), pageNumber = 1)
            }
            R.string.upcoming_list -> {
                movieListDataUp.value = AppState.Loading
                repositoryImpl.getMovieListFromServer(listType.pathPart, CallBack(listType.listNameId), pageNumber = 1)
            }
        }
    }

    inner class CallBack(val listType: Int) : Callback<MovieList> {

        override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
            val serverResponse: MovieList? = response.body()
            val valueToPost =
                if (response.isSuccessful && serverResponse != null) {
                    AppState.Starting(serverResponse)
                } else {
                    AppState.Error(Throwable(R.string.server_error.toString()))
                }
            when (listType) {
                R.string.now_playing_list -> {
                    movieListDataNow.postValue(valueToPost)
                }
                R.string.top_rated_list -> {
                    movieListDataTop.postValue(valueToPost)
                }
                R.string.popular_list -> {
                    movieListDataPop.postValue(valueToPost)
                }
                R.string.upcoming_list -> {
                    movieListDataUp.postValue(valueToPost)
                }
            }
        }

        override fun onFailure(call: Call<MovieList>, t: Throwable) {
            val valueToPost = AppState.Error(
                Throwable(t.message ?: R.string.request_error.toString()))
            when (listType) {
                R.string.now_playing_list -> {
                    movieListDataNow.postValue(valueToPost)
                }
                R.string.top_rated_list -> {
                    movieListDataTop.postValue(valueToPost)
                }
                R.string.popular_list -> {
                    movieListDataPop.postValue(valueToPost)
                }
                R.string.upcoming_list -> {
                    movieListDataUp.postValue(valueToPost)
                }
            }
        }
    }
}