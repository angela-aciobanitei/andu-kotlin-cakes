package com.ang.acb.baking.data.network

/**
 * A generic class that holds a value with its loading status.
 */
data class Resource<out T>(val status: Status, val data: T?, val message: String?) {

    companion object {
        // Creates a Resource object with a SUCCESS status and some data.
        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        // Creates a Resource object with an ERROR status and an error message.
        fun <T> error(msg: String, data: T?): Resource<T> {
            return Resource(Status.ERROR, data, msg)
        }

        // Creates a Resource object with a LOADING status to notify the UI.
        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }
    }
}


/**
 * Status of a resource that is provided to the UI. These are usually created
 * by the Repository classes where they return LiveData<Resource<T>> to pass
 * back the latest data to the UI with its fetch status.
 */
enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}