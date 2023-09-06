package com.example.moviedb.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class BaseResponseApi<T>(
    @SerializedName("page")
    val page: Int,

    @SerializedName("results")
    val results: List<T>,

    @SerializedName("total_pages")
    val totalPages: Int,

    @SerializedName("total_results")
    val totalResults: Int
)