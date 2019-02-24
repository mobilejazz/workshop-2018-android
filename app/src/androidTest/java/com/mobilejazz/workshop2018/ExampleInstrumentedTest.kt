package com.mobilejazz.workshop2018

import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.mobilejazz.kotlin.core.repository.datasource.srpreferences.DeviceStorageDataSource
import com.mobilejazz.kotlin.core.repository.datasource.srpreferences.DeviceStorageObjectAssemblerDataSource
import com.mobilejazz.kotlin.core.repository.mapper.ListModelToStringMapper
import com.mobilejazz.kotlin.core.repository.mapper.ModelToStringMapper
import com.mobilejazz.kotlin.core.repository.mapper.StringToListModelMapper
import com.mobilejazz.kotlin.core.repository.mapper.StringToModelMapper
import com.mobilejazz.kotlin.core.repository.query.IdQuery
import com.mobilejazz.kotlin.core.repository.query.KeyQuery
import com.mobilejazz.workshop2018.core.data.model.ItemEntity
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
  @Test
  fun useAppContext() {
    // Context of the app under test.
    val appContext = InstrumentationRegistry.getTargetContext()
    assertEquals("com.mobilejazz.workshop2018", appContext.packageName)
  }

  @Test
  fun store_string() {
    val appContext = InstrumentationRegistry.getTargetContext()
    val sharedPreferences = appContext.applicationContext.getSharedPreferences("test", Context.MODE_PRIVATE)
    val deviceStorageDataSource = DeviceStorageDataSource<String>(sharedPreferences)

    deviceStorageDataSource.put(IdQuery(123), "jose-123").get()
    val value = deviceStorageDataSource.get(IdQuery(123)).get()

    assertEquals(value, "jose-123")
  }

  @Test
  fun store_int() {
    val appContext = InstrumentationRegistry.getTargetContext()
    val sharedPreferences = appContext.applicationContext.getSharedPreferences("test", Context.MODE_PRIVATE)
    val deviceStorageDataSource = DeviceStorageDataSource<Int>(sharedPreferences, "Int")

    deviceStorageDataSource.put(KeyQuery("123"), 123).get()
    val value = deviceStorageDataSource.get(KeyQuery("123")).get()

    assertEquals(value, 123)
  }

  @Test
  fun store_set() {
    val appContext = InstrumentationRegistry.getTargetContext()
    val sharedPreferences = appContext.applicationContext.getSharedPreferences("test", Context.MODE_PRIVATE)
    val deviceStorageDataSource = DeviceStorageDataSource<Set<String>>(sharedPreferences, "Int")

    deviceStorageDataSource.put(KeyQuery("123"), setOf("1", "2", "3")).get()
    val value = deviceStorageDataSource.get(KeyQuery("123")).get()

    assertEquals(value, setOf("1", "2", "3"))
  }

  @Test
  fun store_object() {
    val gson = Gson()
    val toStringMapper = ModelToStringMapper<ItemEntity>(gson)
    val toModelMapper = StringToModelMapper(ItemEntity::class.java, gson)
    val toListModelMapper = ListModelToStringMapper<ItemEntity>(gson)
    val toStringListMapper = StringToListModelMapper( object : TypeToken<List<ItemEntity>>() {}, gson)

    val appContext = InstrumentationRegistry.getTargetContext()
    val sharedPreferences = appContext.applicationContext.getSharedPreferences("test", Context.MODE_PRIVATE)

    val deviceStorageDataSource = DeviceStorageDataSource<String>(sharedPreferences)
    val itemEntityDeviceStorageDataSource = DeviceStorageObjectAssemblerDataSource(toStringMapper, toModelMapper, toListModelMapper, toStringListMapper,
        deviceStorageDataSource)

    val itemEntity = ItemEntity(123, "by", "title", "text", "type", 123, "url", emptyList())
    itemEntityDeviceStorageDataSource.put(KeyQuery("1"), itemEntity).get()
    val result = itemEntityDeviceStorageDataSource.get(KeyQuery("1")).get()

    assertEquals(itemEntity.id, result.id)
  }

  @Test
  fun store_list_object() {
    val gson = Gson()
    val toStringMapper = ModelToStringMapper<ItemEntity>(gson)
    val toModelMapper = StringToModelMapper(ItemEntity::class.java, gson)
    val toListModelMapper = ListModelToStringMapper<ItemEntity>(gson)
    val toStringListMapper = StringToListModelMapper( object : TypeToken<List<ItemEntity>>() {}, gson)

    val appContext = InstrumentationRegistry.getTargetContext()
    val sharedPreferences = appContext.applicationContext.getSharedPreferences("test", Context.MODE_PRIVATE)

    val deviceStorageDataSource = DeviceStorageDataSource<String>(sharedPreferences)
    val itemEntityDeviceStorageDataSource = DeviceStorageObjectAssemblerDataSource(toStringMapper, toModelMapper, toListModelMapper, toStringListMapper,
        deviceStorageDataSource)

    val itemEntity = ItemEntity(123, "by", "title", "text", "type", 123, "url", emptyList())
    val values = listOf(itemEntity, itemEntity)
    itemEntityDeviceStorageDataSource.putAll(KeyQuery("1"), values).get()
    val result = itemEntityDeviceStorageDataSource.getAll(KeyQuery("1")).get()

    assertEquals(values.size, result.size)
    assertEquals(values[0].id, result[0].id)
  }
}
