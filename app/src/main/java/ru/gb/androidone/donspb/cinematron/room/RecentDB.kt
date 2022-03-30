package ru.gb.androidone.donspb.cinematron.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RecentMovies::class], version = 1, exportSchema = false)
abstract class RecentDB : RoomDatabase() {

    abstract fun recentDao(): RecentDao
}