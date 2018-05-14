package com.jmvincenti.marvelcharacters.ui.characterdetail.comics

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jmvincenti.marvelcharacters.R
import com.jmvincenti.marvelcharacters.data.model.Series
import kotlinx.android.synthetic.main.item_series.view.*

/**
 * TODO: Add a class header comment! 😘
 */
class SeriesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bindTo(item: Series) {
        itemView.item_series_name.text = item.name
    }

    companion object {
        fun create(parent: ViewGroup): SeriesViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(LAYOUT_ID, parent, false)
            return SeriesViewHolder(view)
        }

        const val LAYOUT_ID = R.layout.item_series
    }
}