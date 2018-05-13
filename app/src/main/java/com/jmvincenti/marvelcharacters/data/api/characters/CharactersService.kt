package com.jmvincenti.marvelcharacters.data.api.characters

import com.jmvincenti.marvelcharacters.data.model.Character
import com.jmvincenti.marvelcharacters.data.model.CharacterDataWrapper
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * API entries for character model
 */
interface CharactersService {

    @GET("v1/public/characters")
    fun getCharacters(@Query("offset") offset: Int = 0): Single<CharacterDataWrapper<Character>>


}