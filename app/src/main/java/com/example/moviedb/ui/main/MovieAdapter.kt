package com.example.moviedb.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.moviedb.R
import com.example.moviedb.core.domain.model.MovieData
import com.example.moviedb.core.env.IEnvironment
import com.example.moviedb.databinding.ItemMovieBinding

class MovieAdapter(
    private val env: IEnvironment,
    private val onItemClickListener: (movieId: Int) -> Unit
) : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    var listData = listOf<MovieData>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            ItemMovieBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listData[position]
        holder.bind(data)
    }

    override fun getItemCount() = listData.size

    inner class ViewHolder(private val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: MovieData) {
            with(binding) {
                Glide.with(root)
                    .load(env.getBaseImageUrl() + data.posterPath)
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_loading).error(R.drawable.ic_error))
                    .into(ivPoster)

                tvTitle.text = data.title
                tvReleaseDate.text = data.releaseDate
                tvDesc.text = data.overview

                this.root.setOnClickListener {
                    onItemClickListener(data.id)
                }
            }
        }
    }
}