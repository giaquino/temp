package com.itshere.di

import android.app.Application
import com.itshere.ItsHereApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


@Singleton
@Component(modules = arrayOf(
        ActivityBuildersModule::class,
        ApplicationModule::class,
        AndroidInjectionModule::class,
        AndroidSupportInjectionModule::class))
interface ApplicationComponent {

    fun inject(application: ItsHereApplication)

    @Component.Builder
    interface Builder {
        fun build(): ApplicationComponent
        @BindsInstance fun application(application: Application): Builder
    }
}