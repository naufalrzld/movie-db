package com.example.moviedb.core.env

interface IEnvironment {
    fun getBaseUrl(): String
    fun getAPIKey(): String
    fun getBaseImageUrl(): String
}