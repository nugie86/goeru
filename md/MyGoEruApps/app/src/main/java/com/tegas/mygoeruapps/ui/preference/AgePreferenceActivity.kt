package com.tegas.mygoeruapps.ui.preference

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.tegas.mygoeruapps.R
import com.tegas.mygoeruapps.databinding.ActivityAgePreferenceBinding

class AgePreferenceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAgePreferenceBinding
    private val sharedPreferencesKey = "selected_checkboxes"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgePreferenceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.nextButton.setOnClickListener {

            if (binding.etAge.text.isNullOrEmpty()) {
                Toast.makeText(this, "Please fill your age", Toast.LENGTH_SHORT).show()
            } else {
                val userAge = binding.etAge.text.toString()
                saveUserAge(userAge)
                startActivity(Intent(this, NewActivity::class.java))
                finish()
            }
        }
    }

    private fun saveUserAge(age: String) {
        val sharedPreferences = getSharedPreferences(sharedPreferencesKey, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("$sharedPreferencesKey${0}", age)
        editor.apply()
    }
}