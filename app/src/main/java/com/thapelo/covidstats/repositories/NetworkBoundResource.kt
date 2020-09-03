package com.thapelo.covidstats.repositories

import android.annotation.SuppressLint
import android.util.Log
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import timber.log.Timber

// ResultType: Type for the Resource data.
// RequestType: Type for the API response.
abstract class NetworkBoundResource<ResultType, RequestType> @MainThread constructor(private val appExecutors: AppExecutors) {

    private val result = MediatorLiveData<Resource<ResultType>>()

    init {
        result.value = Resource.Loading(null)
        @Suppress("LeakingThis")
        Log.d("INIT", "DB runs here")
        val dbSource = loadFromDb()
        result.addSource(dbSource) { data ->
            Log.d("INIT", "goes forward")
            result.removeSource(dbSource)
            if (true) {
                Log.d("INIT", "gets to fetch")
                fetchFromNetwork(dbSource)
            } else {
                result.addSource(dbSource) { newData ->
                    result.value = Resource.Success(newData)
                }
            }
        }
    }


    private fun fetchFromNetwork(dbSource: LiveData<ResultType>) {
        val apiResponse = createCall()
        // we re-attach dbSource as a new source, it will dispatch its latest value quickly
        result.addSource(dbSource) { newData ->
            result.value = Resource.Loading(newData)
        }
        result.addSource(apiResponse) { response ->
            result.removeSource(apiResponse)
            result.removeSource(dbSource)
            when (response) {
                is ApiSuccessResponse -> {
                    appExecutors.diskIO().execute {
                        saveCallResult(processResponse(response))
                        appExecutors.mainThread().execute {
                            // we specially request a new live data,
                            // otherwise we will get immediately last cached value,
                            // which may not be updated with latest results received from network.
                            Timber.d("fetch - ApiSuccess")
                            result.addSource(loadFromDb()) { newData ->
                                result.value = Resource.Success(newData)
                            }
                        }
                    }
                }
                is ApiEmptyResponse -> {
                    appExecutors.mainThread().execute {
                        // reload from disk whatever we had
                        Timber.d("fetch - ApiEmpty")
                        result.addSource(loadFromDb()) { newData ->
                            result.value = Resource.Success(newData)
                        }
                    }
                }
                is ApiErrorResponse -> {
                    onFetchFailed()
                    Timber.d("fetch - ApiError")
                    result.addSource(dbSource) { newData ->
                        result.value = Resource.Error(response.errorMessage, newData)
                    }
                }
            }
        }
    }

    @SuppressLint("LogNotTimber")
    @WorkerThread
    protected open fun processResponse(response: ApiSuccessResponse<RequestType>): RequestType {
        val resBody = response.body
        Log.d("response body", resBody.toString())
        return resBody
    }

    // Called to save the result of the API response into the database
    @WorkerThread
    protected abstract fun saveCallResult(item: RequestType)

    // Called with the data in the database to decide whether to fetch
    // potentially updated data from the network.
    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    // Called to get the cached data from the database.
    @MainThread
    protected abstract fun loadFromDb(): LiveData<ResultType>

    // Called to create the API call.
    @MainThread
    protected abstract fun createCall(): LiveData<ApiResponse<RequestType>>

    // Called when the fetch fails. The child class may want to reset components
    // like rate limiter.
    protected open fun onFetchFailed() {}

    // Returns a LiveData object that represents the resource that's implemented
    // in the base class.
    fun asLiveData() = result as LiveData<Resource<ResultType>>
}
