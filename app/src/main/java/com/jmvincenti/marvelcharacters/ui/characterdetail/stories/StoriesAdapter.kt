package com.jmvincenti.marvelcharacters.ui.characterdetail.comics

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.jmvincenti.marvelcharacters.data.model.Stories

class StoriesAdapter : RecyclerView.Adapter<StoriesViewHolder>() {
    val items = mutableListOf<Stories>()

    fun setItem(comics: List<Stories>?) {
        items.clear()
        comics?.let { items.addAll(it) }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoriesViewHolder {
        return StoriesViewHolder.create(parent)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: StoriesViewHolder, position: Int) {
        holder.bindTo(items[position])
    }
}