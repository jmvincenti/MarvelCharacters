package com.jmvincenti.marvelcharacters.ui.guess

import com.jmvincenti.marvelcharacters.data.model.Character
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.*

class GuessContractTest {

    var guessContract: GuessContract.View? = Mockito.mock(GuessContract.View::class.java)

    @Test
    fun testHandleResult() {
        val presenter = GuessPresenter()
        val results = listOf(Character(id = 1000, name = "name1"), Character(id = 1001, name = "name2"), Character(id = 1003, name = "name3"), Character(id = 1004, name = "name4"))
        presenter.view = guessContract
        presenter.handleResult(GuessResult(results[0], results))
        assert(presenter.targetId == 1001)
        verify(guessContract, times(1))!!.updateState(true, true, true, true)
        verify(guessContract, times(1))!!.displayResult("name1", "name2", "name3", "name4")
        verify(guessContract, times(1))!!.showLoader(false)
    }

    @Test
    fun testOnPressed() {
        val presenter = GuessPresenter()
        val results = listOf(Character(id = 1000, name = "name1"), Character(id = 1001, name = "name2"), Character(id = 1003, name = "name3"), Character(id = 1004, name = "name4"))
        presenter.view = guessContract
        presenter.handleResult(GuessResult(results[3], results))

        verify(guessContract, times(1))!!.updateState(true, true, true, true)
        presenter.onPressed(1)
        verify(guessContract, times(1))!!.updateState(false, true, true, true)
        presenter.onPressed(2)
        verify(guessContract, times(1))!!.updateState(false, false, true, true)
        presenter.onPressed(3)
        verify(guessContract, times(1))!!.updateState(false, false, false, true)

        verify(guessContract, times(1))!!.showLoader(false)
        verify(guessContract, times(1))!!.setCover(null)
        verify(guessContract, never())!!.startNext()

        presenter.onPressed(4)
        verify(guessContract, times(1))!!.showLoader(true)
        verify(guessContract, times(2))!!.setCover(null)
        verify(guessContract, times(1))!!.startNext()
        verify(guessContract, times(1))!!.updateState(false, false, false, false)

    }
}