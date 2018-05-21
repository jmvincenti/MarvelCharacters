package com.jmvincenti.marvelcharacters.ui.characterlist

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.View
import com.jmvincenti.marvelcharacters.R
import com.jmvincenti.marvelcharacters.data.model.Character
import com.jmvincenti.marvelcharacters.injection.InjectorManager
import com.jmvincenti.marvelcharacters.ui.characterdetail.CharacterDetailActivity
import com.jmvincenti.marvelcharacters.ui.characterlist.list.CharacterAdapter
import com.jmvincenti.marvelcharacters.ui.characterlist.list.OnCharacterSelectedListener
import com.jmvincenti.marvelcharacters.ui.characterlist.list.OnTryAgainListener
import kotlinx.android.synthetic.main.activity_character_list.*


class CharacterListActivity : AppCompatActivity(), CharacterListContract.View, OnCharacterSelectedListener, OnTryAgainListener {


    lateinit var adapter: CharacterAdapter
    private lateinit var viewModel: CharacterListViewModel
    private var presenter: CharacterListContract.Presenter<CharacterListContract.View>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_list)
        viewModel = getViewModel()
        presenter = CharacterListPresenter()
        presenter?.setView(this)

        initAdapter()

        viewModel.characterList.observe(this, Observer { result ->
            adapter.submitList(result)
        })
        viewModel.onNewList.observe(this, Observer {
            adapter.onClear()
            initAdapter()
        })
        viewModel.loadMoreState.observe(this, Observer { state ->
            presenter?.handleLoadMoreState(state)
        })
        viewModel.initialLoadState.observe(this, Observer { state ->
            presenter?.handleInitialState(state)
        })
    }


    private fun initAdapter() {
        val linearLayoutManager = GridLayoutManager(this, 2)
        adapter = CharacterAdapter(this, this)
        recycler_characters.layoutManager = linearLayoutManager
        recycler_characters.adapter = adapter
    }

    override fun onTryAgain() {
        viewModel.onTryAgain()
    }

    override fun showTryAgain(showTryAgain: Boolean) {
        adapter.showTryAgain = showTryAgain
    }

    override fun showLoadMoreState(isLoading: Boolean) {
        adapter.isLoading = isLoading
    }

    override fun showInitialLoadState(isLoading: Boolean) {
        if (isLoading) {
            animation_view.visibility = View.VISIBLE
            recycler_characters.visibility = View.INVISIBLE
            animation_view.playAnimation()
        } else {

            animation_view.visibility = View.INVISIBLE
            recycler_characters.visibility = View.VISIBLE
            animation_view.cancelAnimation()
        }
    }

    override fun onCharacterSelected(character: Character?, tileView: View) {
        if (presenter?.onOpenCharacter(character) == true) {
            character?.let {
                val i = Intent(this, CharacterDetailActivity::class.java)
                i.putExtra(CharacterDetailActivity.INTENT_CHARACTER_ID, it.id)
                i.putExtra(CharacterDetailActivity.INTENT_CHARACTER_NAME, it.name)
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, tileView, "profile")
                startActivity(i, options.toBundle())
            }
        }
    }

    private fun getViewModel(): CharacterListViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return CharacterListViewModel(InjectorManager.getCharacterRepository(this@CharacterListActivity)) as T
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
