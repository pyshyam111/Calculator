package com.shyam.calculator

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.shyam.calculator.databinding.ActivityChangeThemeBinding

class ChangeThemeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangeThemeBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityChangeThemeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("DarkThemePrefs", Context.MODE_PRIVATE)

        // Apply saved theme before setting up UI
        applySavedTheme()

        // Set switch state from preference
        val isDark = sharedPreferences.getBoolean("switch", false)
        binding.mySwitch.isChecked = isDark

        // Toolbar back action
        binding.toolbar2.setNavigationOnClickListener {
            finish()
        }

        // Switch listener
        binding.mySwitch.setOnCheckedChangeListener { _, isChecked ->
            val editor = sharedPreferences.edit()
            editor.putBoolean("switch", isChecked)
            editor.apply()

            // Apply theme and recreate to reflect change
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }

            // Recreate activity to apply theme immediately
            recreate()
        }
    }

    private fun applySavedTheme() {
        val isDark = getSharedPreferences("DarkThemePrefs", Context.MODE_PRIVATE)
            .getBoolean("switch", false)

        AppCompatDelegate.setDefaultNightMode(
            if (isDark) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}
