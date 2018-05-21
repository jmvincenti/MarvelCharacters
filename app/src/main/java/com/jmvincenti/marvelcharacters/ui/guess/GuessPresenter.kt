package com.jmvincenti.marvelcharacters.ui.guess

class GuessPresenter : GuessContract.Presenter {


    override var view: GuessContract.View? = null


    var state1 = true
    var state2 = true
    var state3 = true
    var state4 = true
    var guessResult: GuessResult? = null
    var targetId: Int? = null

    override fun onAttached() {
        view?.updateState(state1, state2, state3, state4)
        view?.setCover(guessResult?.target?.thumbnail)
        if (targetId==null) {
            view?.showLoader(true)
            view?.startNext()
        }
    }


    override fun handleResult(guessResult: GuessResult?) {
        this.guessResult = guessResult
        if (guessResult != null) {
            view?.showLoader(false)
            view?.displayResult(
                    answer1 = guessResult.characters?.get(0)?.name,
                    answer2 = guessResult.characters?.get(1)?.name,
                    answer3 = guessResult.characters?.get(2)?.name,
                    answer4 = guessResult.characters?.get(3)?.name)
            view?.setCover(guessResult.target?.thumbnail)
            state1 = true
            state2 = true
            state3 = true
            state4 = true
            targetId = guessResult.target?.id
            view?.updateState(state1, state2, state3, state4)
        }
    }

    override fun onPressed(witch: Int) {
        if (targetId == guessResult?.characters?.get(witch-1)?.id) {
            view?.showLoader(true)
            view?.setCover(null)
            view?.startNext()
            view?.updateState(false, false, false, false)
            guessResult = null
            targetId = null
        } else {
            when (witch) {
                1 -> state1 = guessResult?.target?.id == guessResult?.characters?.get(0)?.id
                2 -> state2 = guessResult?.target?.id == guessResult?.characters?.get(1)?.id
                3 -> state3 = guessResult?.target?.id == guessResult?.characters?.get(2)?.id
                4 -> state4 = guessResult?.target?.id == guessResult?.characters?.get(3)?.id
            }
            view?.updateState(state1, state2, state3, state4)
        }
    }

    override fun handleError(throwable: Throwable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}