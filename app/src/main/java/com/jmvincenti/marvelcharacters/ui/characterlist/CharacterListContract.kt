package com.jmvincenti.marvelcharacters.ui.characterlist

import com.jmvincenti.marvelcharacters.data.model.Character


/**
 * TODO: Add a class header comment! 😘
 */
interface CharacterListContract {

    interface View {
        fun openCharacterDetail(characterId : Int)
    }

    interface Presenter<V : CharacterListContract.View> {
        fun setView(view: V)
        fun onCharacterSelected(character: Character?)
        fun handleError(throwable: Throwable)
    }


}