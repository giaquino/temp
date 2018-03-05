package com.itshere.model.repository

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.itshere.model.entity.Account
import com.itshere.network.Resource
import com.itshere.network.Status.ERROR
import com.itshere.network.Status.LOADING
import com.itshere.network.Status.SUCCESS
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

@Suppress("MemberVisibilityCanPrivate", "UNCHECKED_CAST")
class AccountRepositoryUnitTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock lateinit var firebaseAuth: FirebaseAuth
    @Mock lateinit var accountObserver: Observer<Account>
    @Mock lateinit var resourceObserver: Observer<Resource<Account>>

    /* firebase results */
    @Mock lateinit var task: Task<AuthResult>
    @Mock lateinit var result: AuthResult

    lateinit var accountRepository: AccountRepository
    lateinit var successListener: OnSuccessListener<Any>
    lateinit var failureListener: OnFailureListener

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        setFirebaseUser(null)
        accountRepository = AccountRepository(firebaseAuth)
        /* set our mocked task so we can intercept added listener */
        `when`(firebaseAuth.signInWithCredential(any())).thenReturn(task)

        /* store added listener to be called manually */
        `when`(task.addOnSuccessListener(any())).thenAnswer {
            val args = it.arguments
            successListener = args[0] as OnSuccessListener<Any>
            return@thenAnswer task
        }
        `when`(task.addOnFailureListener(any())).thenAnswer {
            val args = it.arguments
            failureListener = args[0] as OnFailureListener
            return@thenAnswer task
        }

    }

    @Test
    fun unAuthenticatedFirebaseReturnsNull() {
        accountRepository.getAccount().observeForever(accountObserver)
        verify(accountObserver).onChanged(null)
    }

    @Test
    fun authenticatedFirebaseReturnsCorrectAccount() {
        val user = createFirebaseUser("1", "Name")
        setFirebaseUser(user)
        accountRepository = AccountRepository(firebaseAuth)
        accountRepository.getAccount().observeForever(accountObserver)
        verify(accountObserver).onChanged(Account.create(user))
    }

    @Test
    fun authenticateReturnsLoadingStateFirst() {
        val authenticate = accountRepository.authenticate(
                mock(AuthCredential::class.java)).getAsLiveData().apply {
            observeForever(resourceObserver)
        }
        assert(authenticate.value!!.status == LOADING)
    }

    @Test
    fun authenticateReturnsErrorStateWithMessage() {
        val authenticate = accountRepository.authenticate(
                mock(AuthCredential::class.java)).getAsLiveData().apply {
            observeForever(resourceObserver)
        }
        assert(authenticate.value!!.status == LOADING)

        /* call failure manually */
        failureListener.onFailure(Exception("error message"))
        assert(authenticate.value!!.status == ERROR)
        assert(authenticate.value!!.message == "error message")
    }

    @Test
    fun authenticateReturnsSuccessWithCorrectAccount() {
        val authenticate = accountRepository.authenticate(
                mock(AuthCredential::class.java)).getAsLiveData().apply {
            observeForever(resourceObserver)
        }
        assert(authenticate.value!!.status == LOADING)

        /* call success manually */
        setFirebaseUser(createFirebaseUser("1", "name"))
        successListener.onSuccess(result)
        assert(authenticate.value!!.status == SUCCESS)
        assert(authenticate.value!!.data!!.id == "1")
        assert(authenticate.value!!.data!!.name == "name")
    }

    private fun setFirebaseUser(user: FirebaseUser?) {
        `when`(result.user).thenReturn(user)
        `when`(firebaseAuth.currentUser).thenReturn(user)
    }

    private fun createFirebaseUser(id: String, name: String): FirebaseUser {
        return mock(FirebaseUser::class.java).apply {
            `when`(this.uid).thenReturn(id)
            `when`(this.displayName).thenReturn(name)
        }
    }
}