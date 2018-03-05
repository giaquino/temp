package com.itshere.model.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import java.util.UUID

@Entity(tableName = Account.TABLE_NAME)
data class Account(
        @PrimaryKey @ColumnInfo(name = KEY_UUID) val uuid: String,
        @ColumnInfo(name = KEY_NAME) val name: String,
        @ColumnInfo(name = KEY_ACCOUNT_BROADCAST_ID) val accountBroadcastId: String,
        @ColumnInfo(name = KEY_UPDATED_AT) val updatedAt: String = "") {

    companion object {
        const val TABLE_NAME = "accounts"
        const val KEY_UUID = "uuid"
        const val KEY_NAME = "name"
        const val KEY_ACCOUNT_BROADCAST_ID = "account_broadcast_id"
        const val KEY_UPDATED_AT = "updated_at"

        fun create(user: FirebaseUser, doc: DocumentSnapshot): Account {
            return Account(
                    uuid = user.uid,
                    name = user.displayName!!,
                    accountBroadcastId = doc.getString(KEY_ACCOUNT_BROADCAST_ID),
                    updatedAt = doc.getString(KEY_UPDATED_AT))
        }

        fun create(user: FirebaseUser): Account {
            return Account(
                    uuid = user.uid,
                    name = user.displayName!!,
                    accountBroadcastId = UUID.randomUUID().toString())
        }
    }

    fun toFirestoreMap(): Map<String, Any> = mutableMapOf<String, Any>().apply {
        put(KEY_UUID, uuid)
        put(KEY_NAME, name)
        put(KEY_ACCOUNT_BROADCAST_ID, accountBroadcastId)
        put(KEY_UPDATED_AT, FieldValue.serverTimestamp())
    }
}