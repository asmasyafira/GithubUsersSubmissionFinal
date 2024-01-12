package com.example.githubuserssubmissionfinal.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserssubmissionfinal.viewmodel.FavoriteViewModelFactory
import com.example.githubuserssubmission.R
import com.example.githubuserssubmissionfinal.adapter.ListUserAdapter
import com.example.githubuserssubmission.databinding.ActivityFavoriteBinding
import com.example.githubuserssubmissionfinal.response.Users
import com.example.githubuserssubmissionfinal.viewmodel.FavoriteViewModel

class FavoriteActivity : AppCompatActivity() {
    private lateinit var _activityFavBinding: ActivityFavoriteBinding
    private val binding get() = _activityFavBinding
    private var dataUser = listOf<Users>()
    private lateinit var listUserAdapter: ListUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.SplashScreen)
        _activityFavBinding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val favViewModel = obtainViewModel(this)
        favViewModel.getAllFav().observe(this) { listUser ->
            if (listUser.isNotEmpty()) {
                setDataListUser(listUser)
                binding.txtNofav.visibility = View.GONE
            } else {
                binding.rvFav.visibility = View.GONE
                binding.txtNofav.visibility = View.VISIBLE
            }
        }

        listUserAdapter = ListUserAdapter(dataUser)

        binding.rvFav.layoutManager = LinearLayoutManager(this)
        binding.rvFav.setHasFixedSize(true)
        binding.rvFav.adapter = listUserAdapter
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = FavoriteViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteViewModel::class.java]
    }

    private fun setDataListUser(githubList: List<Users>) {
        listUserAdapter = ListUserAdapter(githubList)
        binding.rvFav.apply {
            adapter = listUserAdapter
            layoutManager =
                LinearLayoutManager(this@FavoriteActivity, LinearLayoutManager.VERTICAL, false)

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
}