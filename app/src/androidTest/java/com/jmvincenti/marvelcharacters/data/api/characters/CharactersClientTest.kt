package com.jmvincenti.marvelcharacters.data.api.characters

import com.jmvincenti.marvelcharacters.injection.InjectorManager
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

/**
 * <b>Integration</b> tests for API calls
 * Used to check the Client implementation
 */
class CharactersClientTest {

    private lateinit var charactersClient: CharactersClient

    @Before
    fun iniTest() {
        charactersClient = CharactersClient(InjectorManager.getRetrofitClient())
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
    fun getCharactersAsync() {
        charactersClient.getCharactersAsync(0, 10)
                .test()
                .assertNoErrors()
                .assertValue { result ->
                    result.response?.results?.size == 10
                }
    }


    @Test
    fun getCharacter() {
        charactersClient.getCharacter(1009144)
                .test()
                .assertNoErrors()
                .assertValue { result ->
                    result.results?.get(0)?.id == 1009144
                }
    }
}