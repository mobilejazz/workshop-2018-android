package com.mobilejazz.workshop2018

import com.mobilejazz.kotlin.core.domain.interactor.GetInteractor
import com.mobilejazz.kotlin.core.domain.interactor.execute
import com.mobilejazz.kotlin.core.repository.SingleDataSourceRepository
import com.mobilejazz.kotlin.core.repository.datasource.memory.InMemoryDataSource
import com.mobilejazz.kotlin.core.repository.query.StringKeyQuery
import com.mobilejazz.kotlin.core.threading.DirectExecutor
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }


  @Test
  fun test_inMemoryStorageKey() {
    val inMemoryDataSource = InMemoryDataSource<Int>()

    val keyQuery = StringKeyQuery("marc")

    inMemoryDataSource.put(keyQuery, 1234)

    val value = inMemoryDataSource.get(keyQuery)

    assertEquals(value.get(), 1234)


    var singleDataSourceRepository = SingleDataSourceRepository(inMemoryDataSource, inMemoryDataSource, inMemoryDataSource)
    var getInteractor = GetInteractor(DirectExecutor, singleDataSourceRepository)

    var ft1 = getInteractor.execute("marc")
    var ft2 = getInteractor(keyQuery)

    var expectedValue1 = ft1.get()
    var expectedValue2 = ft2.get()

    assertEquals(expectedValue1, 1234)
    assertEquals(expectedValue2, 1234)
  }


}