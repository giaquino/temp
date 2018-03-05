package com.itshere.di

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.itshere.di.viewmodel.ViewModelModule
import com.itshere.model.db.ItsHereDatabase
import com.itshere.network.RateLimiter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = arrayOf(ViewModelModule::class))
class ApplicationModule {

    @Singleton @Provides
    fun provideDatabase(application: Application): ItsHereDatabase {
        return Room.databaseBuilder(application, ItsHereDatabase::class.java,
                "itshere-provideDatabase").build()
    }

    @Singleton @Provides
    fun provideBroadcastDao(db: ItsHereDatabase) = db.broadcastDao()

    @Singleton @Provides
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Singleton @Provides
    fun provideFirebaseFirestore() = FirebaseFirestore.getInstance()

    @Provides @Singleton
    fun provideRateLimiter(application: Application): RateLimiter {
        return RateLimiter(1000L * 60L * 5L,
                application.getSharedPreferences("com.itshere.ratelimiter.pref",
                        Context.MODE_PRIVATE))
    }
}