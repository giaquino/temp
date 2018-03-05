package com.itshere.common.permission

import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import com.itshere.common.permission.PermissionFragment.Result
import com.itshere.common.permission.PermissionFragment.Result.DENIED
import com.itshere.common.permission.PermissionFragment.Result.GRANTED
import io.reactivex.Single

object PermissionUtil {

    fun hasPermission(context: Context, permission: String): Boolean =
        ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

    fun requestPermission(activity: AppCompatActivity, permission: String): Single<Result> {
        if (hasPermission(activity, permission)) {
            return Single.just(GRANTED)
        }
        return PermissionFragment.create(permission).run {
            activity.fragmentManager.beginTransaction().add(this, PermissionFragment.TAG).commit()
            result.single(DENIED).doFinally {
                activity.fragmentManager.beginTransaction().remove(this).commit()
            }
        }
    }
}