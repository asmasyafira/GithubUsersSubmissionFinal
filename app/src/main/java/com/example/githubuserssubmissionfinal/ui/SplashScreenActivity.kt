package com.example.githubuserssubmissionfinal.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.githubuserssubmission.R
import com.example.githubuserssubmission.databinding.ActivitySplashScreenBinding
import com.example.githubuserssubmissionfinal.response.ScreenPreferences
import com.example.githubuserssubmissionfinal.response.dataStore
import com.example.githubuserssubmissionfinal.viewmodel.ThemeViewModel
import com.example.githubuserssubmissionfinal.viewmodel.ThemeViewModelFactory

class SplashScreenActivity : AppCompatActivity() {

    private val splashTimeOut = 3000
    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.SplashScreen)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, splashTimeOut.toLong())

        val pref = ScreenPreferences.getInstance(application.dataStore)
        val themeViewModel = ViewModelProvider(this, ThemeViewModelFactory(pref))[ThemeViewModel::class.java]

        themeViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.splashscreen.setBackgroundColor(ContextCompat.getColor(this, androidx.appcompat.R.color.primary_material_dark))
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.splashscreen.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            }
        }
    }
}