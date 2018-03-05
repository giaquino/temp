package com.itshere.di.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.itshere.ui.login.LoginViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(value = LoginViewModel::class)
    abstract fun bind(vm: LoginViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelDaggerFactory): ViewModelProvider.Factory
}