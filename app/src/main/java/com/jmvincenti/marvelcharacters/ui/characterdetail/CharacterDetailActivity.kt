package com.jmvincenti.marvelcharacters.ui.characterdetail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import com.bumptech.glide.Glide
import com.jmvincenti.marvelcharacters.R
import com.jmvincenti.marvelcharacters.data.model.ApiImage
import com.jmvincenti.marvelcharacters.data.model.Comic
import com.jmvincenti.marvelcharacters.data.model.Series
import com.jmvincenti.marvelcharacters.data.model.Stories
import com.jmvincenti.marvelcharacters.injection.InjectorManager
import com.jmvincenti.marvelcharacters.ui.characterdetail.comics.ComicAdapter
import com.jmvincenti.marvelcharacters.ui.characterdetail.comics.SeriesAdapter
import com.jmvincenti.marvelcharacters.ui.characterdetail.comics.StoriesAdapter
import com.jmvincenti.marvelcharacters.ui.utils.UiUtils
import com.jmvincenti.marvelcharacters.ui.utils.getLandscapePath
import kotlinx.android.synthetic.main.activity_character_detail.*


class CharacterDetailActivity : AppCompatActivity(), CharacterDetailContract.View {


    private lateinit var comicAdapter: ComicAdapter
    private lateinit var seriesAdapter: SeriesAdapter
    private lateinit var storiesAdapter: StoriesAdapter
    private lateinit var viewModel: CharacterDetailViewModel
    private lateinit var presenter: CharacterDetailContract.Presenter<CharacterDetailContract.View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        setContentView(R.layout.activity_character_detail)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initAdapters()
        viewModel = getViewModel()
        presenter = InjectorManager.getCharacterDetailPresenter()
        presenter.setView(this)
        viewModel.getCharacterLiveData().observe(this, Observer {
            presenter.handleCharacter(it)
        })
        viewModel.getErrorLiveData().observe(this, Observer {
            presenter.handleError(it)
        })
        val config = DetailConfig(
                isRandom = intent?.getBooleanExtra(INTENT_IS_RANDOM, false) ?: false,
                characterId = intent?.getIntExtra(INTENT_CHARACTER_ID, -1) ?: -1,
                characterName = intent?.getStringExtra(INTENT_CHARACTER_NAME))
        presenter.handleConfig(config)
        viewModel.loadConfig(config)

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

    override fun handleError() {
        val alertDialog = AlertDialog.Builder(this@CharacterDetailActivity).create()
        alertDialog.setTitle(R.string.detail_error_title)
        alertDialog.setMessage(getString(R.string.detail_error_message))
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(android.R.string.ok),
                { _, _ -> finish() })
        alertDialog.show()
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
        character_detail_opendetail.setOnClickListener { UiUtils.openLink(this, url) }
    }

    override fun hideWikiAction() {
        character_detail_openwiki.visibility = View.GONE
    }

    override fun initWikiAction(url: String) {
        character_detail_openwiki.visibility = View.VISIBLE
        character_detail_openwiki.setOnClickListener { UiUtils.openLink(this, url) }
    }

    override fun hideComicsAction() {
        character_detail_opencomics.visibility = View.GONE
    }

    override fun initComicsAction(url: String) {
        character_detail_opencomics.visibility = View.VISIBLE
        character_detail_opencomics.setOnClickListener { UiUtils.openLink(this, url) }
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
                val client = InjectorManager.getCharactersClient()
                val dao = InjectorManager.getCharacterDao(this@CharacterDetailActivity)
                @Suppress("UNCHECKED_CAST")
                return CharacterDetailViewModel(client, dao) as T
            }
        })[CharacterDetailViewModel::class.java]
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        return when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val INTENT_CHARACTER_ID = "intent_character_id"
        const val INTENT_CHARACTER_NAME = "intent_character_name"
        const val INTENT_IS_RANDOM = "intent_is_random"
    }
}
