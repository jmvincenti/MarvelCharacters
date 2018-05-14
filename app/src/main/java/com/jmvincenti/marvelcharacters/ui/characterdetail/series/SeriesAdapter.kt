package com.jmvincenti.marvelcharacters.ui.characterdetail.comics

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.jmvincenti.marvelcharacters.data.model.Series

/**
 * TODO: Add a class header comment! 😘
 */
class  SeriesAdapter : RecyclerView.Adapter<SeriesViewHolder>() {
    val items  = mutableListOf<Series>()

    fun setItem(comics: List<Series>?) {
        items.clear()
        comics?.let { items.addAll(it) }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeriesViewHolder {
        return SeriesViewHolder.create(parent)
    }

    override fun getItemCount(): Int {
       return items.size
    }

    override fun onBindViewHolder(holder: SeriesViewHolder, position: Int) {
        holder.bindTo(items[position])
    }
}