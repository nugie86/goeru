package com.tegas.mygoeruapps.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tegas.mygoeruapps.databinding.ActivityWelcomeBinding
import com.tegas.mygoeruapps.ui.login.LoginActivity
import com.tegas.mygoeruapps.ui.register.RegisterActivity
import com.tegas.mygoeruapps.ui.student.StudentActivity

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.btnMainPage.setOnClickListener {
            startActivity(Intent(this, StudentActivity::class.java))
        }

    }
}