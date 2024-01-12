package com.example.githubuserssubmissionfinal.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.githubuserssubmissionfinal.viewmodel.FavoriteViewModelFactory
import com.example.githubuserssubmission.R
import com.example.githubuserssubmissionfinal.adapter.DetailPagerAdapter
import com.example.githubuserssubmission.databinding.ActivityDetailBinding
import com.example.githubuserssubmissionfinal.response.DetailUserResponse
import com.example.githubuserssubmissionfinal.response.Users
import com.example.githubuserssubmissionfinal.viewmodel.DetailViewModel
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel
    private var fav : Boolean = false

    private fun obtainViewModel(detailActivity: DetailActivity): DetailViewModel {
        val factory = FavoriteViewModelFactory.getInstance(detailActivity.application)
        return ViewModelProvider(detailActivity, factory)[DetailViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.SplashScreen)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val detailPagerAdapter = DetailPagerAdapter(this)
        binding.viewPagerDetail.adapter = detailPagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPagerDetail) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f

        detailViewModel = obtainViewModel(this)

        val user = intent.getParcelableExtra<Users>(DATA_USER) as Users
        detailViewModel.dataUser = user.login.toString()

        detailViewModel.userdetails.observe(this) {
            onSuccessDetail(it)
        }

        detailViewModel.isLoading.observe(this) {
            onLoading(it)
        }

        detailViewModel.allList().observe(this){
            fav = it.contains(user)
            if (fav) {
                binding.fabFav.setImageDrawable(resources.getDrawable(R.drawable.ic_favorite))
            } else {
                binding.fabFav.setImageDrawable(resources.getDrawable(R.drawable.ic_favorite_border))
            }
        }

        binding.fabFav.setOnClickListener {
            if (fav) {
                detailViewModel.delete(user)
            } else {
                detailViewModel.insert(user)
                startActivity(Intent(this, FavoriteActivity::class.java))
            }
        }
    }

    private fun onLoading(isLoading: Boolean) {
        binding.detailProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        if (isLoading) resetDetailUser()
    }

    private fun onSuccessDetail(detailuser: DetailUserResponse) {
        with(binding){
            fullNameDetail.text = detailuser.name ?: " - "
            userNameDetail.text = detailuser.login ?: " - "
            locationDetail.text = detailuser.location ?: ""
            followersUser.text = StringBuilder().append(detailuser.followers).append(" Followers")
            followingsUser.text = StringBuilder().append(detailuser.following).append(" Followings")
            Glide.with(this@DetailActivity)
                .load(detailuser.avatarUrl)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(binding.imgProfileDetail)
        }
    }

    private fun resetDetailUser() {
        with(binding) {
            fullNameDetail.text = ""
            userNameDetail.text = ""
            locationDetail.text = ""
            followersUser.text = ""
            followingsUser.text = ""

            Glide.with(this@DetailActivity)
                .load(R.drawable.ic_launcher_foreground)
                .into(imgProfileDetail)
        }
    }

    companion object {
        const val DATA_USER = "data_user"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.title_1,
            R.string.title_2
        )
    }
}