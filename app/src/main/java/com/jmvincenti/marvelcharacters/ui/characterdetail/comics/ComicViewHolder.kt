package com.jmvincenti.marvelcharacters.ui.characterdetail.comics

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jmvincenti.marvelcharacters.R
import com.jmvincenti.marvelcharacters.data.model.Comic
import kotlinx.android.synthetic.main.item_comic.view.*

/**
 * TODO: Add a class header comment! 😘
 */
class ComicViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bindTo(comic: Comic) {
        itemView.item_comic_name.text = comic.name
    }

    companion object {
        fun create(parent: ViewGroup): ComicViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(LAYOUT_ID, parent, false)
            return ComicViewHolder(view)
        }
        const val LAYOUT_ID = R.layout.item_comic
    }
}