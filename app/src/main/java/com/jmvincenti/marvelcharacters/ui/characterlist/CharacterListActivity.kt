package com.jmvincenti.marvelcharacters.ui.characterlist

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.Menu
import com.jmvincenti.marvelcharacters.R
import com.jmvincenti.marvelcharacters.data.api.characters.CharactersClient
import com.jmvincenti.marvelcharacters.data.repository.CharactersDataSourceFactory
import com.jmvincenti.marvelcharacters.ui.characterdetail.CharacterDetailActivity
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_character_list.*
import java.util.concurrent.Executors

class CharacterListActivity : AppCompatActivity(), CharacterListContract.View {


    lateinit var adapter: CharacterAdapter
    private lateinit var viewModel: CharacterListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_list)
        viewModel = getViewModel()
        if (viewModel.presenter == null) {
            viewModel.presenter = CharacterListPresenter()
        }
        viewModel.presenter?.setView(this)
        initAdapter()
        viewModel.characterList.observe(this, Observer{ result ->
            adapter.submitList(result)
        })
    }

    private fun initAdapter() {
//        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val linearLayoutManager = GridLayoutManager(this, 2)
        adapter = CharacterAdapter(viewModel.presenter)
        recycler_characters.layoutManager = linearLayoutManager
        recycler_characters.adapter = adapter
    }



    override fun openCharacterDetail(characterId: Int) {
        val i = Intent(this, CharacterDetailActivity::class.java)
        i.putExtra(CharacterDetailActivity.INTENT_CHARACTER_ID, characterId)
        startActivity(i)
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.character_list_menu, menu)
        // Retrieve the SearchView and plug it into SearchManager
        val searchView = MenuItemCompat.getActionView(menu.findItem(R.id.action_search)) as SearchView
        searchView.setOnCloseListener {
            viewModel.applyFilter(null)
            true
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.applyFilter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.applyFilter(newText)
                return true
            }

        })
        return true
    }
}
