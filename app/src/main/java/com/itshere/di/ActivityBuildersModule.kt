package com.itshere.di

import com.itshere.ui.login.LoginActivity
import com.itshere.ui.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {

    @ActivityScope @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity

    @ActivityScope @ContributesAndroidInjector
    abstract fun contributeLoginActivity(): LoginActivity
}