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
//    private val recentRepository: LocalRepo = LocalRepoImpl(App.getRecentDao())
) : ViewModel() {

    private var nextPage: Int? = null
    private var totalPages: Int = Consts.FIRST_PAGE_INDEX
    val movieListData: MutableLiveData<AppState> = MutableLiveData()

    fun clear() {
        nextPage = null
        totalPages = Consts.FIRST_PAGE_INDEX
    }

    fun getMovieListFromRemote(listType: String = "") {
        movieListData.value = AppState.Loading
        if (nextPage == null) nextPage = Consts.FIRST_PAGE_INDEX
        else nextPage = nextPage!! + 1

        if (nextPage!! <= totalPages!!)
            repositoryImpl.getMovieListFromServer(listType, CallBack(), nextPage!!)


//        when (listType.listNameId) {
//            R.string.now_playing_list -> {
//                movieListDataNow.value = AppState.Loading
//                repositoryImpl.getMovieListFromServer(listType.pathPart, CallBack(listType.listNameId), pageNumber = 1)
//            }
//            R.string.popular_list -> {
//                movieListDataPop.value = AppState.Loading
//                repositoryImpl.getMovieListFromServer(listType.pathPart, CallBack(listType.listNameId), pageNumber = 1)
//            }
//            R.string.top_rated_list -> {
//                movieListDataTop.value = AppState.Loading
//                repositoryImpl.getMovieListFromServer(listType.pathPart, CallBack(listType.listNameId), pageNumber = 1)
//            }
//            R.string.upcoming_list -> {
//                movieListDataUp.value = AppState.Loading
//                repositoryImpl.getMovieListFromServer(listType.pathPart, CallBack(listType.listNameId), pageNumber = 1)
//            }
//        }
    }

    inner class CallBack() : Callback<MovieList> {

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
//            when (listType) {
//                R.string.now_playing_list -> {
//                    movieListDataNow.postValue(valueToPost)
//                }
//                R.string.top_rated_list -> {
//                    movieListDataTop.postValue(valueToPost)
//                }
//                R.string.popular_list -> {
//                    movieListDataPop.postValue(valueToPost)
//                }
//                R.string.upcoming_list -> {
//                    movieListDataUp.postValue(valueToPost)
//                }
            }

        override fun onFailure(call: Call<MovieList>, t: Throwable) {
            val valueToPost = AppState.Error(Throwable(t.message))
            movieListData.postValue(valueToPost)
        }
    }

//        override fun onFailure(call: Call<MovieList>, t: Throwable) {

//            when (listType) {
//                R.string.now_playing_list -> {
//                    movieListDataNow.postValue(valueToPost)
//                }
//                R.string.top_rated_list -> {
//                    movieListDataTop.postValue(valueToPost)
//                }
//                R.string.popular_list -> {
//                    movieListDataPop.postValue(valueToPost)
//                }
//                R.string.upcoming_list -> {
//                    movieListDataUp.postValue(valueToPost)
//                }
//            }
//        }
//    }
}