package com.jmvincenti.marvelcharacters.ui.characterlist.list

import android.view.View
import com.jmvincenti.marvelcharacters.data.model.Character

interface OnCharacterSelectedListener {
    fun onCharacterSelected(character: Character?, tileView: View)
}