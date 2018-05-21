package com.jmvincenti.marvelcharacters.ui.characterlist.list

import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.jmvincenti.marvelcharacters.data.model.Character
import timber.log.Timber

class CharacterAdapter(private var listener: OnCharacterSelectedListener?, private var tryAgainListener: OnTryAgainListener?) : PagedListAdapter<Character, RecyclerView.ViewHolder>(DoorDiffCallback) {

    var isLoading = false
        set(value) {
            Timber.d("isLoading set to $value")
            notifyDataSetChanged()
            field = value
        }
    var showTryAgain = false
        set(value) {
            Timber.d("showTryAgain set to $value")
            notifyDataSetChanged()
            field = value
        }


    fun onClear() {
        listener = null
        tryAgainListener = null
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            RequestStateViewHolder.LAYOUT_ID -> RequestStateViewHolder.create(parent, tryAgainListener)
            else -> CharacterViewHolder.create(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            CharacterViewHolder.LAYOUT_ID -> (holder as CharacterViewHolder).bindTo(getItem(position), listener)
            RequestStateViewHolder.LAYOUT_ID -> (holder as RequestStateViewHolder).bindTo(isLoading, showTryAgain)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position == itemCount - 1 && (isLoading || showTryAgain) -> RequestStateViewHolder.LAYOUT_ID
            else -> CharacterViewHolder.LAYOUT_ID
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if ((isLoading && super.getItemCount() > 0) || showTryAgain) 1 else 0
    }

    companion object {
        val DoorDiffCallback = object : DiffUtil.ItemCallback<Character>() {
            override fun areItemsTheSame(oldItem: Character?, newItem: Character?): Boolean {
                return oldItem?.id == newItem?.id
            }

            override fun areContentsTheSame(oldItem: Character?, newItem: Character?): Boolean {
                return oldItem?.id == newItem?.id
            }

        }
    }


}