package com.jmvincenti.marvelcharacters.ui.characterlist.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jmvincenti.marvelcharacters.R
import kotlinx.android.synthetic.main.item_network_state.view.*

class RequestStateViewHolder(val view: View, private val retryCallback: OnTryAgainListener?) : RecyclerView.ViewHolder(view) {
    init {
        itemView.retryLoadingButton.setOnClickListener { retryCallback?.onTryAgain() }
    }

    fun bindTo(showLoading: Boolean, showTryAgain: Boolean) {
        //error message
        itemView.networkStateCard.visibility = when {
            showTryAgain -> View.VISIBLE
            else -> View.GONE
        }

        //loading and retry
        itemView.loadingProgressBar.visibility = when {
            showLoading -> View.VISIBLE
            else -> View.GONE
        }
    }

    companion object {
        fun create(parent: ViewGroup, retryCallback: OnTryAgainListener?): RequestStateViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(LAYOUT_ID, parent, false)
            return RequestStateViewHolder(view, retryCallback)
        }

        const val LAYOUT_ID = R.layout.item_network_state
    }
}