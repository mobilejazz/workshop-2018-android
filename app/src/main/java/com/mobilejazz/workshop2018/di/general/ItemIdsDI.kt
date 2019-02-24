package com.mobilejazz.workshop2018.di.general

import android.content.SharedPreferences
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.mobilejazz.kotlin.core.repository.GetRepository
import com.mobilejazz.kotlin.core.repository.NetworkStorageRepository
import com.mobilejazz.kotlin.core.repository.RepositoryMapper
import com.mobilejazz.kotlin.core.repository.datasource.VoidDeleteDataSource
import com.mobilejazz.kotlin.core.repository.datasource.VoidPutDataSource
import com.mobilejazz.kotlin.core.repository.datasource.srpreferences.DeviceStorageDataSource
import com.mobilejazz.kotlin.core.repository.datasource.srpreferences.DeviceStorageObjectAssemblerDataSource
import com.mobilejazz.kotlin.core.repository.mapper.ListModelToStringMapper
import com.mobilejazz.kotlin.core.repository.mapper.ModelToStringMapper
import com.mobilejazz.kotlin.core.repository.mapper.StringToListModelMapper
import com.mobilejazz.kotlin.core.repository.mapper.StringToModelMapper
import com.mobilejazz.workshop2018.core.data.mapper.ItemIdsEntityToItemIdsMapper
import com.mobilejazz.workshop2018.core.data.mapper.ItemIdsToItemIdsEntityMapper
import com.mobilejazz.workshop2018.core.data.model.ItemIdsEntity
import com.mobilejazz.workshop2018.core.data.network.HackerNewsItemService
import com.mobilejazz.workshop2018.core.data.network.ItemIdsNetworkDataSource
import com.mobilejazz.workshop2018.core.domain.model.ItemIds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ItemIdsDI {

  @Provides
  @Singleton
  fun provideGetRepository(sharedPreferences: SharedPreferences, hackerNewsItemService: HackerNewsItemService): GetRepository<ItemIds> {
    // We need to create the DeviceStorageDataSource to store all data in the Android SharedPreferences
    val deviceStorageDataSource = DeviceStorageDataSource<String>(sharedPreferences)

    // As DeviceStorageDataSource doesn't support store model objects, we need to use the DeviceStorageObjectAssemblerDataSource to be able to serialize the
    // object into a json string
    val gson = Gson()
    val toStringMapper = ModelToStringMapper<ItemIdsEntity>(gson)
    val toModelMapper = StringToModelMapper(ItemIdsEntity::class.java, gson)
    val toListModelMapper = ListModelToStringMapper<ItemIdsEntity>(gson)
    val toStringListMapper = StringToListModelMapper(object : TypeToken<List<ItemIdsEntity>>() {}, gson)

    val deviceStorageObjectAssemblerDataSource = DeviceStorageObjectAssemblerDataSource(toStringMapper, toModelMapper, toListModelMapper, toStringListMapper, deviceStorageDataSource)

    // Now, we need to get a network dataSource to fetch the item ids
    val itemIdsNetworkDataSource = ItemIdsNetworkDataSource(hackerNewsItemService)

    // We want to establish a simple caching system, so we are going to use the NetworkStorageRepository to automatically have this feature.
    val networkStorageRepository = NetworkStorageRepository(deviceStorageObjectAssemblerDataSource, deviceStorageObjectAssemblerDataSource, deviceStorageObjectAssemblerDataSource,
        itemIdsNetworkDataSource, VoidPutDataSource(), VoidDeleteDataSource())

    // As our business model only understand the ItemIds model instead of the ItemIdsEntity model, we need to use the RepositoryMapper to map the different
    // types
    return RepositoryMapper(networkStorageRepository, networkStorageRepository, networkStorageRepository, ItemIdsEntityToItemIdsMapper(),
        ItemIdsToItemIdsEntityMapper())
  }


//  @Provides
//  @Singleton
//  fun provideGetRepository(singleDataSourceRepository: SingleDataSourceRepository<Int>): GetRepository<Int> = singleDataSourceRepository
}