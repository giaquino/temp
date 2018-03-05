package com.itshere.model.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import android.arch.persistence.room.Transaction
import com.itshere.model.entity.Broadcast
import io.reactivex.Single

@Dao
abstract class BroadcastDao {

    @Query("SELECT * from ${Broadcast.TABLE_NAME}")
    abstract fun getBroadcasts(): Single<List<Broadcast>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun addBroadcasts(broadcasts: List<Broadcast>)

    @Query("DELETE from ${Broadcast.TABLE_NAME}")
    abstract fun deleteBroadcasts()

    @Transaction
    open fun syncBroadcasts(broadcasts: List<Broadcast>) {
        deleteBroadcasts()
        addBroadcasts(broadcasts)
    }
}