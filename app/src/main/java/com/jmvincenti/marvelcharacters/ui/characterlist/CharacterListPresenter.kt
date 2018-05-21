package com.jmvincenti.marvelcharacters.ui.characterlist

import com.jmvincenti.marvelcharacters.data.api.NetworkState
import com.jmvincenti.marvelcharacters.data.api.Status
import com.jmvincenti.marvelcharacters.data.model.Character

class CharacterListPresenter : CharacterListContract.Presenter<CharacterListContract.View> {


    override fun handleInitialState(state: NetworkState?) {
        when (state?.status) {
            Status.SUCCESS -> {
                mView?.showInitialLoadState(false)
                mView?.showTryAgain(false)
            }
            Status.RUNNING -> {
                mView?.showInitialLoadState(true)
                mView?.showTryAgain(false)
            }
            Status.FAILED -> {
                mView?.showInitialLoadState(false)
                mView?.showTryAgain(true)
            }
        }
    }

    override fun handleLoadMoreState(state: NetworkState?) {
        when (state?.status) {
            Status.SUCCESS -> {
                mView?.showLoadMoreState(false)
                mView?.showTryAgain(false)
            }
            Status.RUNNING -> {
                mView?.showLoadMoreState(true)
                mView?.showTryAgain(false)
            }
            Status.FAILED -> {
                mView?.showLoadMoreState(false)
                mView?.showTryAgain(true)
            }
        }
    }

    var mView: CharacterListContract.View? = null
    override fun setView(view: CharacterListContract.View) {
        mView = view
    }

    override fun onOpenCharacter(character: Character?): Boolean {
        return when (character) {
            null -> false
            else -> true
        }
    }
}