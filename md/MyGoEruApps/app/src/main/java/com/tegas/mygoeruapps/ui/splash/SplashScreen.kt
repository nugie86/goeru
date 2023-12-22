package com.tegas.mygoeruapps.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import com.tegas.mygoeruapps.data.ViewModelFactory
import com.tegas.mygoeruapps.databinding.ActivitySplashScreenBinding
import com.tegas.mygoeruapps.ui.student.StudentActivity
import com.tegas.mygoeruapps.ui.teacher.TeacherActivity

class SplashScreen : AppCompatActivity() {
    private val viewModel by viewModels<SplashViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed({
            getSession()
        }, 3000)

    }

    private fun getSession() {
        viewModel.getSession().observe(this){  user ->
            if (!user.isLogin) {
                Log.d("isLogin", "Not login")
                startActivity(Intent(this@SplashScreen, WelcomeActivity::class.java))
                finish()
            } else {
                Log.d("isLogin", "Login")
                Log.d("role", "role: ${user.role}")
                if (user.role == 1) {
                    startActivity(Intent(this@SplashScreen, TeacherActivity::class.java))
                    finish()
                } else {
                    startActivity(Intent(this@SplashScreen, StudentActivity::class.java))
                    finish()
                }
            }
        }
    }
}