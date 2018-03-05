package com.itshere.common.permission

import android.app.Fragment
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v13.app.FragmentCompat
import com.itshere.common.util.checkNotNull
import com.itshere.common.permission.PermissionFragment.Result.DENIED
import com.itshere.common.permission.PermissionFragment.Result.GRANTED
import com.itshere.common.permission.PermissionFragment.Result.SHOW_PERMISSION_RATIONALE
import io.reactivex.subjects.PublishSubject

class PermissionFragment : Fragment() {

    enum class Result {
        DENIED, GRANTED, SHOW_PERMISSION_RATIONALE
    }

    companion object {
        const val TAG = "permission_fragment"
        private const val REQUEST_CODE = 14
        private const val EXTRA_PERMISSION = "extra_permission"
        fun create(permission: String) = PermissionFragment().apply {
            arguments = Bundle().apply {
                putString(EXTRA_PERMISSION, permission)
            }
        }
    }

    val result: PublishSubject<Result> by lazy { PublishSubject.create<Result>() }

    private lateinit var permission: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments checkNotNull "permission == null"
        permission = arguments.getString(EXTRA_PERMISSION)
        FragmentCompat.requestPermissions(this, arrayOf(permission), REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray) {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            result.onNext(GRANTED)
        } else if (FragmentCompat.shouldShowRequestPermissionRationale(this, permission)) {
            result.onNext(SHOW_PERMISSION_RATIONALE)
        } else {
            result.onNext(DENIED)
        }
        result.onComplete()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!result.hasComplete()) {
            result.onNext(DENIED)
            result.onComplete()
        }
    }
}