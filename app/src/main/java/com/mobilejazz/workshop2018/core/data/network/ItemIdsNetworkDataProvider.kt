package com.mobilejazz.workshop2018.core.data.network

import com.mobilejazz.kotlin.core.repository.datasource.GetDataSource
import com.mobilejazz.kotlin.core.repository.query.Query
import com.mobilejazz.kotlin.core.threading.extensions.Future
import javax.inject.Inject

class ItemIdsNetworkDataProvider @Inject constructor(private val hackerNewsItemService: HackerNewsItemService) : GetDataSource<Int> {

  override fun get(query: Query): Future<Int> = throw UnsupportedOperationException("Unsupported operation")


  override fun getAll(query: Query): Future<List<Int>> {
    return hackerNewsItemService.askStories()
  }

}