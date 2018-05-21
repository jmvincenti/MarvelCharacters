package com.jmvincenti.marvelcharacters.ui.characterlist

import android.arch.lifecycle.*
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.jmvincenti.marvelcharacters.data.api.NetworkState
import com.jmvincenti.marvelcharacters.data.api.characters.CharactersClient
import com.jmvincenti.marvelcharacters.data.database.CharacterDao
import com.jmvincenti.marvelcharacters.data.model.Character
import com.jmvincenti.marvelcharacters.data.datasources.CharacterBoundaryCallback
import com.jmvincenti.marvelcharacters.data.datasources.CharactersDataSourceFactory
import com.jmvincenti.marvelcharacters.data.datasources.PageKeyedChraracterDataSource
import com.jmvincenti.marvelcharacters.ui.utils.SingleLiveEvent
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

class CharacterListViewModel(private val remoteSourceFactory: CharactersDataSourceFactory,
                             charactersClient: CharactersClient,
                             private val characterDao: CharacterDao,
                             retryExecutor: Executor) : ViewModel() {

    private val searchSubject = PublishSubject.create<String?>()
    private val compositeDisposable = CompositeDisposable()
    private val PAGE_SIZE = 20
    private var initialLiveDataMerger = MediatorLiveData<NetworkState>()
    private var loadMoreLiveDataMerger = MediatorLiveData<NetworkState>()
    private val isRemoteMode = MutableLiveData<Boolean>()
    private var lastFilter: String? = null

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
        isRemoteMode.postValue(false)
        compositeDisposable.add(searchSubject
                .debounce(800, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { filter ->
                    applyFilter(filter)
                })
        initialLiveDataMerger.addSource(Transformations.switchMap<PageKeyedChraracterDataSource, NetworkState>(
                remoteSourceFactory.sourceLiveData, { it.initialLoad }), { state -> if (isRemoteMode.value == true) initialLiveDataMerger.value = state })
        initialLiveDataMerger.addSource(boundaryCallback.initialLoad, { state -> if (isRemoteMode.value == false) initialLiveDataMerger.value = state })

        loadMoreLiveDataMerger.addSource(Transformations.switchMap<PageKeyedChraracterDataSource, NetworkState>(
                remoteSourceFactory.sourceLiveData, { it.networkState }), { state -> if (isRemoteMode.value == true) loadMoreLiveDataMerger.value = state })
        loadMoreLiveDataMerger.addSource(boundaryCallback.networkState, { state -> if (isRemoteMode.value == false) loadMoreLiveDataMerger.value = state })
    }

    val initialLoadState: LiveData<NetworkState> = initialLiveDataMerger
    val loadMoreState: LiveData<NetworkState> = loadMoreLiveDataMerger
    val characterList: LiveData<PagedList<Character>> = Transformations.switchMap(isRemoteMode) { isRemoteMode ->
        if (isRemoteMode) remoteCharacterList else localCharacterList
    }
    var onNewList = SingleLiveEvent<Boolean>()



    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun requestFilter(query: String?) {
        Timber.i("requestFilter $query")
        searchSubject.onNext(query ?: "")
    }

    private fun applyFilter(query: String?) {
        Timber.i("applyFilter $query")
        if (query.isNullOrBlank()) {
            if (lastFilter != null) {
                lastFilter = null
                onNewList.value = true
                if (isRemoteMode.value == true) {
                    isRemoteMode.postValue(false)
                    Timber.i("isRemoteMode.postValue(false)")
                }
                localCharacterList.value?.dataSource?.invalidate()
            }
            return
        } else {
            if (query != lastFilter) {
                lastFilter = query
                onNewList.value = true
                if (isRemoteMode.value == false) {
                    Timber.i("isRemoteMode.postValue(true)")
                    isRemoteMode.postValue(true)
                }
                remoteSourceFactory.sourceLiveData.value?.applyFilter(query)
            }
        }
    }

    fun onTryAgain() {
        when (isRemoteMode.value) {
            true -> remoteSourceFactory.sourceLiveData.value?.retryAllFailed()
            false -> boundaryCallback.retryAllFailed()
        }
    }


}