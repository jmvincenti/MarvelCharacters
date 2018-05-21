package com.jmvincenti.marvelcharacters.ui

import android.content.Intent
import android.os.Bundle
import android.support.constraint.ConstraintSet
import android.support.v7.app.AppCompatActivity
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.view.animation.AccelerateDecelerateInterpolator
import com.jmvincenti.marvelcharacters.R
import com.jmvincenti.marvelcharacters.ui.characterdetail.CharacterDetailActivity
import com.jmvincenti.marvelcharacters.ui.characterlist.CharacterListActivity
import com.jmvincenti.marvelcharacters.ui.guess.GuessActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_hide)

        main_random_card.setOnClickListener {
            val i = Intent(this, CharacterDetailActivity::class.java)
            i.putExtra(CharacterDetailActivity.INTENT_IS_RANDOM, true)
            startActivity(i)
        }

        main_list_card.setOnClickListener {
            val i = Intent(this, CharacterListActivity::class.java)
            startActivity(i)
        }

        main_guess_card.setOnClickListener {
            val i = Intent(this, GuessActivity::class.java)
            startActivity(i)
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        animateHeroes()
    }


    private fun animateHeroes() {
        val constraintSet = ConstraintSet()
        constraintSet.clone(this, R.layout.activity_main)
        val transition = ChangeBounds()
        transition.interpolator = AccelerateDecelerateInterpolator()
        transition.duration = resources.getInteger(android.R.integer.config_longAnimTime).toLong()
        transition.startDelay = 0
        TransitionManager.beginDelayedTransition(constraint, transition)
        constraintSet.applyTo(constraint)
    }
}
