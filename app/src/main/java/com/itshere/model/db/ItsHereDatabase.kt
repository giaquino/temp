package com.itshere.model.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.itshere.model.entity.Broadcast

@Database(version = 1, entities = arrayOf(Broadcast::class))
abstract class ItsHereDatabase : RoomDatabase() {

    abstract fun broadcastDao(): BroadcastDao
}