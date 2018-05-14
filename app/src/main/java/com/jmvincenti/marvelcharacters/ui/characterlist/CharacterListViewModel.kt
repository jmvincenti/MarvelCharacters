package com.jmvincenti.marvelcharacters.ui.characterlist

import android.arch.lifecycle.ViewModel
import android.arch.paging.PagedList
import android.arch.paging.RxPagedListBuilder
import com.jmvincenti.marvelcharacters.data.model.Character
import com.jmvincenti.marvelcharacters.data.repository.CharactersDataSourceFactory
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

/**
 * TODO: Add a class header comment! 😘
 */

class CharacterListViewModel(sourceFactory: CharactersDataSourceFactory) : ViewModel() {
    private val searchSubject = PublishSubject.create<String?>()
    private val compositeDisposable = CompositeDisposable()


    init {
        compositeDisposable.add(searchSubject
                .debounce(1000, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .subscribe({ filter ->
                    sourceFactory.sourceLiveData.value?.applyFilter(filter)
                }, { throwable ->
                    throwable.printStackTrace()
                    //TODO deal with error
                }))
    }

    fun applyFilter(query: String?) {
        searchSubject.onNext(query ?: "")
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    val characterList: Flowable<PagedList<Character>> = RxPagedListBuilder(
            sourceFactory,
            /* page size */ 20
    ).buildFlowable(BackpressureStrategy.LATEST)
}