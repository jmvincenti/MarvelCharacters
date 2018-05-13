package com.jmvincenti.marvelcharacters.ui.characterlist

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.jmvincenti.marvelcharacters.R
import com.jmvincenti.marvelcharacters.data.api.characters.CharactersClient
import com.jmvincenti.marvelcharacters.data.repository.CharactersDataSourceFactory
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_character_list.*
import java.util.concurrent.Executors

class CharacterListActivity : AppCompatActivity() {
    lateinit var adapter: CharacterAdapter
    private lateinit var viewModel: CharacterListViewModel

    private val disposable = CompositeDisposable()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_list)
        viewModel = getViewModel()
        initAdapter()
    }

    private fun initAdapter() {
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = CharacterAdapter()
        recycler_characters.layoutManager = linearLayoutManager
        recycler_characters.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        disposable.add(viewModel.characterList.subscribe({ flowableList ->
            adapter.submitList(flowableList)
        }))
    }

    override fun onStop() {
        super.onStop()
        disposable.clear()
    }

    private fun getViewModel(): CharacterListViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val client = CharactersClient()
                val repo = CharactersDataSourceFactory(client, Executors.newFixedThreadPool(5))
                @Suppress("UNCHECKED_CAST")
                return CharacterListViewModel(repo) as T
            }
        })[CharacterListViewModel::class.java]
    }
}
