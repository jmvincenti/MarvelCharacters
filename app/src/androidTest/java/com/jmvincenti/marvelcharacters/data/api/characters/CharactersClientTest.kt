package com.jmvincenti.marvelcharacters.data.api.characters

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
        charactersClient.getCharacters()
                .test()
                .assertNoErrors()
                .assertValue { result ->
                    result.code == 200
                }
    }
}