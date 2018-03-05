package com.itshere.model.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.itshere.model.entity.Account
import com.itshere.network.NetworkBoundResource
import com.itshere.network.Response
import io.reactivex.Single
import org.reactivestreams.Subscriber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountRepository @Inject constructor(
        private val firebaseAuth: FirebaseAuth,
        private val firestore: FirebaseFirestore) {

    private val account = MutableLiveData<Account>()

    init {
        if (firebaseAuth.currentUser != null) {
            account.postValue(Account.create(firebaseAuth.currentUser!!))
        } else {
            account.postValue(null)
        }
    }

    fun getAccount(): LiveData<Account> = account

    fun authenticate(credential: AuthCredential): NetworkBoundResource<Account, Account> {
        return object : NetworkBoundResource<Account, Account>() {

            override fun saveCallResult(item: Account) = account.postValue(item)

            override fun shouldFetch(data: Account): Boolean = true

            override fun loadFromDb(): Single<Account> {
                if (firebaseAuth.currentUser != null) {
                    return Single.just(Account.create(firebaseAuth.currentUser!!))
                }
                return Single.error(NullPointerException("account is null"))
            }

            override fun createCall(): Single<Response<Account>> {
                return Single.fromPublisher<Response<Account>> { subscriber ->
                    firebaseAuth.signInWithCredential(credential).addOnSuccessListener {
                        fetchAccount(it.user, subscriber)
                    }.addOnFailureListener {
                        subscriber.onNext(Response.create(it))
                        subscriber.onComplete()
                    }
                }.onErrorReturn { Response.create(it) }
            }
        }
    }


    fun fetchAccount(user: FirebaseUser, subscriber: Subscriber<in Response<Account>>) {
        firestore.collection("accounts").document(user.uid).get()
                .addOnSuccessListener {
                    if (it.exists()) {
                        subscriber.onNext(Response.create(Account.create(user, it)))
                    }

                }
                .addOnFailureListener { subscriber.onNext(Response.create(it)) }
                .addOnCompleteListener { subscriber.onComplete() }
    }

    fun createAccount(user: FirebaseUser, subscriber: Subscriber<in Response<Account>>) {
        firestore.collection(Account.TABLE_NAME).document(user.uid)
                .set(Account.create(user).toFirestoreMap())
    }
}