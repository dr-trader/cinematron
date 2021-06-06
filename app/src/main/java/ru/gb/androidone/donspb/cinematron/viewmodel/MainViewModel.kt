package ru.gb.androidone.donspb.cinematron.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.gb.androidone.donspb.cinematron.repository.MainRepoImpl
import java.lang.Thread.sleep

class MainViewModel(
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData(),
    private val repositoryImpl: MainRepoImpl = MainRepoImpl()
) :
    ViewModel() {

    fun getLiveData() = liveDataToObserve

    fun getMovieFromLocalSource() = getDataFromLocalSource()

    fun getMovieFromRemouteSource() = getDataFromLocalSource()

    private fun getDataFromLocalSource() {
        liveDataToObserve.value = AppState.Loading
        Thread {
            sleep(1000)
            liveDataToObserve.postValue(
                    AppState.Starting(repositoryImpl.getMoviesLocalList(),
                            repositoryImpl.getMoviesLocalList(),
                            repositoryImpl.getMoviesLocalList())
            )
        }.start()
    }
}