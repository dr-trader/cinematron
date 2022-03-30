package ru.gb.androidone.donspb.cinematron.repository

import ru.gb.androidone.donspb.cinematron.model.MovieListItem
import ru.gb.androidone.donspb.cinematron.room.RecentDao
import ru.gb.androidone.donspb.cinematron.room.RecentMovies
import java.time.LocalDateTime

private const val RECENT_LIMIT = 20

class LocalRepoImpl(private val localData: RecentDao) : LocalRepo {
    override fun getLocalList(): List<MovieListItem> {
        return convertFromDB(localData.getAll())
    }

    override fun saveEntity(movieItem: MovieListItem) {
        if (localData.getAll().size >= RECENT_LIMIT) {
            localData.delOverlimit(RECENT_LIMIT - 1)
        }
        localData.insert(convertToDB(movieItem))
    }

    fun convertFromDB(entityList: List<RecentMovies>): List<MovieListItem> {
        return entityList.map {
            MovieListItem(it.title, it.id, it.release, it.vote_avg, it.poster, it.backdrop,it.runtime)
        }
    }

    fun convertToDB(movieItem: MovieListItem): RecentMovies {
        val moviePoster = movieItem.poster_path ?: ""
        val backdrop = movieItem.backdrop_path ?: ""
        return RecentMovies(movieItem.id, movieItem.title, moviePoster,
        movieItem.release_date, LocalDateTime.now().toString(), movieItem.vote_average,
            backdrop, movieItem.runtime)
    }

}