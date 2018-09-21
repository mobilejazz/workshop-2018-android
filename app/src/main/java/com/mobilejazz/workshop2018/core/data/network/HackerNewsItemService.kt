package com.mobilejazz.workshop2018.core.data.network

import com.mobilejazz.kotlin.core.threading.extensions.Future
import com.mobilejazz.workshop2018.core.data.model.ItemEntity
import retrofit2.http.GET
import retrofit2.http.Path

interface HackerNewsItemService {

  @GET("askstories.json")
  fun askStories(): Future<List<Int>>

  @GET("item/{id}.json")
  fun newItem(@Path("id") id: Int): Future<ItemEntity>
}
