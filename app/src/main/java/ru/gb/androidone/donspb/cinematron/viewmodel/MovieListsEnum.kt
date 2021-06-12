package ru.gb.androidone.donspb.cinematron.viewmodel

import androidx.annotation.StringRes
import ru.gb.androidone.donspb.cinematron.R

enum class MovieListsEnum(val pathPart: String, val listNameId: Int) {
    NowPlayingList("now_playing", R.string.now_playing_list),
    PopularList("popular", R.string.popular_list),
    TopRatedList("top_rated", R.string.top_rated_list),
    UpcomingList("upcoming", R.string.upcoming_list)
}