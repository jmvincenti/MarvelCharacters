package com.jmvincenti.marvelcharacters.ui.characterdetail

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.jmvincenti.marvelcharacters.data.api.characters.CharactersClient
import com.jmvincenti.marvelcharacters.data.database.CharacterDao
import com.jmvincenti.marvelcharacters.data.model.Character
import com.jmvincenti.marvelcharacters.data.preferences.MyPreferences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class CharacterDetailViewModel(private val charactersClient: CharactersClient,
                               val characterDao: CharacterDao) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    private var character: Character? = null
    private val livedata = MutableLiveData<Character>()
    private val errorLivedata = MutableLiveData<Throwable>()

    fun getCharacterLiveData(): LiveData<Character> = livedata
    fun getErrorLiveData(): LiveData<Throwable> = errorLivedata

    fun loadConfig(config: DetailConfig) {
        if (config.isRandom) {
            loadRandomRemote()
        } else if (config.characterId != null) {
            if (livedata.value?.id == config.characterId) {
                livedata.postValue(livedata.value)
            } else {
                loadLocal(config.characterId)
            }
        }
    }


    private fun loadLocal(id: Int) {
        characterDao.loadSingle(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    character = result
                    livedata.postValue(result)
                }, { error ->
                    Timber.d(error)
                    loadRemote(id)
                })
    }

    private fun loadRemote(id: Int) {
        charactersClient.getCharacter(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    character = result?.results?.get(0)
                    livedata.postValue(character)
                }, { error ->
                    Timber.d(error)
                    errorLivedata.postValue(error)
                })
    }

    private fun loadRandomRemote() {
        charactersClient.getCharactersAsync((Math.random() * MyPreferences.maxCharacter).toInt(), 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    result?.response?.total?.let {
                        MyPreferences.maxCharacter = it
                    }
                    character = result?.response?.results?.get(0)
                    livedata.postValue(character)
                }, { error ->
                    Timber.d(error)
                    errorLivedata.postValue(error)
                })
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}