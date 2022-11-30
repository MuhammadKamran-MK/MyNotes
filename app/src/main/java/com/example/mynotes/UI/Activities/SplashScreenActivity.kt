package com.example.mynotes.UI.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.databinding.DataBindingUtil
import com.example.mynotes.R
import com.example.mynotes.databinding.ActivitySplashScreenBinding

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(
            this, R.layout.activity_splash_screen
        )

        var i = 0

        while (i <= 3) {

            i++

            binding.progressBar.progress = i

            if (i == 3) {

                Handler(Looper.getMainLooper()).postDelayed({

                    startActivity(Intent(this, MainActivity::class.java))
                    finish()

                }, 3000)

            }

        }
    }
}