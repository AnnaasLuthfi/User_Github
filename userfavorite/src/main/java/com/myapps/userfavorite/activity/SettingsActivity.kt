package com.myapps.userfavorite.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import com.myapps.userfavorite.R
import com.myapps.userfavorite.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Settings"

        binding.changeLanguage.setOnClickListener(this)
        binding.reminderAlarm.setOnClickListener(this)

    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.changeLanguage -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }

            R.id.reminder_alarm -> {
                startActivity(Intent(this@SettingsActivity, ReminderActivity::class.java))
            }
        }
    }
}