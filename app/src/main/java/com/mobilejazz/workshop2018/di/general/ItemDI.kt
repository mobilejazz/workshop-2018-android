package com.mobilejazz.workshop2018.di.general

import android.content.SharedPreferences
import com.google.gson.Gson
import com.mobilejazz.kotlin.core.repository.*
import com.mobilejazz.kotlin.core.repository.datasource.*
import com.mobilejazz.kotlin.core.repository.datasource.memory.InMemoryDataSource
import com.mobilejazz.kotlin.core.repository.datasource.srpreferences.*
import com.mobilejazz.kotlin.core.repository.mapper.VoidMapper
import com.mobilejazz.kotlin.core.repository.validator.vastra.ValidationServiceManager
import com.mobilejazz.kotlin.core.repository.validator.vastra.strategies.timestamp.TimestampValidationStrategy
import com.mobilejazz.workshop2018.core.data.mapper.ItemEntityToItemMapper
import com.mobilejazz.workshop2018.core.data.model.ItemEntity
import com.mobilejazz.workshop2018.core.data.network.ItemNetworkDataProvider
import com.mobilejazz.workshop2018.core.domain.model.Item
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class ItemDI {


  // Datasources
  // --> Network
  @Provides
  @Singleton
  fun provideGetNetworkDataProvider(dataSourceVastraValidator: DataSourceVastraValidator<ItemEntity>): GetDataSource<ItemEntity> = dataSourceVastraValidator

  @Provides
  @Singleton
  fun providePutNetworkDataProvider(dataSourceVastraValidator: DataSourceVastraValidator<ItemEntity>): PutDataSource<ItemEntity> = dataSourceVastraValidator

  @Provides
  @Singleton
  fun provideDeleteNetworkDataProvider(dataSourceVastraValidator: DataSourceVastraValidator<ItemEntity>): DeleteDataSource = dataSourceVastraValidator

  // Datasources
  // --> Storage
  @Provides
  @Singleton
  fun provideInMemoryStorage(): InMemoryDataSource<ItemEntity> = InMemoryDataSource()

  @Provides
  @Singleton
  fun provideSharedPreferencesStorage(sharedPreferences: SharedPreferences): SharedPreferencesDataSource<ItemEntity> {
    val gson = Gson()
    val toStringMapper = ModelToStringMapper<ItemEntity>(gson)
    val toModelMapper = StringToModelMapper(ItemEntity::class.java, gson)
    val toListModelMapper = ListModelToStringMapper<ItemEntity>(gson)
    val toStringListMapper = StringToListModelMapper<ItemEntity>(gson)
    return SharedPreferencesDataSource(sharedPreferences, toStringMapper, toModelMapper, toListModelMapper, toStringListMapper)
  }

  // Datasources
  // --> Validator

  @Provides
  @Singleton
  fun provideVastraValidator(sharedPreferencesDataSource: SharedPreferencesDataSource<ItemEntity>): DataSourceVastraValidator<ItemEntity> {
    val validationServiceManager = ValidationServiceManager(arrayListOf(TimestampValidationStrategy()))
    return DataSourceVastraValidator(sharedPreferencesDataSource, sharedPreferencesDataSource, sharedPreferencesDataSource, validationServiceManager)
  }

  // Repositories
  // --> Main
  @Provides
  @Singleton
  fun provideNetworkStorageRepository(dataSourceVastraValidator: DataSourceVastraValidator<ItemEntity>, ind: ItemNetworkDataProvider): NetworkStorageRepository<ItemEntity> {
    return NetworkStorageRepository(dataSourceVastraValidator, dataSourceVastraValidator, dataSourceVastraValidator, ind, VoidPutDataSource(), VoidDeleteDataSource())
  }

  @Provides
  @Singleton
  fun provideGetEntityRepository(networkStorageRepository: NetworkStorageRepository<ItemEntity>): GetRepository<ItemEntity> = networkStorageRepository

  @Provides
  @Singleton
  fun providePutEntityRepository(networkStorageRepository: NetworkStorageRepository<ItemEntity>): PutRepository<ItemEntity> = networkStorageRepository

  @Provides
  @Singleton
  @Named("delete-item-entity-repository-main")
  fun provideDeleteEntityRepository(networkStorageRepository: NetworkStorageRepository<ItemEntity>): DeleteRepository = networkStorageRepository

  // Repositories
  // ---> Mappers

  @Provides
  @Singleton
  fun provideRepositoryMapper(getRepository: GetRepository<ItemEntity>,
                              putRepository: PutRepository<ItemEntity>,
                              @Named("delete-item-entity-repository-main") deleteRepository: DeleteRepository): RepositoryMapper<ItemEntity, Item> {
    return RepositoryMapper(getRepository, putRepository, deleteRepository, ItemEntityToItemMapper(), VoidMapper())
  }

  // Repositories
  // --> Public to the domain model
  @Provides
  @Singleton
  fun provideGetRepository(repositoryMapper: RepositoryMapper<ItemEntity, Item>): GetRepository<Item> = repositoryMapper

  @Provides
  @Singleton
  fun providPutRepository(repositoryMapper: RepositoryMapper<ItemEntity, Item>): PutRepository<Item> = repositoryMapper

  @Provides
  @Singleton
  @Named("delete-item-repository-main")
  fun provideDeleteRepository(repositoryMapper: RepositoryMapper<ItemEntity, Item>): DeleteRepository = repositoryMapper

}