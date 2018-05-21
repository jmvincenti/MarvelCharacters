package com.jmvincenti.marvelcharacters.data.api.characters

import io.reactivex.Single
import org.junit.Before
import org.junit.Test

/**
 * Tests for API calls
 * Used to check the Client implementation
 */
class CharactersClientTest {

    lateinit var charactersClient: CharactersClient

    @Before
    fun iniTest() {
        charactersClient = CharactersClient()
    }

    @Test
    fun getCharacters() {
        Single.fromCallable {
            val request = charactersClient.getCharactersSync(0, 10)
            return@fromCallable request.execute()
        }
                .test()
                .assertNoErrors()
                .assertValue { result ->
                    result.isSuccessful && result.body()?.response?.count == 10
                }
    }


    @Test
    fun getCharacter() {
        charactersClient.getCharacter(1009144)
                .test()
                .assertNoErrors()
                .assertValue { result ->
                    result.code == 200
                }
    }
}