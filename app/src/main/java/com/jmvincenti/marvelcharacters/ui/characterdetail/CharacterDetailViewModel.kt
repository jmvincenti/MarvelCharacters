package com.jmvincenti.marvelcharacters.ui.characterdetail

import android.arch.lifecycle.ViewModel
import com.jmvincenti.marvelcharacters.data.api.characters.CharactersClient
import com.jmvincenti.marvelcharacters.data.model.Character
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * TODO: Add a class header comment! 😘
 */
class CharacterDetailViewModel(private val charactersClient: CharactersClient) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    private var character: Character? = null
    var presenter: CharacterDetailContract.Presenter<CharacterDetailContract.View>? = null

    fun loadCharacter(characterId: Int) {
        if (character?.id == characterId) {
            presenter?.handleCharacter(character)
        } else {
            charactersClient.getCharacter(characterId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ result ->
                        character = result
                        presenter?.handleCharacter(character)
                    }, { error ->
                        presenter?.handleError(error)
                    })
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}