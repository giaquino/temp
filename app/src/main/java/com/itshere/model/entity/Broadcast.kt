package com.itshere.model.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue

/**
 * Model which contains needed values for identifying and broadcasting user's location.
 */
@Entity(tableName = Broadcast.TABLE_NAME)
data class Broadcast(
        /**
         * Broadcast topic identifier for FCM
         */
        @PrimaryKey @ColumnInfo(name = KEY_UUID) val uuid: String,
        /**
         * Broadcast visible name
         */
        @ColumnInfo(name = KEY_NAME) val name: String,
        /**
         * Broadcast type = `account`, `device`
         */
        @ColumnInfo(name = KEY_TYPE) val type: String,
        /**
         * Device name for displaying to user
         */
        @ColumnInfo(name = KEY_DEVICE_NAME) val deviceName: String,
        /**
         * Device identifier for matching devices
         */
        @ColumnInfo(name = KEY_DEVICE_IDENTIFIER) val deviceIdentifier: String,
        /**
         * Updated timestamp value is from firebase,
         */
        @ColumnInfo(name = KEY_UPDATED_AT) val updatedAt: String = "") {

    companion object {
        const val TABLE_NAME = "broadcasts"
        const val KEY_UUID = "uuid"
        const val KEY_NAME = "name"
        const val KEY_TYPE = "type"
        const val KEY_DEVICE_NAME = "device_name"
        const val KEY_DEVICE_IDENTIFIER = "device_identifier"
        const val KEY_UPDATED_AT = "updated_at"

        fun create(doc: DocumentSnapshot): Broadcast {
            return Broadcast(
                    doc.getString(KEY_UUID),
                    doc.getString(KEY_NAME),
                    doc.getString(KEY_TYPE),
                    doc.getString(KEY_DEVICE_NAME),
                    doc.getString(KEY_DEVICE_IDENTIFIER),
                    doc.getString(KEY_UPDATED_AT))
        }
    }

    fun toFirestoreMap(): Map<String, Any> = mutableMapOf<String, Any>().apply {
        put(KEY_UUID, uuid)
        put(KEY_NAME, name)
        put(KEY_TYPE, type)
        put(KEY_DEVICE_NAME, deviceName)
        put(KEY_DEVICE_IDENTIFIER, deviceIdentifier)
        put(KEY_UPDATED_AT, FieldValue.serverTimestamp())
    }
}

