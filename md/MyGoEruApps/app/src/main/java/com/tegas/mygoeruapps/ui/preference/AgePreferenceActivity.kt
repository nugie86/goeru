package com.tegas.mygoeruapps.ui.preference

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.tegas.mygoeruapps.R
import com.tegas.mygoeruapps.databinding.ActivityAgePreferenceBinding

class AgePreferenceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAgePreferenceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgePreferenceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val age = binding.etAge.text

        binding.nextButton.setOnClickListener {

            if (binding.etAge.text.isNullOrEmpty()) {
                Toast.makeText(this, "Please fill your age", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, NewActivity::class.java)
                intent.putExtra("userAge", age)
                startActivity(intent)
                Log.d("age", age.toString())
            }
        }
    }
}