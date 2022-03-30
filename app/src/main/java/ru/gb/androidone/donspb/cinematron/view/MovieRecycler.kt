package ru.gb.androidone.donspb.cinematron.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.progressindicator.CircularProgressIndicator
import ru.gb.androidone.donspb.cinematron.R
import ru.gb.androidone.donspb.cinematron.model.MovieListItem
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import ru.gb.androidone.donspb.cinematron.Consts


class MovieRecycler(private var onItemViewClickListener: MainFragment.OnItemViewClickListener?) : RecyclerView.Adapter<MovieRecycler.ViewHolder>() {

    private val movieData: MutableList<MovieListItem> = mutableListOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setMovie(data: List<MovieListItem>?) {
        if (data != null) {
            movieData.addAll(data)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.recycler_item, parent, false) as View
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(movieData[position])
    }

    override fun getItemCount() = movieData.size

    fun removeListener() {
        onItemViewClickListener = null
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(movie: MovieListItem) {
            itemView.apply {
                findViewById<ImageView>(R.id.rv_item_image)
                    .load(Consts.BASE_IMAGE_URL + movie.poster_path)
                findViewById<TextView>(R.id.rv_item_title).text = movie.title
                val formatter = DateTimeFormatter.ofPattern("dd LLL yyyy")
                findViewById<TextView>(R.id.rv_item_year).text =
                    LocalDate.parse(movie.release_date).format(formatter).toString()
                val movieRating = (movie.vote_average * 10).toInt()

                findViewById<CircularProgressIndicator>(R.id.progress_bar_rating).progress =
                    movieRating
                findViewById<CircularProgressIndicator>(R.id.progress_bar_rating)
                    .setIndicatorColor(colorMe(movieRating))
                findViewById<TextView>(R.id.tv_rating).text = movieRating.toString()
                setOnClickListener { onItemViewClickListener?.onItemViewClick(movie) }
            }
        }

        private fun colorMe(rating: Int) = when {
            rating < Consts.RATING_RED_MAX ->
                ContextCompat.getColor(itemView.context, R.color.progress_bad)
            rating < Consts.RATING_YEL_MAX ->
                ContextCompat.getColor(itemView.context, R.color.progress_notbad)
            rating < Consts.RATING_LGR_MAX ->
                ContextCompat.getColor(itemView.context, R.color.progress_good)
            else -> ContextCompat.getColor(itemView.context, R.color.progress_excellent)
        }
    }
}

