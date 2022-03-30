package ru.gb.androidone.donspb.cinematron.room

import androidx.room.*

@Dao
interface RecentDao
{

    @Query("SELECT * FROM RecentMovies ORDER BY datetime DESC")
    fun getAll(): List<RecentMovies>

    @Query("DELETE FROM RecentMovies WHERE id NOT IN (SELECT id FROM RecentMovies ORDER BY datetime DESC LIMIT :limit)")
    fun delOverlimit(limit: Int)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entity: RecentMovies)

    @Update
    fun update(entity: RecentMovies)

    @Delete
    fun delete(entity: RecentMovies)
}