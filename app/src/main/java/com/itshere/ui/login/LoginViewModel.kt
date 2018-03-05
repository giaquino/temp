package com.itshere.ui.login

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import com.google.firebase.auth.AuthCredential
import com.itshere.model.entity.Account
import com.itshere.model.repository.AccountRepository
import com.itshere.network.Status.ERROR
import com.itshere.network.Status.LOADING
import com.itshere.network.Status.SUCCESS
import javax.inject.Inject

/**
 * Scope is managed by ViewModelProviders
 */
class LoginViewModel @Inject constructor(
        private val accountRepository: AccountRepository) : ViewModel() {

    val hasError = ObservableBoolean(false)

    val isLoading = ObservableBoolean(false)

    val errorMessage = ObservableField<String>("")

    fun authenticate(credential: AuthCredential): LiveData<Account> {
        val mediator = MediatorLiveData<Account>()
        mediator.addSource(accountRepository.authenticate(credential).getAsLiveData(), {
            if (it != null) {
                val status = it.status
                hasError.set(status == ERROR)
                isLoading.set(status == LOADING)
                errorMessage.set(if (status == ERROR) it.message else "")
                if (status == SUCCESS) mediator.postValue(it.data)
            }
        })
        return mediator
    }
}