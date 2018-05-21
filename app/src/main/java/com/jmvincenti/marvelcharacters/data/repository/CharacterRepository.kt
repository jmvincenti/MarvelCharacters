package com.jmvincenti.marvelcharacters.data.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.Transformations
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.jmvincenti.marvelcharacters.data.api.NetworkState
import com.jmvincenti.marvelcharacters.data.api.characters.CharactersClient
import com.jmvincenti.marvelcharacters.data.database.CharacterDao
import com.jmvincenti.marvelcharacters.data.model.Character
import io.reactivex.Single
import java.util.concurrent.Executor


/**
 * Character repository. Select local data source if no filter is required else remote data source
 */
class CharacterRepository(
        val remoteSourceFactory: CharactersDataSourceFactory,
        charactersClient: CharactersClient,
        val characterDao: CharacterDao,
        retryExecutor: Executor) {

    val PAGE_SIZE = 20
    private var initialLiveDataMerger = MediatorLiveData<NetworkState>()
    private var loadMoreLiveDataMerger = MediatorLiveData<NetworkState>()
    private var characterLiveDataMerger = MediatorLiveData<PagedList<Character>>()

    var isRemoteMode: Boolean = false
    private val mLock = Any()


    private val boundaryCallback = CharacterBoundaryCallback(
            charactersClient = charactersClient,
            pageSize = PAGE_SIZE,
            getItemOffset = { characterDao.getCount().flatMap { result -> Single.just(result.size) } },
            handleResult = { result -> characterDao.insertOrUpdate(result) },
            retryExecutor = retryExecutor
    )

    private val localCharacterList: LiveData<PagedList<Character>> = LivePagedListBuilder(
            characterDao.characterByName(),
            /* page size */ PAGE_SIZE
    )
            .setBoundaryCallback(boundaryCallback)
            .build()

    private val remoteCharacterList: LiveData<PagedList<Character>> = LivePagedListBuilder(remoteSourceFactory, PAGE_SIZE)
            .build()


    init {
        characterLiveDataMerger.addSource(remoteCharacterList, { page -> if (isRemoteMode) characterLiveDataMerger.value = page })
        characterLiveDataMerger.addSource(localCharacterList, { page -> if (!isRemoteMode) characterLiveDataMerger.value = page })

        initialLiveDataMerger.addSource(Transformations.switchMap<PageKeyedChraracterDataSource, NetworkState>(
                remoteSourceFactory.sourceLiveData, { it.initialLoad }), { state -> if (isRemoteMode) initialLiveDataMerger.value = state })
        initialLiveDataMerger.addSource(boundaryCallback.initialLoad, { state -> if (!isRemoteMode) initialLiveDataMerger.value = state })

        loadMoreLiveDataMerger.addSource(Transformations.switchMap<PageKeyedChraracterDataSource, NetworkState>(
                remoteSourceFactory.sourceLiveData, { it.networkState }), { state -> if (isRemoteMode) loadMoreLiveDataMerger.value = state })
        loadMoreLiveDataMerger.addSource(boundaryCallback.networkState, { state -> if (!isRemoteMode) loadMoreLiveDataMerger.value = state })
    }


    fun applyFilter(query: String?) {
        synchronized(mLock) {
            if (query.isNullOrBlank()) {
                if (isRemoteMode) {
                    isRemoteMode = false
                    localCharacterList.value?.dataSource?.invalidate()
                }
                return
            } else {
                if (!isRemoteMode) {
                    isRemoteMode = true
                }
                remoteSourceFactory.sourceLiveData.value?.applyFilter(query)
            }
        }
    }

    fun tryAgain() {
        if (isRemoteMode) {
            remoteSourceFactory.sourceLiveData.value?.retryAllFailed()
        } else {
            boundaryCallback.retryAllFailed()
        }
    }

    val initialLoadState: LiveData<NetworkState> = initialLiveDataMerger
    val loadMoreState: LiveData<NetworkState> = loadMoreLiveDataMerger
    val charactersLiveData: LiveData<PagedList<Character>> = characterLiveDataMerger


}