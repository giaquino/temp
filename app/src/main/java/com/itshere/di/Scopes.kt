package com.itshere.di

import javax.inject.Scope
import kotlin.annotation.AnnotationRetention.RUNTIME

@Scope
@Retention(RUNTIME)
annotation class UserScope

@Scope
@Retention(RUNTIME)
annotation class ActivityScope