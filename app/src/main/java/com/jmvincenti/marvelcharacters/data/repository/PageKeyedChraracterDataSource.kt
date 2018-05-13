package com.jmvincenti.marvelcharacters.data.repository

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PageKeyedDataSource
import com.jmvincenti.marvelcharacters.data.api.NetworkState
import com.jmvincenti.marvelcharacters.data.api.characters.CharactersClient
import com.jmvincenti.marvelcharacters.data.model.Character
import com.jmvincenti.marvelcharacters.data.model.CharacterDataWrapper
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import java.util.concurrent.Executor

/**
 * TODO: Add a class header comment! 😘
 */
class PageKeyedChraracterDataSource(private val client: CharactersClient,
                                    private val retryExecutor: Executor) : PageKeyedDataSource<Int, Character>() {

    // keep a function reference for the retry event
    private var retry: (() -> Any)? = null

    fun retryAllFailed() {
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
        val request = client.getCharacters(
                offset = 0,
                limit = params.requestedLoadSize)
        networkState.postValue(NetworkState.LOADING)
        initialLoad.postValue(NetworkState.LOADING)

        // triggered by a refresh, we better execute sync
        try {
            val response = request.execute()
            val data = response.body()?.response
            networkState.postValue(NetworkState.LOADED)
            initialLoad.postValue(NetworkState.LOADED)
            retry = null
            if (data != null) {
                val items = data.results ?: emptyList()
                if (data.offset + data.count < data.total) {
                    callback.onResult(items, null, data.offset + data.limit)
                } else {
                    callback.onResult(items, null, null)
                }
            } else {
                val error = NetworkState.error("empty data")
                callback.onResult(emptyList(), null, null)
                handleInitialError(params, callback, error)
            }
        } catch (ioException: IOException) {
            val error = NetworkState.error(ioException.message ?: "unknown error")
            handleInitialError(params, callback, error)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Character>) {
        networkState.postValue(NetworkState.LOADING)
        client.getCharacters(
                offset = params.key,
                limit = params.requestedLoadSize).enqueue(
                object : retrofit2.Callback<CharacterDataWrapper<Character>> {
                    override fun onFailure(call: Call<CharacterDataWrapper<Character>>, t: Throwable) {
                        retry = {
                            loadAfter(params, callback)
                        }
                        networkState.postValue(NetworkState.error(t.message ?: "unknown error"))
                    }

                    override fun onResponse(
                            call: Call<CharacterDataWrapper<Character>>,
                            response: Response<CharacterDataWrapper<Character>>) {

                        if (response.isSuccessful) {
                            val data = response.body()?.response
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
                                callback.onResult(emptyList(), null)
                                handleCallError(params, callback, error)
                            }
                        } else {
                            val error = NetworkState.error("error code: ${response.code()}")
                            handleCallError(params, callback, error)
                        }
                    }
                }
        )
    }

    private fun handleInitialError(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Character>, error: NetworkState) {
        retry = {
            loadInitial(params, callback)
        }
        networkState.postValue(error)
        initialLoad.postValue(error)
    }

    private fun handleCallError(params: LoadParams<Int>, callback: LoadCallback<Int, Character>, error: NetworkState) {
        retry = {
            loadAfter(params, callback)
        }
        networkState.postValue(error)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Character>) {
        // ignored, since we only ever append to our initial load
    }
}