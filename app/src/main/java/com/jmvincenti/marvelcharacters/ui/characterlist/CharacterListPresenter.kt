package com.jmvincenti.marvelcharacters.ui.characterlist

import com.jmvincenti.marvelcharacters.data.model.Character

/**
 * TODO: Add a class header comment! 😘
 */
class CharacterListPresenter : CharacterListContract.Presenter<CharacterListContract.View> {
    override fun handleError(throwable: Throwable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    var mView: CharacterListContract.View? = null
    override fun setView(view: CharacterListContract.View) {
        mView = view
    }

    override fun onCharacterSelected(character: Character?) {
        character?.id?.let { mView?.openCharacterDetail(it) }
    }
}