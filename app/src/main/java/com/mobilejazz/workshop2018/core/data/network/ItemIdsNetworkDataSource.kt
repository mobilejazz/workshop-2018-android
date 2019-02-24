package com.mobilejazz.workshop2018.core.data.network

import com.mobilejazz.kotlin.core.repository.datasource.GetDataSource
import com.mobilejazz.kotlin.core.repository.query.Query
import com.mobilejazz.kotlin.core.threading.extensions.Future
import com.mobilejazz.workshop2018.core.data.model.ItemIdsEntity
import javax.inject.Inject

class ItemIdsNetworkDataSource @Inject constructor(private val hackerNewsItemService: HackerNewsItemService) : GetDataSource<ItemIdsEntity> {

  override fun get(query: Query): Future<ItemIdsEntity> {
    return Future {
      val askStoriesIds = hackerNewsItemService.askStories().get()
      return@Future ItemIdsEntity(askStoriesIds)
    }
  }


  override fun getAll(query: Query): Future<List<ItemIdsEntity>> {
    return throw UnsupportedOperationException("Unsupported operation")
  }

}