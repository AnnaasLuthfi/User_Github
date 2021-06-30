package com.myapps.usergithubsub3.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.edit
import com.myapps.usergithubsub3.databinding.ActivityReminderBinding
import com.myapps.usergithubsub3.reminder.ReminderReceiver

class ReminderActivity : AppCompatActivity() {

    private var binding: ActivityReminderBinding? = null
    private lateinit var alarmReceiver: ReminderReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReminderBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        supportActionBar?.title = "Reminder Setup"

        loadCondition()

        binding?.reminderActivation?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                alarmReceiver.setTurnOnReminder(this)
            } else {
                alarmReceiver.turnOffReminder(this)
            }
            savedCondition()
        }

        alarmReceiver = ReminderReceiver()

    }

    private fun savedCondition(){
        val preferences = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        preferences.edit {
            putBoolean("KEY_BOOLEAN", binding?.reminderActivation?.isChecked ?: false)
        }
//
//        val editor = preferences.edit()
//        editor.apply {
////            putBoolean("KEY_BOOLEAN", binding?.reminderActivation!!.isChecked)
//            putBoolean("KEY_BOOLEAN", binding?.reminderActivation?.isChecked ?: false)
//        }.apply()
    }

    private fun loadCondition() {
        val getPreferences = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        val savedBoolean = getPreferences.getBoolean("KEY_BOOLEAN", binding?.reminderActivation!!.isChecked)

        binding?.reminderActivation!!.isChecked = savedBoolean
    }

    override fun onDestroy() {
        super.onDestroy()

        binding = null
    }
}