package test.hackernews.data.api

import retrofit2.Call
import retrofit2.http.GET
import test.hackernews.model.Data

interface HackerNewsApi {
    @GET("search_by_date?query=android")
    fun getPosts() : Call<Data>
}