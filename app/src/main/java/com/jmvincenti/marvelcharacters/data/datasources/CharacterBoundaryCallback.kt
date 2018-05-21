package com.jmvincenti.marvelcharacters.data.datasources

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PagedList
import com.jmvincenti.marvelcharacters.data.api.NetworkState
import com.jmvincenti.marvelcharacters.data.api.characters.CharactersClient
import com.jmvincenti.marvelcharacters.data.model.Character
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.Executor

class CharacterBoundaryCallback(private val charactersClient: CharactersClient,
                                private val pageSize: Int,
                                private val getItemOffset: () -> Single<Int>,
                                private val handleResult: (List<Character>) -> Unit,
                                private val retryExecutor: Executor) : PagedList.BoundaryCallback<Character>() {

    private val mLock = Any()
    private var isRunning = false
    val initialLoad = MutableLiveData<NetworkState>()
    val networkState = MutableLiveData<NetworkState>()

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

    override fun onZeroItemsLoaded() {
        Timber.d("onZeroItemsLoaded")
        synchronized(mLock) {
            if (!isRunning) {
                retry = null
                isRunning = true
                initialLoad.postValue(NetworkState.LOADING)
                networkState.postValue(NetworkState.LOADING)
                charactersClient
                        .getCharactersAsync(0, pageSize)
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            Timber.d("onZeroItemsLoaded result")
                            isRunning = false
                            initialLoad.postValue(NetworkState.LOADED)
                            networkState.postValue(NetworkState.LOADED)
                            result.response?.results?.let {
                                handleResult(it)
                            }
                        }, { error ->
                            Timber.e("onZeroItemsLoaded error")
                            Timber.w(error)
                            retry = { onZeroItemsLoaded() }
                            isRunning = false
                            initialLoad.postValue(NetworkState.error(error.message))
                            networkState.postValue(NetworkState.error(error.message))
                        })
            }
        }
    }

    override fun onItemAtEndLoaded(itemAtEnd: Character) {
        Timber.d("onItemAtEndLoaded")
        synchronized(mLock) {
            if (!isRunning) {
                retry = null
                isRunning = true
                networkState.postValue(NetworkState.LOADING)
                getItemOffset()
                        .flatMap { position ->
                            charactersClient.getCharactersAsync(position + 1, pageSize)
                        }
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            Timber.d("onItemAtEndLoaded result")
                            isRunning = false
                            networkState.postValue(NetworkState.LOADED)
                            result.response?.results?.let {
                                handleResult(it)
                            }
                        }, { error ->
                            Timber.e("onItemAtEndLoaded error")
                            Timber.w(error)
                            retry = { onZeroItemsLoaded() }
                            isRunning = false
                            networkState.postValue(NetworkState.error(error.message))
                        })
            }
        }
    }


}