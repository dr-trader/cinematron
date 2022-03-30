package ru.gb.androidone.donspb.cinematron.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.gb.androidone.donspb.cinematron.Consts
import ru.gb.androidone.donspb.cinematron.model.MovieList
import ru.gb.androidone.donspb.cinematron.repository.MainRepoImpl
import ru.gb.androidone.donspb.cinematron.repository.RemoteDataSource

class MainViewModel(
    private val repositoryImpl: MainRepoImpl = MainRepoImpl(RemoteDataSource())
) : ViewModel() {

    private var nextPage: Int? = null
    private var totalPages: Int = Consts.FIRST_PAGE_INDEX
    val movieListData: MutableLiveData<AppState> = MutableLiveData()

    fun clear() {
        nextPage = null
        totalPages = Consts.FIRST_PAGE_INDEX
    }

    fun getMovieListFromRemote(listName: String) {
        movieListData.value = AppState.Loading
        if (nextPage == null) nextPage = Consts.FIRST_PAGE_INDEX
        else nextPage = nextPage!! + 1

        if (nextPage!! <= totalPages)
            repositoryImpl.getMovieListFromServer(listName, CallBack(), nextPage!!)
    }

    inner class CallBack : Callback<MovieList> {

        override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
            val serverResponse: MovieList? = response.body()
            val valueToPost =
                if (response.isSuccessful && serverResponse != null) {
                    totalPages = serverResponse.total_pages
                    AppState.Starting(serverResponse)
                } else {
                    AppState.Error(Throwable(response.body().toString()))
                }
            movieListData.postValue(valueToPost)
            }

        override fun onFailure(call: Call<MovieList>, t: Throwable) {
            val valueToPost = AppState.Error(Throwable(t.message))
            movieListData.postValue(valueToPost)
        }
    }
}