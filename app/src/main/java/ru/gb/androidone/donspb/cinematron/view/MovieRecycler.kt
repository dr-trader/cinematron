package ru.gb.androidone.donspb.cinematron.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.gb.androidone.donspb.cinematron.R
import ru.gb.androidone.donspb.cinematron.model.Movie

class MovieRecycler(private val movieData: List<Movie>) : RecyclerView.Adapter<MovieRecycler.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView? = null
        var titleView: TextView? = null
        var yearView: TextView? = null

        init {
            imageView = itemView.findViewById(R.id.rv_item_image)
            titleView = itemView.findViewById(R.id.rv_item_title)
            yearView = itemView.findViewById(R.id.rv_item_year)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.imageView?.setImageResource(R.drawable.demo_poster)
        holder.titleView?.text = movieData[position].movieTitle
        holder.yearView?.text = "(" + movieData[position].movieYear.toString() + ")"

    }

    override fun getItemCount() = movieData.size


}

