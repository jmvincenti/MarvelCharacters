package com.jmvincenti.marvelcharacters.ui.characterlist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jmvincenti.marvelcharacters.R
import com.jmvincenti.marvelcharacters.data.model.Character
import kotlinx.android.synthetic.main.item_character.view.*

/**
 * TODO: Add a class header comment! 😘
 */
class CharacterViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bindTo(character: Character?) {

        itemView.character_item_name.text = character?.name
//        itemView.character_item_description.text = character?.description
//        itemView.character_item_image.text = character?.description
    }


    companion object {
        fun create(parent: ViewGroup): CharacterViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(LAYOUT_ID, parent, false)
            return CharacterViewHolder(view)
        }

        const val LAYOUT_ID = R.layout.item_character
    }
}