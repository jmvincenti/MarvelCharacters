package com.jmvincenti.marvelcharacters.data.repository

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource
import com.jmvincenti.marvelcharacters.data.api.characters.CharactersClient
import com.jmvincenti.marvelcharacters.data.model.Character
import java.util.concurrent.Executor

/**
 * TODO: Add a class header comment! 😘
 */
class CharactersDataSourceFactory(private val client: CharactersClient,
                                  private val retryExecutor: Executor) : DataSource.Factory<Int, Character>() {
    val sourceLiveData = MutableLiveData<PageKeyedChraracterDataSource>()
    override fun create(): DataSource<Int, Character> {
        val source = PageKeyedChraracterDataSource(client, retryExecutor)
        sourceLiveData.postValue(source)
        return source
    }
}