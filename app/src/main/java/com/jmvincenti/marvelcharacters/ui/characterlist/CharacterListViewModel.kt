package com.jmvincenti.marvelcharacters.ui.characterlist

import android.arch.lifecycle.ViewModel
import android.arch.paging.PagedList
import android.arch.paging.RxPagedListBuilder
import com.jmvincenti.marvelcharacters.data.model.Character
import com.jmvincenti.marvelcharacters.data.repository.CharactersDataSourceFactory
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable

/**
 * TODO: Add a class header comment! 😘
 */

class CharacterListViewModel(repository: CharactersDataSourceFactory) : ViewModel() {
    val characterList: Flowable<PagedList<Character>> = RxPagedListBuilder(
            repository,
            /* page size */ 20
    ).buildFlowable(BackpressureStrategy.LATEST)
}