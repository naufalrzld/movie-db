package com.example.moviedb.ui.detail

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.moviedb.R
import com.example.moviedb.core.domain.model.MovieData
import com.example.moviedb.core.env.IEnvironment
import com.example.moviedb.core.utils.gone
import com.example.moviedb.core.utils.visible
import com.example.moviedb.databinding.ActivityDetailBinding
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private val viewModel: DetailViewModel by viewModel()
    private val env: IEnvironment by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.detail_movie)
        }

        initObserver()

        val id = intent.getIntExtra(EXTRA_MOVIE_ID, 0)
        if (id != 0) viewModel.getDetailMovie(id)
    }

    private fun initObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.uiState.collect { uiState ->
                    when (uiState) {
                        is DetailUIState.Loading -> {
                            binding.progressBar.visible()
                            binding.groupContent.gone()
                        }
                        is DetailUIState.Loaded -> {
                            binding.progressBar.gone()
                            binding.lytError.gone()
                            binding.groupContent.visible()
                            loadData(uiState.data)
                        }
                        is DetailUIState.OnError -> {
                            binding.groupContent.gone()
                            binding.progressBar.gone()
                            binding.lytError.visible()
                            binding.tvMessage.text = uiState.message
                        }
                    }
                }
            }
        }
    }

    private fun loadData(data: MovieData?) {
        binding.tvTitle.text = data?.title
        binding.tvReleaseDate.text = data?.releaseDate
        binding.tvCategory.text = data?.genres
        binding.tvOverview.text = data?.overview

        Glide.with(this)
            .load(env.getBaseImageUrl() + data?.posterPath)
            .apply(RequestOptions.placeholderOf(R.drawable.ic_loading).error(R.drawable.ic_error))
            .into(binding.ivPoster)

        binding.imgShare.setOnClickListener {
            val mimeType = "text/plain"
            ShareCompat.IntentBuilder.from(this).apply {
                setType(mimeType)
                setChooserTitle("Bagikan aplikasi ini sekarang.")
                setText(resources.getString(R.string.share_movie_text, data?.title))
                startChooser()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val EXTRA_MOVIE_ID = "extra_movie_id"
    }
}