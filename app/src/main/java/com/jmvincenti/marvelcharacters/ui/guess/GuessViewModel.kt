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
import timber.log.Timber

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
        Timber.d("getNewRandom $isRunning")
        synchronized(mLock) {
            if (!isRunning) {
                isRunning = true
                Timber.d("getNewRandom LOADING")
                netWorkliveData.postValue(NetworkState.LOADING)
                charactersClient.getCharactersAsync((Math.random() * (MyPreferences.maxCharacter - 4)).toInt(), 4)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ result ->
                            Timber.d("getNewRandom LOADED")
                            netWorkliveData.postValue(NetworkState.LOADED)
                            result?.response?.total?.let {
                                MyPreferences.maxCharacter = it
                            }
                            characters = result.response?.results
                            guessResult = GuessResult(characters?.get((Math.random() * 4).toInt()), characters)
                            isRunning = false
                            if (attempt < maxAttempt && guessResult?.target?.thumbnail?.path?.contains("image_not_available") != false) {
                                Timber.d("getNewRandom new attempt")
                                attempt++
                                getNewRandom()
                            } else {
                                Timber.d("getNewRandom success")
                                attempt = 0
                                liveData.postValue(guessResult)
                            }

                        }, { error ->
                            Timber.d(error)
                            netWorkliveData.postValue(NetworkState.error(error.message))
                            isRunning = false
                        })
            }
        }
    }


}