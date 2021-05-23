package ru.gb.androidone.donspb.cinematron.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.gb.androidone.donspb.cinematron.R
import ru.gb.androidone.donspb.cinematron.model.Movie

class MovieRecycler(private var onItemViewClickListener: MainFragment.OnItemViewClickListener?) : RecyclerView.Adapter<MovieRecycler.ViewHolder>() {

    private var movieData: List<Movie> = listOf()

    fun setMovie(data: List<Movie>) {
        movieData = data
        notifyDataSetChanged()
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

        fun bind(movie: Movie) {
            itemView.findViewById<ImageView>(R.id.rv_item_image).setImageResource(R.drawable.demo_poster)
            itemView.findViewById<TextView>(R.id.rv_item_title).text =  movie.movieTitle
            itemView.findViewById<TextView>(R.id.rv_item_year).text = "(${movie.movieYear})"

            itemView.setOnClickListener { onItemViewClickListener?.onItemViewClick(movie) }
        }
    }
}

