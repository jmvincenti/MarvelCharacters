package com.jmvincenti.marvelcharacters.ui.guess

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.jmvincenti.marvelcharacters.data.api.NetworkState
import com.jmvincenti.marvelcharacters.data.api.characters.CharactersClient
import com.jmvincenti.marvelcharacters.data.model.Character
import com.jmvincenti.marvelcharacters.data.preferences.MyPreferences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class GuessViewModel(val charactersClient: CharactersClient) : ViewModel() {

    private val liveData = MutableLiveData<GuessResult>()
    private val netWorkliveData = MutableLiveData<NetworkState>()
    private var characters: List<Character>? = null
    private var guessResult: GuessResult? = null
    private var attempt = 0
    private val maxAttempt = 3
    private val mLock = Any()
    private var isRunning = false

    var guessPresenter: GuessContract.Presenter? = null

    fun getCharacterLiveData(): LiveData<GuessResult> = liveData
    fun getStateLiveData(): LiveData<NetworkState> = netWorkliveData

    fun getNewRandom() {
        synchronized(mLock) {
            if (!isRunning) {
                isRunning = true
                netWorkliveData.postValue(NetworkState.LOADING)
                charactersClient.getCharactersAsync((Math.random() * (MyPreferences.maxCharacter - 4)).toInt(), 4)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ result ->
                            netWorkliveData.postValue(NetworkState.LOADED)
                            result?.response?.total?.let {
                                MyPreferences.maxCharacter = it
                            }
                            characters = result.response?.results
                            guessResult = GuessResult(characters?.get((Math.random() * 4).toInt()), characters)
                            if (attempt < maxAttempt && guessResult?.target?.thumbnail?.path?.contains("image_not_available") != false) {
                                attempt++
                                getNewRandom()
                            } else {
                                attempt = 0
                                liveData.postValue(guessResult)
                            }
                            isRunning = false
                        }, { error ->
                            netWorkliveData.postValue(NetworkState.error(error.message))
                            isRunning = false
                        })
            }
        }
    }


}