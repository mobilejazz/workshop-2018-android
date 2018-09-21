package com.mobilejazz.workshop2018.di.ui

import com.mobilejazz.kotlin.core.di.ActivityScope
import com.mobilejazz.workshop2018.screens.detail.ItemDetailActivity
import com.mobilejazz.workshop2018.screens.home.HomeActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingsModule {

  @ActivityScope
  @ContributesAndroidInjector(modules = [(HomeModule::class)])
  abstract fun provisioningHomeActivityInjector(): HomeActivity

  @ActivityScope
  @ContributesAndroidInjector(modules = [(ItemDetailModule::class)])
  abstract fun provisioningItemDetailActivityInjector(): ItemDetailActivity
}