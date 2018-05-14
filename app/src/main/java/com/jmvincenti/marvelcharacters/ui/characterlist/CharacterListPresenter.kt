package com.jmvincenti.marvelcharacters.ui.characterlist

import com.jmvincenti.marvelcharacters.data.model.Character

/**
 * TODO: Add a class header comment! 😘
 */
class CharacterListPresenter : CharacterListContract.Presenter<CharacterListContract.View> {
    var mView: CharacterListContract.View? = null
    override fun setView(view: CharacterListContract.View) {
        mView = view
    }

    override fun onCharacterSelected(character: Character?) {
        character?.id?.let { mView?.openCharacterDetail(it) }
    }
}