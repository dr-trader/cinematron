package ru.gb.androidone.donspb.cinematron.viewmodel

import android.os.Handler
import android.os.HandlerThread
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.gb.androidone.donspb.cinematron.App.Companion.getRecentDao
import ru.gb.androidone.donspb.cinematron.model.MovieList
import ru.gb.androidone.donspb.cinematron.model.MovieListItem
import ru.gb.androidone.donspb.cinematron.repository.LocalRepo
import ru.gb.androidone.donspb.cinematron.repository.LocalRepoImpl

class LocalViewModel(
    val recentLiveData: MutableLiveData<AppState> = MutableLiveData(),
    private val recentRepo: LocalRepo = LocalRepoImpl(getRecentDao()),
) : ViewModel() {

    val handlerThread = HandlerThread("LocalDBThread")
    val dbHandler by lazy {
        Handler(handlerThread.looper)
    }

    init {
        handlerThread.start()
    }

//    fun getRecentMovies() {
//        recentLiveData.value = AppState.Loading
//        dbHandler.post {
//            recentLiveData.postValue(AppState.Starting(MovieList(recentRepo.getLocalList())))
//        }
//    }
//
//    fun saveMovieToDB(movieData: MovieListItem) {
//        dbHandler.post {
//            recentRepo.saveEntity(movieData)
//        }
//    }
}