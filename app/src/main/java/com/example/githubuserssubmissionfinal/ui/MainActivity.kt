package com.example.githubuserssubmissionfinal.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserssubmission.R
import com.example.githubuserssubmission.databinding.ActivityMainBinding
import com.example.githubuserssubmissionfinal.response.Users
import com.example.githubuserssubmissionfinal.adapter.ListUserAdapter
import com.example.githubuserssubmissionfinal.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var listUserAdapter: ListUserAdapter
    private var dataUser = listOf<Users>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){
            val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
            searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
            searchView.queryHint = getString(R.string.hintForSearch)
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let {
                        mainViewModel.findSearchUser(it)
                    }
                    return true
                }
                override fun onQueryTextChange(newText: String?): Boolean {
                    return true
                }
            })
        }

        listUserAdapter = ListUserAdapter(dataUser)
        val layoutManager = LinearLayoutManager(this)
        binding.mainRv.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.mainRv.addItemDecoration(itemDecoration)

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()
        )[MainViewModel::class.java]

        mainViewModel.user.observe(this) { user ->
            dataUser = user
            setDataListUser(dataUser)
        }

        mainViewModel.mainLoading.observe(this) {
            onLoading(it)
        }

        mainViewModel.userList.observe(this@MainActivity) { userList ->
            setDataListUser(userList)
        }
    }

    private fun setDataListUser(githubList: List<Users>) {
        listUserAdapter = ListUserAdapter(githubList)
        binding.mainRv.apply {
            adapter = listUserAdapter
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)

            listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
                override fun onItemClicked(data: Users) {
                    clickToDetail(data)
                }
            })
        }
    }

    private fun clickToDetail(data: Users) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.DATA_USER, data)
        startActivity(intent)
    }

    private fun onLoading(loading: Boolean) {
        binding.mainProgressBar.visibility = if (loading) View.VISIBLE else View.GONE
        if (loading) resetListUser()
    }

    private fun resetListUser() {
        with(binding) {
            mainRv.adapter = null
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_screen -> {
                val intent = Intent(this, ThemeActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.action_favorite -> {
                val intent = Intent(this, FavoriteActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
}