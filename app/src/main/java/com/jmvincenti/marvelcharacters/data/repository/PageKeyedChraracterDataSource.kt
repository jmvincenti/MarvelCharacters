package com.jmvincenti.marvelcharacters.data.repository

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PageKeyedDataSource
import com.jmvincenti.marvelcharacters.data.api.NetworkState
import com.jmvincenti.marvelcharacters.data.api.characters.CharactersClient
import com.jmvincenti.marvelcharacters.data.model.Character
import timber.log.Timber
import java.io.IOException
import java.util.concurrent.Executor

class PageKeyedChraracterDataSource(private val client: CharactersClient,
                                    private val retryExecutor: Executor) : PageKeyedDataSource<Int, Character>() {

    // keep a function reference for the retry event
    private var retry: (() -> Any)? = null

    fun retryAllFailed() {
        Timber.i("retryAllFailed")
        val prevRetry = retry
        retry = null
        prevRetry?.let {
            retryExecutor.execute {
                it.invoke()
            }
        }
    }

    val networkState = MutableLiveData<NetworkState>()
    val initialLoad = MutableLiveData<NetworkState>()

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Character>) {
        Timber.i("loadInitial")
        val request = client.getCharactersSync(
                offset = 0,
                limit = params.requestedLoadSize,
                startName = filter)
        networkState.postValue(NetworkState.LOADING)
        initialLoad.postValue(NetworkState.LOADING)

        // triggered by a refresh, we better execute sync
        try {
            val response = request.execute()
            val data = response.body()?.response
            retry = null
            if (data != null) {
                networkState.postValue(NetworkState.LOADED)
                initialLoad.postValue(NetworkState.LOADED)
                val items = data.results ?: emptyList()
                if (data.offset + data.count < data.total) {
                    callback.onResult(items, null, data.offset + data.limit)
                } else {
                    callback.onResult(items, null, null)
                }
            } else {
                val error = NetworkState.error("empty data")
                networkState.postValue(error)
                initialLoad.postValue(error)
                callback.onResult(emptyList(), null, null)
                handleInitialError(params, callback)
            }
        } catch (ioException: IOException) {
            val error = NetworkState.error(ioException.message ?: "unknown error")
            networkState.postValue(error)
            initialLoad.postValue(error)
            handleInitialError(params, callback)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Character>) {
        Timber.i("loadAfter")
        val request = client.getCharactersSync(
                offset = params.key,
                limit = params.requestedLoadSize,
                startName = filter)

        networkState.postValue(NetworkState.LOADING)
        // triggered by a refresh, we better execute sync
        try {
            val response = request.execute()
            val data = response.body()?.response
            retry = null
            if (data != null) {
                networkState.postValue(NetworkState.LOADED)
                val items = data.results ?: emptyList()
                if (data.offset + data.count < data.total) {
                    callback.onResult(items, data.offset + data.limit)
                } else {
                    callback.onResult(items, null)
                }
            } else {
                val error = NetworkState.error("empty data")
                networkState.postValue(error)
                callback.onResult(emptyList(), null)
                handleCallError(params, callback)
            }

        } catch (ioException: IOException) {
            val error = NetworkState.error(ioException.message ?: "unknown error")
            handleCallError(params, callback)
            networkState.postValue(error)
        }
    }

    private fun handleInitialError(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Character>) {
        retry = {
            loadInitial(params, callback)
        }
    }

    private fun handleCallError(params: LoadParams<Int>, callback: LoadCallback<Int, Character>) {
        retry = {
            loadAfter(params, callback)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Character>) {
        // ignored, since we only ever append to our initial load
    }

    fun applyFilter(filter: String?) {
        val newFilter = if (filter.isNullOrBlank()) {
            null
        } else {
            filter
        }
        PageKeyedChraracterDataSource.filter = newFilter
        invalidate()
    }

    companion object {
        var filter: String? = null
    }
}