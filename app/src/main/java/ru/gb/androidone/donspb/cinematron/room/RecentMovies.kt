package ru.gb.androidone.donspb.cinematron.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class RecentMovies(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val title: String,
    val poster: String,
    val release: String,
    val datetime: String,
    val vote_avg: Float,
    val backdrop: String,
    val runtime: Int
)
