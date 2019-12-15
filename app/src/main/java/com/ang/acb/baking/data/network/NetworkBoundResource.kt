package com.ang.acb.baking.data.network

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.ang.acb.baking.utils.AppExecutors

/**
 * A generic class that can provide a resource backed by both the SQLite database and the network.
 * It defines two type parameters, ResultType and RequestType, because the data type returned from
 * the API might not match the data type used locally.
 *
 * See: https://developer.android.com/jetpack/docs/guide#addendum
 * See: https://github.com/android/android-architecture-components/tree/master/GithubBrowserSample
 *
 * @param <ResultType> Type for the Resource data.
 * @param <RequestType> Type for the API response.
 */
abstract class NetworkBoundResource<ResultType, RequestType>
@MainThread constructor(private val appExecutors: AppExecutors) {

    // The final result LiveData.
    private val result = MediatorLiveData<Resource<ResultType>>()

    init {
        // Send loading state to the UI.
        result.value = Resource.loading(null)

        // Get the cached data from the database.
        @Suppress("LeakingThis")
        val dbSource = loadFromDb()

        // Start to listen to the database data.
        result.addSource(dbSource) { data ->
            result.removeSource(dbSource)
            if (shouldFetch(data)) {
                fetchFromNetwork(dbSource)
            } else {
                result.addSource(dbSource) { newData ->
                    setValue(Resource.success(newData))
                }
            }
        }
    }

    @MainThread
    private fun setValue(newValue: Resource<ResultType>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    /**
     * Fetch the data from network and persist into DB and then send it back to UI.
     */
    private fun fetchFromNetwork(dbSource: LiveData<ResultType>) {
        // Create the API call to load data from themoviedb.org.
        val apiResponse = createCall()

        // Re-attach db as a new source, it will dispatch its latest value quickly.
        result.addSource(dbSource) { newData ->
            setValue(Resource.loading(newData))
        }

        // Start listening to the API response.
        result.addSource(apiResponse) { response ->
            result.removeSource(apiResponse)
            result.removeSource(dbSource)
            when (response) {
                // If the network call completes successfully, save the response
                // into the database and re-initialize the stream.
                is ApiSuccessResponse -> {
                    appExecutors.diskIO().execute {
                        saveCallResult(processResponse(response))
                        appExecutors.mainThread().execute {
                            // We specially request new live data, otherwise we will
                            // get the immediately last cached value, which may not
                            // be updated with latest results received from network.
                            result.addSource(loadFromDb()) { newData ->
                                setValue(Resource.success(newData))
                            }
                        }
                    }
                }
                // If the response is empty, reload from disk whatever we had.
                is ApiEmptyResponse -> {
                    appExecutors.mainThread().execute {
                        result.addSource(loadFromDb()) { newData ->
                            setValue(Resource.success(newData))
                        }
                    }
                }
                // If network request fails, dispatch a failure directly.
                is ApiErrorResponse -> {
                    onFetchFailed()
                    result.addSource(dbSource) { newData ->
                        setValue(Resource.error(response.errorMessage, newData))
                    }
                }
            }
        }
    }

    @WorkerThread
    protected open fun processResponse(
        response: ApiSuccessResponse<RequestType>) = response.body

    /**
     * Called to create the API call.
     * @param: <RequestType> Type for the API response (remote data).
     */
    @MainThread
    protected abstract fun createCall(): LiveData<ApiResponse<RequestType>>

    /**
     * Called to save the result of the API response into the database.
     * @param: <RequestType> Type for the API response (remote data).
     */
    @WorkerThread
    protected abstract fun saveCallResult(result: RequestType)

    protected open fun onFetchFailed() {}

    /**
     * Called with the data in the database to decide whether to fetch
     * potentially updated data from the network.
     * @param: <ResultType> Type for the Resource data (local data).
     */
    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    /**
     *  Called to get the cached data from the database.
     *  @param: <ResultType> Type for the Resource data (local data)
     */
    @MainThread
    protected abstract fun loadFromDb(): LiveData<ResultType>

    /**
     * Returns a LiveData object that represents the resource that's
     * implemented in the base class.
     * @param: <ResultType> Type for the Resource data (local data).
     */
    fun asLiveData() = result as LiveData<Resource<ResultType>>
}