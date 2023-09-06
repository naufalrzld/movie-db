package com.example.moviedb.core.env

class Environment : IEnvironment {

    init {
        System.loadLibrary("core")
    }

    external override fun getBaseUrl(): String
    external override fun getAPIKey(): String
    external override fun getBaseImageUrl(): String
}