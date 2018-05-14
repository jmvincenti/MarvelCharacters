package com.jmvincenti.marvelcharacters.ui.characterdetail

import com.jmvincenti.marvelcharacters.data.model.*


/**
 * TODO: Add a class header comment! 😘
 */
interface CharacterDetailContract {

    interface View {
        fun setTitle(title: String?)
        fun setCover(url: ApiImage)
        fun hideDescription()
        fun handleNoDescription()
        fun setDescription(desciption: String)
        fun hideComics()
        fun showComics(comics: List<Comic>)
        fun hideSeries()
        fun showSeries(series: List<Series>)
        fun hideStories()
        fun showStories(stories: List<Stories>)

        fun hideDetailAction()
        fun initDetailAction(url: String)
        fun hideWikiAction()
        fun initWikiAction(url: String)
        fun hideComicsAction()
        fun initComicsAction(url: String)

    }

    interface Presenter<V : CharacterDetailContract.View> {
        fun setView(view: V)
        fun handleCharacter(character: Character?)
        fun handleError(error: Throwable)
    }


}