package com.example.moviedb.core.helper

import com.example.moviedb.core.env.IEnvironment

class FakeEnvironment : IEnvironment {
    override fun getBaseUrl(): String = "fake_base_url"

    override fun getAPIKey(): String = "fake_api_key"

    override fun getBaseImageUrl(): String = "fake_base_mage_url"
}