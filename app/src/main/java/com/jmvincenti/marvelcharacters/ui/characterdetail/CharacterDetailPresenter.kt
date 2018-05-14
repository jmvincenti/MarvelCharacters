package com.jmvincenti.marvelcharacters.ui.characterdetail

import com.jmvincenti.marvelcharacters.data.model.ApiUrl
import com.jmvincenti.marvelcharacters.data.model.Character

/**
 * TODO: Add a class header comment! 😘
 */
class CharacterDetailPresenter<V : CharacterDetailContract.View> : CharacterDetailContract.Presenter<V> {
    var mView: V? = null


    override fun setView(view: V) {
        mView = view
    }


    override fun handleCharacter(character: Character?) {

        character?.name?.let { mView?.setTitle(it) }

        character?.thumbnail?.let { mView?.setCover(it) }

        val comics = character?.comics?.items
        if (comics == null|| comics.isEmpty()) {
            mView?.hideComics()
        } else {
            mView?.showComics(comics)
        }

        val series = character?.series?.items
        if (series == null || series.isEmpty()) {
            mView?.hideSeries()
        } else {
            mView?.showSeries(series)
        }

        val stories = character?.stories?.items
        if (stories == null|| stories.isEmpty()) {
            mView?.hideStories()
        } else {
            mView?.showStories(stories)
        }

        var hasWikiLink = false
        var hasDetailLink = false
        var hasComicLink = false

        character?.urls?.forEach { url ->
            when (url.type) {
                ApiUrl.TYPE_DETAIL -> {
                    hasDetailLink = true
                    mView?.initDetailAction(url.url)
                }
                ApiUrl.TYPE_WIKI -> {
                    hasWikiLink = true
                    mView?.initWikiAction(url.url)
                }
                ApiUrl.TYPE_COMICLINK -> {
                    hasComicLink = true
                    mView?.initComicsAction(url.url)
                }
            }
        }

        if (!hasWikiLink) mView?.hideWikiAction()
        if (!hasComicLink) mView?.hideComics()
        if (!hasDetailLink) mView?.hideDetailAction()

        val description = character?.description
        val hasDescription = description != null && description.isNotBlank()
        when {
            !hasWikiLink && !hasComicLink && !hasDetailLink && !hasDescription -> mView?.hideDescription()
            !hasDescription -> mView?.handleNoDescription()
            else -> mView?.setDescription(description!!)
        }

    }

    override fun handleError(error: Throwable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}