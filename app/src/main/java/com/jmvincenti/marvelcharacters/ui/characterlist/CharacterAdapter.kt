package com.jmvincenti.marvelcharacters.ui.characterlist

import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.view.ViewGroup
import com.jmvincenti.marvelcharacters.data.model.Character

/**
 * TODO: Add a class header comment! 😘
 */
class CharacterAdapter : PagedListAdapter<Character, CharacterViewHolder>(DoorDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        return CharacterViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }


    companion object {
        val DoorDiffCallback = object : DiffUtil.ItemCallback<Character>() {
            override fun areItemsTheSame(oldItem: Character?, newItem: Character?): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Character?, newItem: Character?): Boolean {
                return oldItem == newItem
            }

        }
    }
}