package com.mobilejazz.workshop2018.di.general

import android.content.SharedPreferences
import com.google.gson.Gson
import com.mobilejazz.kotlin.core.repository.GetRepository
import com.mobilejazz.kotlin.core.repository.NetworkStorageRepository
import com.mobilejazz.kotlin.core.repository.SingleDataSourceRepository
import com.mobilejazz.kotlin.core.repository.datasource.GetDataSource
import com.mobilejazz.kotlin.core.repository.datasource.VoidDeleteDataSource
import com.mobilejazz.kotlin.core.repository.datasource.VoidPutDataSource
import com.mobilejazz.kotlin.core.repository.datasource.memory.InMemoryDataSource
import com.mobilejazz.kotlin.core.repository.datasource.srpreferences.*
import com.mobilejazz.workshop2018.core.data.network.ItemIdsNetworkDataProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ItemIdsDI {

  // Datasources
  // --> Network
  @Provides
  @Singleton
  fun provideGetNetworkDataProvider(ind: ItemIdsNetworkDataProvider): GetDataSource<Int> = ind


  // Datasources
  // --> Storage
  @Provides
  @Singleton
  fun provideInMemoryStorage(): InMemoryDataSource<Int> = InMemoryDataSource()

  @Provides
  @Singleton
  fun provideSharedPreferencesStorage(sharedPreferences: SharedPreferences): DeviceStorageDataSource<Int> {
    val gson = Gson()
    val toStringMapper = ModelToStringMapper<Int>(gson)
    val toModelMapper = StringToModelMapper(Int::class.java, gson)
    val toListModelMapper = ListModelToStringMapper<Int>(gson)
    val toStringListMapper = StringToListModelMapper<Int>(gson)
    return DeviceStorageDataSource(sharedPreferences, toStringMapper, toModelMapper, toListModelMapper, toStringListMapper)
  }

  // Repository
  // --> Main
  @Provides
  @Singleton
  fun provideSingleDataSourceRepository(getDataSource: GetDataSource<Int>):
      SingleDataSourceRepository<Int> {
    return SingleDataSourceRepository(getDataSource, VoidPutDataSource(), VoidDeleteDataSource())
  }

  @Provides
  @Singleton
  fun provideNetworkStorageRepository(sharedPreferencesDataSource: DeviceStorageDataSource<Int>, getDataSource: GetDataSource<Int>): NetworkStorageRepository<Int> {
    return NetworkStorageRepository(sharedPreferencesDataSource, sharedPreferencesDataSource, sharedPreferencesDataSource, getDataSource, VoidPutDataSource()
        , VoidDeleteDataSource())
  }

  // Repository
  // --> Public to the business model

  @Provides
  @Singleton
  fun provideGetRepository(networkStorageRepository: NetworkStorageRepository<Int>):
      GetRepository<Int> = networkStorageRepository

//  @Provides
//  @Singleton
//  fun provideGetRepository(singleDataSourceRepository: SingleDataSourceRepository<Int>): GetRepository<Int> = singleDataSourceRepository
}