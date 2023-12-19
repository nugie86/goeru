package com.tegas.mygoeruapps.ui.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.jakewharton.rxbinding2.widget.RxTextView
import com.tegas.mygoeruapps.R
import com.tegas.mygoeruapps.data.Result
import com.tegas.mygoeruapps.data.ViewModelFactory
import com.tegas.mygoeruapps.databinding.ActivityLoginBinding
import com.tegas.mygoeruapps.ui.register.RegisterActivity
import com.tegas.mygoeruapps.ui.student.StudentActivity
import com.tegas.mygoeruapps.ui.teacher.TeacherActivity

class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityLoginBinding
    private val TAG = "LoginActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Username validation
        val usernameStream = RxTextView.textChanges(binding.etEmail)
            .skipInitialValue()
            .map { username ->
                username.isEmpty()
            }
        usernameStream.subscribe {
            showTextMinimalAlert(it, "Email/Username")
            showEmailExistAlert(it)
        }
        // Password validation
        val passwordStream = RxTextView.textChanges(binding.etPassword)
            .skipInitialValue()
            .map { password ->
                password.isEmpty()
            }
        passwordStream.subscribe {
            showTextMinimalAlert(it, "Password")
            showPasswordExistAlert(it)
        }

        // Button enable True or False
        val invalidFieldsStream = io.reactivex.Observable.combineLatest(
            usernameStream,
            passwordStream
        ) { usernameInvalid: Boolean, passwordInvalid: Boolean ->
            !usernameInvalid && !passwordInvalid
        }
        invalidFieldsStream.subscribe { isValid ->
            if (isValid) {
                binding.btnLogin.isEnabled = true
                binding.btnLogin.backgroundTintList =
                    ContextCompat.getColorStateList(this, R.color.primary_color)
            } else {
                binding.btnLogin.isEnabled = false
                binding.btnLogin.backgroundTintList =
                    ContextCompat.getColorStateList(this, android.R.color.darker_gray)
            }
        }

        setupView()
        setupAction()
        setupLogin()
        goToRegister()
    }

    private fun setupAction() {
        binding.btnLogin.setOnClickListener {
            binding.apply {
                if (etEmail.error.isNullOrEmpty() && etPassword.error.isNullOrEmpty()) {
                    val email = etEmail.text.toString().trim()
                    val password = etPassword.text.toString().trim()
                    viewModel.login(email, password)
                }
            }
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupLogin() {
        viewModel.loginViewModel.observe(this) {
            when (it) {
                is Result.Loading -> {
                    showLoading(true)
                }

                is Result.Success -> {
                    showLoading(false)
                    val role = it.data.role
                    val id = it.data.userId
                    AlertDialog.Builder(this).apply {
                        setTitle("Alright!")
                        setMessage("Login Success!")
                        setPositiveButton("Go to Main Page") { _, _ ->
                            navigateToCorrectActivity(role, id)
                        }
                        create()
                        show()
                    }
                }

                is Result.Error -> {
                    showLoading(false)
                    val errorMessage = when {
                        it.isTimeout() -> "Timeout: Something's wrong with the server"
                        else -> it.error ?: "Gagal Login"
                    }
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun goToRegister() {
        binding.tvHaventAccount.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showEmailExistAlert(isNotValid: Boolean) {
        binding.etEmail.error = if (isNotValid) "Nama tidak boleh kosong!" else null
    }

    private fun showPasswordExistAlert(isNotValid: Boolean) {
        binding.etPassword.error = if (isNotValid) "Password tidak boleh kosong!" else null
    }

    private fun showTextMinimalAlert(isNotValid: Boolean, text: String) {
        if (text == "Username")
            binding.etEmail.error = if (isNotValid) "$text tidak boleh kosong!" else null
        else if (text == "Password")
            binding.etPassword.error = if (isNotValid) "$text tidak boleh kosong" else null
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun navigateToCorrectActivity(role: Int, id: String) {
        val intent: Intent = when (role) {
            1 -> { // Replace 1 with the actual role code for Student
                Intent(this, TeacherActivity::class.java)
            }

            else -> { // Replace 2 with the actual role code for Teacher
                Intent(this, StudentActivity::class.java)
            }
        }
        intent.putExtra("id", id)

        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun Result.Error.isTimeout(): Boolean {
        // Check if the error is a timeout
        // You might need to adjust this based on the actual structure of your error class
        return this.error?.contains("timeout", ignoreCase = true) == true
    }
}