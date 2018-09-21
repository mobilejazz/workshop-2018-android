package com.mobilejazz.workshop2018.di

import android.app.Application
import android.content.Context
import com.mobilejazz.workshop2018.App
import com.mobilejazz.workshop2018.di.general.*
import com.mobilejazz.workshop2018.di.ui.ActivityBindingsModule
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Module(includes = [
  (AndroidSupportInjectionModule::class),
  NetworkingModule::class,
  AndroidModule::class,
  ItemDI::class,
  InteractorsModule::class,
  ItemIdsDI::class])
internal abstract class AppModule {

  @Binds
  @Singleton
  internal abstract fun application(app: App): Application

  @Binds
  @Singleton
  internal abstract fun context(app: App): Context

}

@Singleton
@Component(modules = [(AppModule::class), (ActivityBindingsModule::class)])
internal interface AppComponent : AndroidInjector<App> {

  @Component.Builder
  abstract class Builder : AndroidInjector.Builder<App>()
}