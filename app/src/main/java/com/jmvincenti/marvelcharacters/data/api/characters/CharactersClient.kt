package com.jmvincenti.marvelcharacters.data.api.characters

import com.jmvincenti.marvelcharacters.data.api.BaseApiClient
import com.jmvincenti.marvelcharacters.data.model.Character
import com.jmvincenti.marvelcharacters.data.model.CharacterDataWrapper
import retrofit2.Call

/**
 * Client for Character model calls
 */
class CharactersClient : BaseApiClient() {

    private val service: CharactersService by lazy { getApi(CharactersService::class.java) }


    fun getCharacters(offset: Int = 0, limit: Int): Call<CharacterDataWrapper<Character>> = service.getCharacters(offset, limit)


}
