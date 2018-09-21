package com.mobilejazz.workshop2018.core.domain.interactor

import com.mobilejazz.kotlin.core.domain.interactor.GetInteractor
import com.mobilejazz.kotlin.core.repository.operation.StorageSyncOperation
import com.mobilejazz.kotlin.core.repository.query.ByIdentifierIntegerQuery
import com.mobilejazz.kotlin.core.threading.DirectExecutor
import com.mobilejazz.kotlin.core.threading.Executor
import com.mobilejazz.kotlin.core.threading.extensions.Future
import com.mobilejazz.workshop2018.core.domain.model.Item
import java.util.concurrent.Callable
import javax.inject.Inject

class GetItemsByIdInteractor @Inject constructor(private val executor: Executor,
                                                 private val getItemInteractor: GetInteractor<Item>) {

  operator fun invoke(ids: List<Int>, executor: Executor = this.executor): Future<List<Item>> {
    return executor.submit(Callable {
      return@Callable ids.map {
        getItemInteractor(ByIdentifierIntegerQuery(it), operation = StorageSyncOperation, executor = DirectExecutor).get()
      }
    })
  }
}