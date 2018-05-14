package com.jmvincenti.marvelcharacters.ui.characterdetail.comics

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.jmvincenti.marvelcharacters.data.model.Comic

/**
 * TODO: Add a class header comment! 😘
 */
class  ComicAdapter : RecyclerView.Adapter<ComicViewHolder>() {
    val items  = mutableListOf<Comic>()


    fun setItem(comics: List<Comic>?) {
        items.clear()
        comics?.let { items.addAll(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicViewHolder {
        return ComicViewHolder.create(parent)
    }

    override fun getItemCount(): Int {
       return items.size
    }

    override fun onBindViewHolder(holder: ComicViewHolder, position: Int) {
        holder.bindTo(items[position])
    }
}