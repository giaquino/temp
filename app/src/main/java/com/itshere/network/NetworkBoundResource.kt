package com.itshere.network

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.LiveDataReactiveStreams
import android.support.annotation.MainThread
import android.support.annotation.WorkerThread
import com.itshere.common.util.Optional
import com.itshere.network.Status.LOADING
import io.reactivex.Flowable
import io.reactivex.Single


abstract class NetworkBoundResource<ResultType, RequestType> {

    private val result: Flowable<Resource<ResultType>>

    @Suppress("LeakingThis")
    @MainThread constructor() {
        /* load from db and cache it */
        val cache = loadFromDb()
                .map { Optional.of(it) }
                .onErrorReturn { Optional.empty() }
                .cache()

        /* get from network if possible and return success/error status */
        val network = cache.flatMap {
            if (it.value == null || shouldFetch(it.value)) {
                return@flatMap loadFromNetwork(it.value)
            }
            return@flatMap Single.just(Resource.success(it.value))
        }.toFlowable()

        /* return loading state first */
        val loading = cache.map { Resource.loading(it.value) }.toFlowable()

        /* once we receive success/error status return a completable flowable */
        result = Flowable.concat(loading, network).flatMap {
            if (it.status == LOADING) {
                return@flatMap Flowable.just(it)
            }
            return@flatMap Single.just(it).toFlowable()
        }
    }

    private fun loadFromNetwork(result: ResultType?): Single<Resource<ResultType>> {
        return createCall().flatMap { response ->
            if (response.isSuccessful()) {
                onFetchSuccess()
                saveCallResult(response.body!!) // synchronous
                return@flatMap loadFromDb().map { Resource.success(it) } // always return from db
            } else {
                onFetchFailure()
                return@flatMap Single.just(Resource.error(response.errorMessage!!, result))
            }
        }
    }


    @WorkerThread
    abstract fun saveCallResult(item: RequestType)

    abstract fun shouldFetch(data: ResultType): Boolean

    abstract fun loadFromDb(): Single<ResultType>

    abstract fun createCall(): Single<Response<RequestType>>

    open fun onFetchFailure() = Unit

    open fun onFetchSuccess() = Unit

    /**
     * Returns 2 [Resource] item in sequence before completing the flowable.
     *
     * 1. [Status.LOADING]
     * 2. [Status.SUCCESS] or [Status.ERROR]
     *
     */
    fun getAsFlowable(): Flowable<Resource<ResultType>> = result

    fun getAsLiveData(): LiveData<Resource<ResultType>> = LiveDataReactiveStreams.fromPublisher(
            result)
}