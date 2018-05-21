package com.jmvincenti.marvelcharacters.ui.characterlist

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.arch.paging.PagedList
import com.jmvincenti.marvelcharacters.data.api.NetworkState
import com.jmvincenti.marvelcharacters.data.model.Character
import com.jmvincenti.marvelcharacters.data.repository.CharacterRepository
import com.jmvincenti.marvelcharacters.ui.utils.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class CharacterListViewModel( val repository: CharacterRepository) : ViewModel() {
    private val searchSubject = PublishSubject.create<String?>()
    private val compositeDisposable = CompositeDisposable()

    var onNewList = SingleLiveEvent<Boolean>()
    var lastFilter: String? = null

    init {
        compositeDisposable.add(searchSubject
                .debounce(800, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { filter ->
                    if (filter.isNullOrBlank()) {
                        if (lastFilter != null) {
                            lastFilter = null
                            onNewList.value = true
                            repository.applyFilter(null)
                        }
                    } else {
                        if (filter != lastFilter) {
                            lastFilter = filter
                            onNewList.value = true
                            repository.applyFilter(filter)
                        }
                    }
                })
    }

    fun applyFilter(query: String?) {
        searchSubject.onNext(query ?: "")
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun onTryAgain() {
        repository.tryAgain()
    }

    val characterList: LiveData<PagedList<Character>> = repository.charactersLiveData
    val initialLoadState: LiveData<NetworkState> = repository.initialLoadState
    val loadMoreState: LiveData<NetworkState> = repository.loadMoreState
}