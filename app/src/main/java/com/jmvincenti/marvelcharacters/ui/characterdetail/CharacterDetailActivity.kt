package com.jmvincenti.marvelcharacters.ui.characterdetail

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.bumptech.glide.Glide
import com.jmvincenti.marvelcharacters.R
import com.jmvincenti.marvelcharacters.data.api.characters.CharactersClient
import com.jmvincenti.marvelcharacters.data.model.ApiImage
import com.jmvincenti.marvelcharacters.data.model.Comic
import com.jmvincenti.marvelcharacters.data.model.Series
import com.jmvincenti.marvelcharacters.data.model.Stories
import com.jmvincenti.marvelcharacters.ui.Utils
import com.jmvincenti.marvelcharacters.ui.characterdetail.comics.ComicAdapter
import com.jmvincenti.marvelcharacters.ui.characterdetail.comics.SeriesAdapter
import com.jmvincenti.marvelcharacters.ui.characterdetail.comics.StoriesAdapter
import com.jmvincenti.marvelcharacters.ui.getLandscapePath
import kotlinx.android.synthetic.main.activity_character_detail.*

class CharacterDetailActivity : AppCompatActivity(), CharacterDetailContract.View {


    private lateinit var comicAdapter: ComicAdapter
    private lateinit var seriesAdapter: SeriesAdapter
    private lateinit var storiesAdapter: StoriesAdapter
    private lateinit var viewModel: CharacterDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        setContentView(R.layout.activity_character_detail)
        initAdapters()
        viewModel = getViewModel()
        if (viewModel.presenter == null) {
            viewModel.presenter = CharacterDetailPresenter() //TODO injection
        }
        viewModel.presenter?.setView(this)
        val characterId = intent?.getIntExtra(INTENT_CHARACTER_ID, -1) ?: -1
        if (characterId == -1) {
            TODO()
        } else {
            viewModel.loadCharacter(characterId)
        }
    }

    override fun setName(title: String?) {
        toolbar.title = title
        character_detail_name.text = title
    }

    override fun setCover(url: ApiImage) {
        Glide.with(this)
                .load(url.getLandscapePath(this))
                .into(character_detail_image)
    }

    override fun setDescription(desciption: String) {
        character_detail_description.visibility = View.VISIBLE
        character_detail_description.text = desciption
    }

    override fun hideDescription() {
        character_detail_description.visibility = View.GONE
    }

    override fun handleNoDescription() {
        character_detail_description.visibility = View.VISIBLE
        character_detail_description.setText(R.string.no_description)
    }

    override fun hideComics() {
        character_detail_comics.visibility = View.GONE
        character_detail_comics_title.visibility = View.GONE
    }

    override fun showComics(comics: List<Comic>) {
        character_detail_comics.visibility = View.VISIBLE
        character_detail_comics_title.visibility = View.VISIBLE
        comicAdapter.setItem(comics)
        comicAdapter.notifyDataSetChanged()
    }

    override fun hideSeries() {
        character_detail_series.visibility = View.GONE
        character_detail_series_title.visibility = View.GONE
    }

    override fun showSeries(series: List<Series>) {
        character_detail_series.visibility = View.VISIBLE
        character_detail_series_title.visibility = View.VISIBLE
        seriesAdapter.setItem(series)
        seriesAdapter.notifyDataSetChanged()
    }

    override fun hideStories() {
        character_detail_stories.visibility = View.GONE
        character_detail_stories_title.visibility = View.GONE
    }

    override fun showStories(stories: List<Stories>) {
        character_detail_stories.visibility = View.VISIBLE
        character_detail_stories_title.visibility = View.VISIBLE
        storiesAdapter.setItem(stories)
        storiesAdapter.notifyDataSetChanged()
    }

    override fun hideDetailAction() {
        character_detail_opendetail.visibility = View.GONE
    }

    override fun initDetailAction(url: String) {
        character_detail_opendetail.visibility = View.VISIBLE
        character_detail_opendetail.setOnClickListener { Utils.openLink(this, url) }
    }

    override fun hideWikiAction() {
        character_detail_openwiki.visibility = View.GONE
    }

    override fun initWikiAction(url: String) {
        character_detail_openwiki.visibility = View.VISIBLE
        character_detail_openwiki.setOnClickListener { Utils.openLink(this, url) }
    }

    override fun hideComicsAction() {
        character_detail_opencomics.visibility = View.GONE
    }

    override fun initComicsAction(url: String) {
        character_detail_opencomics.visibility = View.VISIBLE
        character_detail_opencomics.setOnClickListener { Utils.openLink(this, url) }
    }

    private fun initAdapters() {
        character_detail_comics.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        comicAdapter = ComicAdapter()
        character_detail_comics.adapter = comicAdapter

        character_detail_series.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        seriesAdapter = SeriesAdapter()
        character_detail_series.adapter = seriesAdapter

        character_detail_stories.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        storiesAdapter = StoriesAdapter()
        character_detail_stories.adapter = storiesAdapter
    }

    private fun getViewModel(): CharacterDetailViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val client = CharactersClient()
                @Suppress("UNCHECKED_CAST")
                return CharacterDetailViewModel(client) as T
            }
        })[CharacterDetailViewModel::class.java]
    }

    companion object {
        const val INTENT_CHARACTER_ID = "intent_character_id"
    }
}
