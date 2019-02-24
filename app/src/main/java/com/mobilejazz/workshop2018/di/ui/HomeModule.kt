package com.mobilejazz.workshop2018.di.ui

import com.mobilejazz.kotlin.core.di.ActivityScope
import com.mobilejazz.kotlin.core.domain.interactor.GetInteractor
import com.mobilejazz.kotlin.core.repository.GetRepository
import com.mobilejazz.kotlin.core.threading.Executor
import com.mobilejazz.workshop2018.core.domain.model.ItemIds
import dagger.Module
import dagger.Provides

@Module
class HomeModule {

  @Provides
  @ActivityScope
  fun provideGetAllStories(executor: Executor, getRepository: GetRepository<ItemIds>): GetInteractor<ItemIds> {
    return GetInteractor(executor, getRepository)
  }
}