package com.mobilejazz.workshop2018.network

import com.mobilejazz.workshop2018.model.Item
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path


interface HackerNewsItemService {

  @GET("askstories.json")
  fun askStories(): Call<List<Int>>

  @GET("item/{id}.json")
  fun newItem(@Path("id") id: Int): Call<Item>
}


object Network {

  private val retrofit by lazy {
    val clientBuilder = OkHttpClient.Builder()

    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
    clientBuilder.addInterceptor(httpLoggingInterceptor)


    Retrofit.Builder()
        .baseUrl("https://hacker-news.firebaseio.com/v0/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(clientBuilder.build())
        .build()
  }

  private val newsService by lazy {
    retrofit.create(HackerNewsItemService::class.java)
  }

  fun askstoriesItemsIds(s: (List<Int>) -> Unit, f: (Throwable) -> Unit) {
    newsService.askStories().enqueue(object : Callback<List<Int>> {
      override fun onFailure(call: Call<List<Int>>?, t: Throwable?) {
        t?.let { f(it) }
      }

      override fun onResponse(call: Call<List<Int>>?, response: Response<List<Int>>?) {
        response?.body()?.let { s(it) }
      }

    })
  }

  fun itemById(id: Int, success: (Item) -> Unit, failure: (Throwable) -> Unit) {
    newsService.newItem(id).enqueue(object : Callback<Item> {
      override fun onFailure(call: Call<Item>?, t: Throwable?) {
        t?.let { failure(it) }
      }

      override fun onResponse(call: Call<Item>?, response: Response<Item>?) {
        response?.body()?.let { success(it) }
      }
    })
  }
}