package ru.gb.androidone.donspb.cinematron.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import ru.gb.androidone.donspb.cinematron.R
import ru.gb.androidone.donspb.cinematron.model.MovieListItem
import java.time.LocalDate

class MovieRecycler(private var onItemViewClickListener: MainFragment.OnItemViewClickListener?) : RecyclerView.Adapter<MovieRecycler.ViewHolder>() {

    private var movieData: List<MovieListItem> = listOf()

    fun setMovie(data: List<MovieListItem>?) {
        if (data != null) {
            movieData = data
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
                    .load("https://image.tmdb.org/t/p/original/${movie.poster_path}")
                findViewById<TextView>(R.id.rv_item_title).text = movie.title
                val date = LocalDate.parse(movie.release_date)
                findViewById<TextView>(R.id.rv_item_year).text = "(${date.year})"

                setOnClickListener { onItemViewClickListener?.onItemViewClick(movie) }
            }
        }
    }
}

