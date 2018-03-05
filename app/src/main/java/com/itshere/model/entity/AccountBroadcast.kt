import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue

@Entity(tableName = AccountBroadcast.TABLE_NAME)
data class AccountBroadcast(
        @PrimaryKey @ColumnInfo(name = KEY_BROADCAST_ID) val broadcastId: String,
        @ColumnInfo(name = KEY_BROADCAST_NAME) val broadcastName: String,
        @ColumnInfo(name = KEY_DEVICE_NAME) val deviceName: String,
        @ColumnInfo(name = KEY_DEVICE_ID) val deviceId: String,
        @ColumnInfo(name = KEY_UPDATED_AT) val updatedAt: String = "") {

    companion object {
        const val TABLE_NAME = "account_broadcasts"
        const val KEY_BROADCAST_ID = "broadcast_id"
        const val KEY_BROADCAST_NAME = "broadcast_name"
        const val KEY_DEVICE_NAME = "device_name"
        const val KEY_DEVICE_ID = "device_id"
        const val KEY_UPDATED_AT = "updated_at"

        fun create(doc: DocumentSnapshot): AccountBroadcast {
            return AccountBroadcast(
                    doc.getString(KEY_BROADCAST_ID),
                    doc.getString(KEY_BROADCAST_NAME),
                    doc.getString(KEY_DEVICE_NAME),
                    doc.getString(KEY_DEVICE_ID),
                    doc.getString(KEY_UPDATED_AT))
        }
    }

    fun toFirestoreMap(): Map<String, Any> = mutableMapOf<String, Any>().apply {
        put(KEY_BROADCAST_ID, broadcastId)
        put(KEY_BROADCAST_NAME, broadcastName)
        put(KEY_DEVICE_NAME, deviceName)
        put(KEY_DEVICE_ID, deviceId)
        put(KEY_UPDATED_AT, FieldValue.serverTimestamp())
    }
}

