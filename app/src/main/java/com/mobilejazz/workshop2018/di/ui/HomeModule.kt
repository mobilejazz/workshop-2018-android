package com.mobilejazz.workshop2018.di.ui

import com.mobilejazz.kotlin.core.di.ActivityScope
import com.mobilejazz.kotlin.core.domain.interactor.GetAllInteractor
import com.mobilejazz.kotlin.core.repository.GetRepository
import com.mobilejazz.kotlin.core.threading.Executor
import dagger.Module
import dagger.Provides

@Module
class HomeModule {

  @Provides
  @ActivityScope
  fun provideGetAllStories(executor: Executor, getRepository: GetRepository<Int>): GetAllInteractor<Int> {
    return GetAllInteractor(executor, getRepository)
  }
}