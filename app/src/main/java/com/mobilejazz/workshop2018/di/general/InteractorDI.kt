package com.mobilejazz.workshop2018.di.general

import com.mobilejazz.kotlin.core.domain.interactor.GetInteractor
import com.mobilejazz.kotlin.core.threading.AppExecutor
import com.mobilejazz.kotlin.core.threading.Executor
import com.mobilejazz.workshop2018.core.domain.interactor.GetItemsByIdInteractor
import com.mobilejazz.workshop2018.core.domain.model.Item
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Singleton


@Module(subcomponents = [(InteractorsComponent::class)])
class InteractorsModule {

  @Provides
  @Singleton
  fun provideAppListeningExecutorService(): Executor = AppExecutor

  // Global Interactors
  @Provides
  @Singleton
  fun provideGetItemsByIdInteractor(executor: Executor, getItemInteractor: GetInteractor<Item>): GetItemsByIdInteractor {
    return GetItemsByIdInteractor(executor, getItemInteractor)
  }


}

@Subcomponent
interface InteractorsComponent {

  fun appExecutor(): AppExecutor

  @Subcomponent.Builder
  interface Builder {
    fun requestModule(module: InteractorsModule): Builder
    fun build(): InteractorsComponent
  }

}