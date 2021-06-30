package com.myapps.userfavorite.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.myapps.userfavorite.R

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        supportActionBar?.title = ""

        Handler(mainLooper).postDelayed({
            startActivity(Intent(this@SplashScreenActivity, FavoriteActivity::class.java))
            finish()
        }, 2000)
    }
}