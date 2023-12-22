package com.tegas.mygoeruapps.ui.register

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.jakewharton.rxbinding2.widget.RxTextView
import com.tegas.mygoeruapps.R
import com.tegas.mygoeruapps.data.ViewModelFactory
import com.tegas.mygoeruapps.databinding.ActivityRegisterBinding
import com.tegas.mygoeruapps.data.Result
import com.tegas.mygoeruapps.ui.login.LoginActivity
import com.tegas.mygoeruapps.ui.preference.AgePreferenceActivity
import com.tegas.mygoeruapps.ui.preference.PreferencesActivity

class RegisterActivity : AppCompatActivity() {

    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Full name validation
        val nameStream = RxTextView.textChanges(binding.etFullname)
            .skipInitialValue()
            .map { name ->
                name.isEmpty()
            }
        nameStream.subscribe {
            showNameExistAlert(it)
        }

        // Email validation
        val emailStream = RxTextView.textChanges(binding.etEmail)
            .skipInitialValue()
            .map { email ->
                !Patterns.EMAIL_ADDRESS.matcher(email).matches()
            }
        emailStream.subscribe {
            showEmailValidAlert(it)
        }

        // Username validation
        val usernameStream = RxTextView.textChanges(binding.etUsername)
            .skipInitialValue()
            .map { username ->
                username.length < 6
            }
        usernameStream.subscribe {
            showTextMinimalAlert(it, "Username")
        }
        // Password validation
        val passwordStream = RxTextView.textChanges(binding.etPassword)
            .skipInitialValue()
            .map { password ->
                password.length < 6
            }
        passwordStream.subscribe {
            showTextMinimalAlert(it, "Password")
        }

        // Confirm password validation
        val passwordConfirmStream = io.reactivex.Observable.merge(
            RxTextView.textChanges(binding.etConfirmPassword)
                .skipInitialValue()
                .map { password ->
                    password.toString() != binding.etConfirmPassword.text.toString()
                },
            RxTextView.textChanges(binding.etConfirmPassword)
                .skipInitialValue()
                .map { confirmPassword ->
                    confirmPassword.toString() != binding.etPassword.text.toString()
                }
        )
        passwordConfirmStream.subscribe {
            showPasswordConfirmAlert(it)
        }

        // Button enable True or False
        val invalidFieldsStream = io.reactivex.Observable.combineLatest(
            nameStream,
            emailStream,
            usernameStream,
            passwordStream,
            passwordConfirmStream
        ) { nameInvalid: Boolean, emailInvalid: Boolean, usernameInvalid: Boolean, passwordInvalid: Boolean, passwordConfirmInvalid: Boolean ->
            !nameInvalid && !emailInvalid && !usernameInvalid && !passwordInvalid && !passwordConfirmInvalid
        }
        invalidFieldsStream.subscribe { isValid ->
            if (isValid) {
                binding.btnRegister.isEnabled = true
                binding.btnRegister.backgroundTintList = ContextCompat.getColorStateList(
                    this,
                    R.color.primary_color
                )
            } else {
                binding.btnRegister.isEnabled = false
                binding.btnRegister.backgroundTintList =
                    ContextCompat.getColorStateList(this, android.R.color.darker_gray)
            }
        }

        setupView()
        setupAction()
        setupLogin()
        goToLogin()
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

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility =
            when (isLoading) {
                true -> View.VISIBLE
                else -> View.GONE
            }

    }

    private fun toastFailed() {
        Toast.makeText(
            this,
            R.string.failed_register,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun setupAction() {
        binding.btnRegister.setOnClickListener {
            binding.apply {
                if (etFullname.error.isNullOrEmpty() && etEmail.error.isNullOrEmpty() && etPassword.error.isNullOrEmpty()) {
                    val name = etFullname.text.toString().trim()
                    val email = etEmail.text.toString().trim()
                    val password = etPassword.text.toString().trim()
                    val confirmPassword = etConfirmPassword.text.toString().trim()
                    viewModel.register(name, email, password, confirmPassword)
                } else {
                    toastFailed()
                }
            }
        }
    }

    private fun setupLogin() {
        val email = binding.etEmail.text.toString()
        viewModel.registerResponse.observe(this) {
            when (it) {
                is Result.Loading -> {
                    showLoading(true)
                }

                is Result.Success -> {
                    showLoading(false)
                    AlertDialog.Builder(this).apply {
                        setTitle("Selamat!")
                        setMessage("Akun dengan email $email telah dibuat.")
                        setCancelable(false)
                        setPositiveButton("Okay") { _, _ ->
                            val intent = Intent(context, PreferencesActivity::class.java)
                            intent.putExtra("token", it.data.token)
                            startActivity(intent)
                            finish()
                        }
                        create()
                        show()
                    }
                }

                is Result.Error -> {
                    val errorMessage = when {
                        it.isTimeout() -> "Timeout: Something's wrong with the server"
                        else -> it.error ?: "Gagal Daftar"
                    }
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                    showLoading(false)
                }
            }
        }
    }

    private fun showNameExistAlert(isNotValid: Boolean) {
        binding.etFullname.error = if (isNotValid) "Nama tidak boleh kosong!" else null
    }

    private fun showTextMinimalAlert(isNotValid: Boolean, text: String) {
        if (text == "Username")
            binding.etUsername.error = if (isNotValid) "$text harus lebih dari 6 huruf!" else null
        else if (text == "Password")
            binding.etPassword.error = if (isNotValid) "$text harus lebih dari 8 huruf" else null
    }

    private fun showEmailValidAlert(isNotValid: Boolean) {
        binding.etEmail.error = if (isNotValid) "Email tidak valid" else null
    }

    private fun showPasswordConfirmAlert(isNotValid: Boolean) {
        binding.etConfirmPassword.error = if (isNotValid) "Password tidak sama" else null
    }

    private fun Result.Error.isTimeout(): Boolean {
        // Check if the error is a timeout
        // You might need to adjust this based on the actual structure of your error class
        return this.error?.contains("timeout", ignoreCase = true) == true
    }

    private fun goToLogin() {
        binding.tvHaveAccount.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

}