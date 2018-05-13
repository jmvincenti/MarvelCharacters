package com.jmvincenti.marvelcharacters.data.api

/**
 * TODO: Add a class header comment! 😘
 */


enum class Status {
    RUNNING,
    SUCCESS,
    FAILED
}
@Suppress("DataClassPrivateConstructor")
class NetworkState private constructor(
    val status: Status,
    val msg: String? = null) {
        companion object {
            val LOADED = NetworkState(Status.SUCCESS)
            val LOADING = NetworkState(Status.RUNNING)
            fun error(msg: String?) = NetworkState(Status.FAILED, msg)
        }
}
