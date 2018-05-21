package com.jmvincenti.marvelcharacters.data.api.characters

import com.jmvincenti.marvelcharacters.data.api.BaseApiClient
import io.reactivex.Single
import retrofit2.Retrofit

/**
 * Client for Character model calls
 */
class CharactersClient(retrofit: Retrofit) : BaseApiClient(retrofit) {

    private val service: CharactersService by lazy { getApi(CharactersService::class.java) }

    fun getCharactersSync(offset: Int = 0, limit: Int, startName: String? = null) = service.getCharactersSync(offset, limit, startName)
    fun getCharactersAsync(offset: Int = 0, limit: Int, startName: String? = null) = service.getCharactersAsync(offset, limit, startName)

    fun getCharacter(id: Int) = service.getCharacter(id).flatMap { result -> Single.just(result.response?.results?.get(0)) }

}
