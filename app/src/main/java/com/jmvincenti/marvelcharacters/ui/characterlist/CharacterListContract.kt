package com.jmvincenti.marvelcharacters.ui.characterlist

import com.jmvincenti.marvelcharacters.data.api.NetworkState
import com.jmvincenti.marvelcharacters.data.model.Character


interface CharacterListContract {

    interface View {
        fun showInitialLoadState(isLoading: Boolean)
        fun showTryAgain(showTryAgain: Boolean)
        fun showLoadMoreState(isLoading: Boolean)
    }

    interface Presenter<V : CharacterListContract.View> {
        fun setView(view: V)
        fun handleInitialState(state: NetworkState?)
        fun handleLoadMoreState(state: NetworkState?)
        fun onOpenCharacter(character: Character?): Boolean
    }


}