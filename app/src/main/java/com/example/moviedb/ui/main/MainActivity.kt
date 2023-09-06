package com.example.moviedb.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviedb.R
import com.example.moviedb.core.domain.model.MovieData
import com.example.moviedb.core.env.IEnvironment
import com.example.moviedb.core.utils.gone
import com.example.moviedb.core.utils.visible
import com.example.moviedb.databinding.ActivityMainBinding
import com.example.moviedb.ui.detail.DetailActivity
import com.example.moviedb.ui.detail.DetailActivity.Companion.EXTRA_MOVIE_ID
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var movieAdapter: MovieAdapter

    private val viewModel: MainViewModel by viewModel()
    private val env: IEnvironment by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        movieAdapter = MovieAdapter(env, ::onItemClick, ::onShareClick)

        binding.rvMovies.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = movieAdapter
        }

        initObserver()

        viewModel.getNowPlayingMovies()
    }

    private fun initObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.uiState.collect { uiState ->
                    when (uiState) {
                        is MainUIState.Loading -> binding.progressBar.visible()
                        is MainUIState.ShowMovies -> {
                            binding.progressBar.gone()
                            movieAdapter.listData = uiState.movies
                        }
                        is MainUIState.Empty -> binding.progressBar.gone()
                        is MainUIState.OnError -> {
                            binding.progressBar.gone()
                            showSnackBar(uiState.message)
                        }
                    }
                }
            }
        }
    }

    private fun onItemClick(id: Int) {
        Intent(this, DetailActivity::class.java).also { intent ->
            intent.putExtra(EXTRA_MOVIE_ID, id)
            startActivity(intent)
        }
    }

    private fun onShareClick(data: MovieData) {
        ShareCompat.IntentBuilder.from(this).apply {
            setType("text/plain")
            setChooserTitle("Bagikan aplikasi ini sekarang.")
            setText(resources.getString(R.string.share_movie_text, data.title))
            startChooser()
        }
    }

    private fun showSnackBar(message: String) {
        val snackBar = Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
        snackBar.view.setBackgroundColor(
            ContextCompat.getColor(
                this,
                com.example.moviedb.core.R.color.colorError
            )
        )
        snackBar.show()
    }
}