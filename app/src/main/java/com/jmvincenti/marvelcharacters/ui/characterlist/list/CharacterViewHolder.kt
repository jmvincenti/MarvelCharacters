package com.jmvincenti.marvelcharacters.ui.characterlist.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.jmvincenti.marvelcharacters.R
import com.jmvincenti.marvelcharacters.data.model.Character
import com.jmvincenti.marvelcharacters.ui.utils.getLandscapePath
import kotlinx.android.synthetic.main.item_character.view.*

class CharacterViewHolder(view: View) : RecyclerView.ViewHolder(view) {


    fun bindTo(character: Character?, listener: OnCharacterSelectedListener?) {
        itemView.character_item_overlay.setOnClickListener {
            listener?.onCharacterSelected(character, itemView.character_item_name)
        }
        itemView.character_item_name.text = character?.name
        Glide.with(itemView.context)
                .load(character?.thumbnail?.getLandscapePath(itemView.context))
                .into(itemView.character_item_image)
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