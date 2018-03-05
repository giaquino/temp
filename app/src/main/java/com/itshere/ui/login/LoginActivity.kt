package com.itshere.ui.login

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.itshere.R
import com.itshere.common.application.BaseActivity
import com.itshere.databinding.LoginBinding
import timber.log.Timber
import javax.inject.Inject
import kotlin.LazyThreadSafetyMode.NONE

class LoginActivity : BaseActivity() {

    companion object {
        private const val REQUEST_CODE_LOGIN = 10
    }

    @Inject lateinit var auth: FirebaseAuth
    @Inject lateinit var factory: ViewModelProvider.Factory

    private val vm by lazy(NONE) {
        ViewModelProviders.of(this, factory).get(LoginViewModel::class.java)
    }

    private val binding by lazy(NONE) {
        DataBindingUtil.setContentView<LoginBinding>(this, R.layout.login)
    }

    private val client by lazy(NONE) {
        GoogleSignIn.getClient(this,
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.keys_google_web_client_id))
                        .requestProfile()
                        .requestEmail()
                        .build())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.activity = this
        binding.vm = vm
        setResult(Activity.RESULT_CANCELED)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == REQUEST_CODE_LOGIN) {
            handleLoginResult(GoogleSignIn.getSignedInAccountFromIntent(data))
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun login() {
        startActivityForResult(client.signInIntent, REQUEST_CODE_LOGIN)
    }

    fun dismiss() {
        ActivityCompat.finishAfterTransition(this)
    }

    fun handleError(exception: Throwable) {
        Timber.e(exception, "error %s", exception.message)
    }

    fun handleLoginResult(task: Task<GoogleSignInAccount>) {
        try {
            authenticate(task.getResult(ApiException::class.java))
        } catch (e: ApiException) {
            handleError(e)
        }
    }

    fun authenticate(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        vm.authenticate(credential).observe(this, Observer {
            if (it != null) dismiss()
        })
    }
}