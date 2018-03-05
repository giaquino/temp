package com.itshere.model.repository

import android.arch.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.itshere.model.db.BroadcastDao
import com.itshere.model.entity.Broadcast
import com.itshere.network.NetworkBoundResource
import com.itshere.network.RateLimiter
import com.itshere.network.Resource
import com.itshere.network.Response
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BroadcastRepository @Inject constructor(
        private val dao: BroadcastDao,
        private val firestore: FirebaseFirestore,
        private val rateLimiter: RateLimiter) {

    fun getBroadcasts(userId: String): Flowable<Resource<List<Broadcast>>> {
        return object : NetworkBoundResource<List<Broadcast>, List<Broadcast>>() {

            override fun saveCallResult(item: List<Broadcast>) = dao.syncBroadcasts(item)

            override fun shouldFetch(data: List<Broadcast>): Boolean =
                    data.isEmpty() || rateLimiter.shouldFetch(Broadcast::class.java.name)

            override fun loadFromDb(): Single<List<Broadcast>> = dao.getBroadcasts()

            override fun onFetchSuccess() = rateLimiter.recordFetchTime(Broadcast::class.java.name)

            override fun createCall(): Single<Response<List<Broadcast>>> {
                return Single.fromPublisher<Response<List<Broadcast>>> { publisher ->
                    loadBroadcasts(userId,
                            success = { snapshot: QuerySnapshot ->
                                publisher.onNext(
                                        Response.create(snapshot.map { Broadcast.create(it) }))
                            },
                            failure = { it: Exception ->
                                publisher.onNext(Response.create(it))
                            },
                            complete = {
                                publisher.onComplete()
                            })
                }
            }
        }.getAsFlowable()
    }

    private fun loadBroadcasts(
            userId: String,
            success: (snapshot: QuerySnapshot) -> Unit,
            failure: (error: Exception) -> Unit,
            complete: (snapshot: Task<QuerySnapshot>) -> Unit) {
        firestore.collection(Broadcast.TABLE_NAME).document(userId).collection(Broadcast.TABLE_NAME)
                .get()
                .addOnSuccessListener(success::invoke)
                .addOnFailureListener(failure::invoke)
                .addOnCompleteListener(complete::invoke)
    }


    fun createBroadcast(userId: String, broadcast: Broadcast): LiveData<Resource<Broadcast>> {
        return object : NetworkBoundResource<Broadcast, Broadcast>() {
            override fun saveCallResult(item: Broadcast) {
                dao.addBroadcasts(listOf(item))
            }

            override fun shouldFetch(data: Broadcast): Boolean = true

            override fun loadFromDb(): Single<Broadcast>
                    = Single.just<Broadcast>(null).onErrorReturn { null }

            override fun createCall(): Single<Response<Broadcast>> {
                return Single.fromPublisher<Response<Broadcast>> { publisher ->
                    createBroadcast(userId, broadcast,
                            success = {
                                publisher.onNext(Response.create(broadcast))
                            },
                            failure = { it: Exception ->
                                publisher.onNext(Response.create(it))
                            },
                            complete = {
                                publisher.onComplete()
                            })
                }
            }
        }.getAsLiveData()
    }

    private fun createBroadcast(userId: String, broadcast: Broadcast, success: (void: Void) -> Unit,
            failure: (error: Exception) -> Unit, complete: (task: Task<Void>) -> Unit) {
        firestore.collection(Broadcast.TABLE_NAME)
                .document(userId)
                .collection(Broadcast.TABLE_NAME)
                .document(broadcast.deviceIdentifier)
                .set(broadcast.toFirestoreMap())
                .addOnSuccessListener(success::invoke)
                .addOnFailureListener(failure::invoke)
                .addOnCompleteListener(complete::invoke)
    }
}