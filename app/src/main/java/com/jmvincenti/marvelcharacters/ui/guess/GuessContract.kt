package com.jmvincenti.marvelcharacters.ui.guess

import com.jmvincenti.marvelcharacters.data.api.NetworkState
import com.jmvincenti.marvelcharacters.data.model.ApiImage

interface GuessContract {

    interface View {
        fun displayResult(answer1: String?, answer2: String?, answer3: String?, answer4: String?)
        fun updateState(state1: Boolean, state2: Boolean, state3: Boolean, state4: Boolean)
        fun startNext()
        fun setCover(path: ApiImage?)
        fun handleError()
        fun showLoader(isLoading: Boolean)
    }

    interface Presenter {
        var view  : GuessContract.View?
        fun onAttached()
        fun handleResult(guessResult: GuessResult?)
        fun onPressed(witch: Int)
        fun handleState(state: NetworkState?)
    }

}