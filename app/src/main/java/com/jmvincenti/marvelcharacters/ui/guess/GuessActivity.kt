package com.jmvincenti.marvelcharacters.ui.guess

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import com.bumptech.glide.Glide
import com.jmvincenti.marvelcharacters.R
import com.jmvincenti.marvelcharacters.data.api.characters.CharactersClient
import com.jmvincenti.marvelcharacters.data.model.ApiImage
import com.jmvincenti.marvelcharacters.injection.InjectorManager
import com.jmvincenti.marvelcharacters.ui.utils.getLandscapePath
import com.jmvincenti.marvelcharacters.ui.utils.getPortraitLargePath
import kotlinx.android.synthetic.main.activity_guess.*

class GuessActivity : AppCompatActivity(), GuessContract.View {


    lateinit var guessViewModel: GuessViewModel
    var presenter: GuessContract.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guess)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        guessViewModel = getViewModel()
        presenter = guessViewModel.guessPresenter
        if (presenter == null) {
            presenter = GuessPresenter()

        }
        presenter?.view = this
        guessViewModel.getCharacterLiveData().observe(this, Observer {
            presenter?.handleResult(it)
        })
        guessViewModel.getStateLiveData().observe(this, Observer {
            presenter?.handleState(it)
        })
        guess_answer1.setOnClickListener {
            presenter?.onPressed(1)
        }
        guess_answer2.setOnClickListener {
            presenter?.onPressed(2)
        }
        guess_answer3.setOnClickListener {
            presenter?.onPressed(3)
        }
        guess_answer4.setOnClickListener {
            presenter?.onPressed(4)
        }
        presenter?.onAttached()
    }

    override fun onDestroy() {
        super.onDestroy()
        guessViewModel.guessPresenter = presenter
        presenter?.view = null
        presenter = null
    }

    override fun startNext() {
        guessViewModel.getNewRandom()
    }

    override fun showLoader(isLoading: Boolean) {
        when (isLoading) {
            true -> {
                animation_view.playAnimation()
            }
            false -> {
                animation_view.pauseAnimation()
            }
        }

    }

    override fun setCover(path: ApiImage?) {
        if (path == null) {
            image.visibility = View.INVISIBLE
        } else {
            image.visibility = View.VISIBLE
            Glide.with(this)
                    .load(if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) path.getPortraitLargePath(this)
                    else path.getLandscapePath(
                            this))
                    .into(image)
        }
    }

    override fun handleError() {
        val alertDialog = AlertDialog.Builder(this@GuessActivity).create()
        alertDialog.setTitle(R.string.detail_error_title)
        alertDialog.setMessage(getString(R.string.detail_error_message))
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(android.R.string.ok),
                { _, _ -> guessViewModel.getNewRandom() })
        alertDialog.show()
    }

    override fun displayResult(answer1: String?, answer2: String?, answer3: String?, answer4: String?) {
        guess_answer1.text = answer1
        guess_answer2.text = answer2
        guess_answer3.text = answer3
        guess_answer4.text = answer4
    }

    override fun updateState(state1: Boolean, state2: Boolean, state3: Boolean, state4: Boolean) {
        guess_answer1.isEnabled = state1
        guess_answer2.isEnabled = state2
        guess_answer3.isEnabled = state3
        guess_answer4.isEnabled = state4
    }

    private fun getViewModel(): GuessViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val client = CharactersClient(InjectorManager.getRetrofitClient())
                @Suppress("UNCHECKED_CAST")
                return GuessViewModel(client) as T
            }
        })[GuessViewModel::class.java]
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        return when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
