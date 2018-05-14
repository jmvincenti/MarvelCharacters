package com.jmvincenti.marvelcharacters.ui.characterdetail

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.bumptech.glide.Glide
import com.jmvincenti.marvelcharacters.R
import com.jmvincenti.marvelcharacters.data.api.characters.CharactersClient
import com.jmvincenti.marvelcharacters.ui.Utils
import com.jmvincenti.marvelcharacters.ui.characterdetail.comics.ComicAdapter
import com.jmvincenti.marvelcharacters.ui.characterdetail.comics.SeriesAdapter
import com.jmvincenti.marvelcharacters.ui.characterdetail.comics.StoriesAdapter
import com.jmvincenti.marvelcharacters.ui.getPortraitLargePath
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_character_detail.*

class CharacterDetailActivity : AppCompatActivity() {

    private lateinit var comicAdapter: ComicAdapter
    private lateinit var seriesAdapter: SeriesAdapter
    private lateinit var storiesAdapter: StoriesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        setContentView(R.layout.activity_character_detail)
        initAdapters()
    }

    override fun onResume() {
        super.onResume()
        val client = CharactersClient()
        client.getCharacter(1009144)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    toolbar.title = result?.name
                    Glide.with(this)
                            .load(result?.thumbnail?.getPortraitLargePath(this))
                            .into(character_detail_image)
                    character_detail_description.text = result?.description
                    comicAdapter.setItem(result?.comics?.items)
                    seriesAdapter.setItem(result?.series?.items)
                    storiesAdapter.setItem(result?.stories?.items)

                    result?.urls?.forEach { url ->
                        when (url.type) {
                            "detail" -> {
                                character_detail_opendetail.visibility = View.VISIBLE
                                character_detail_opendetail.setOnClickListener {
                                    Utils.openLink(this, url.url)
                                }
                            }
                            "wiki" -> {
                                character_detail_openwiki.visibility = View.VISIBLE
                                character_detail_openwiki.setOnClickListener {
                                    Utils.openLink(this, url.url)
                                }
                            }
                            "comiclink" -> {
                                character_detail_opencomics.visibility = View.VISIBLE
                                character_detail_opencomics.setOnClickListener {
                                    Utils.openLink(this, url.url)
                                }
                            }
                        }
                    }

                },

                        { error ->
                        })
    }

    private fun initAdapters() {
        character_detail_comics.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        character_detail_comics.setHasFixedSize(true)
        comicAdapter = ComicAdapter()
        character_detail_comics.adapter = comicAdapter

        character_detail_series.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        character_detail_series.setHasFixedSize(true)
        seriesAdapter = SeriesAdapter()
        character_detail_series.adapter = seriesAdapter

        character_detail_stories.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        character_detail_stories.setHasFixedSize(true)
        storiesAdapter = StoriesAdapter()
        character_detail_stories.adapter = storiesAdapter
    }
}
