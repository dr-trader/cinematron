package ru.gb.androidone.donspb.cinematron.room

import androidx.room.*
import ru.gb.androidone.donspb.cinematron.room.RecentMovies

@Dao
interface RecentDao
{

    @Query("SELECT * FROM RecentMovies")
    fun getAll(): List<RecentMovies>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entity: RecentMovies)

    @Update
    fun update(entity: RecentMovies)

    @Delete
    fun delete(entity: RecentMovies)
}