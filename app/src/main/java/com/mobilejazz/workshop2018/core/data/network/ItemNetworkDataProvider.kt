package com.mobilejazz.workshop2018.core.data.network

import com.mobilejazz.kotlin.core.repository.datasource.GetDataSource
import com.mobilejazz.kotlin.core.repository.query.IntegerIdQuery
import com.mobilejazz.kotlin.core.repository.query.IntegerIdsQuery
import com.mobilejazz.kotlin.core.repository.query.Query
import com.mobilejazz.kotlin.core.threading.extensions.Future
import com.mobilejazz.workshop2018.core.data.model.ItemEntity
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ItemNetworkDataProvider @Inject constructor(private val hackerNewsItemService: HackerNewsItemService) : GetDataSource<ItemEntity> {

  override fun get(query: Query): Future<ItemEntity> = when (query) {
    is IntegerIdQuery -> {
      Future {
        val item = hackerNewsItemService.newItem(query.identifier).get()
        item.lastUpdate = Date()
        item.expiryTime = TimeUnit.MINUTES.toMillis(5)

        return@Future item

      }

    }
    else -> throw IllegalArgumentException("Query not mapped correctly!")
  }


  override fun getAll(query: Query): Future<List<ItemEntity>> = when (query) {
    is IntegerIdsQuery -> {
      Future {
        return@Future query.identifiers.map {
          get(IntegerIdQuery(it)).get()
        }
      }
    }
    else -> throw IllegalArgumentException("Query not mapped correctly!")
  }

}