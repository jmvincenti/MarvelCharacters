package com.jmvincenti.marvelcharacters.ui.characterdetail.comics

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jmvincenti.marvelcharacters.R
import com.jmvincenti.marvelcharacters.data.model.Stories
import kotlinx.android.synthetic.main.item_stories.view.*

/**
 * TODO: Add a class header comment! 😘
 */
class StoriesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bindTo(item: Stories) {
        itemView.item_stories_name.text = item.name
    }

    companion object {
        fun create(parent: ViewGroup): StoriesViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(LAYOUT_ID, parent, false)
            return StoriesViewHolder(view)
        }

        const val LAYOUT_ID = R.layout.item_stories
    }
}