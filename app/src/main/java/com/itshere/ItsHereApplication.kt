package com.itshere

import android.app.Activity
import android.app.Application
import android.os.StrictMode
import com.itshere.di.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import timber.log.Timber
import timber.log.Timber.DebugTree
import javax.inject.Inject

class ItsHereApplication : Application(), HasActivityInjector {

    @Inject lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector(): AndroidInjector<Activity> = dispatchingAndroidInjector

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
            StrictMode.enableDefaults()
        }
        DaggerApplicationComponent.builder().application(this).build().inject(this)
    }
}
