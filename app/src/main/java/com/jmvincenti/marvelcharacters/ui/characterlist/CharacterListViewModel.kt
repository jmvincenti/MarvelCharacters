package com.jmvincenti.marvelcharacters.ui.characterlist

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.jmvincenti.marvelcharacters.data.model.Character
import com.jmvincenti.marvelcharacters.data.repository.CharactersDataSourceFactory
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

/**
 * TODO: Add a class header comment! 😘
 */

class CharacterListViewModel(sourceFactory: CharactersDataSourceFactory) : ViewModel() {
    private val searchSubject = PublishSubject.create<String?>()
    private val compositeDisposable = CompositeDisposable()
    var presenter: CharacterListContract.Presenter<CharacterListContract.View>? = null

    init {
        compositeDisposable.add(searchSubject
                .debounce(500, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .subscribe({ filter ->
                    sourceFactory.sourceLiveData.value?.applyFilter(filter)
                }, { throwable ->
                    throwable.printStackTrace()
                    presenter?.handleError(throwable)
                }))
    }

    fun applyFilter(query: String?) {
        searchSubject.onNext(query ?: "")
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    val characterList: LiveData<PagedList<Character>> = LivePagedListBuilder(
            sourceFactory,
            /* page size */ 20
    ).build()
}