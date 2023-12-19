package com.tegas.mygoeruapps.ui.preference

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.tegas.mygoeruapps.R
import com.tegas.mygoeruapps.databinding.ActivityPreferenceBinding
import com.tegas.mygoeruapps.databinding.ActivityProfileBinding
import com.tegas.mygoeruapps.ui.login.LoginActivity

class PreferenceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPreferenceBinding
    private var selectedOptions: MutableList<String> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreferenceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val age = intent.getIntExtra("userAge", 18)

        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val selectedRadioButton: RadioButton = findViewById(checkedId)
            val selectedOption = selectedRadioButton.text.toString()

            if (selectedRadioButton.isChecked) {
                if (!selectedOptions.contains(selectedOption) && selectedOptions.size < 3) {
                    selectedOptions.add(selectedOption)
                    Toast.makeText(this, "Selected option: $selectedOption", Toast.LENGTH_SHORT)
                        .show()
                } else if (selectedOptions.contains(selectedOption)){

                }
                else {
                    selectedRadioButton.isChecked = false
                    Toast.makeText(this, "Selection limit reached!", Toast.LENGTH_SHORT).show()
                }
            } else {
                selectedOptions.remove(selectedOption)
            }

        }

        binding.nextButton.setOnClickListener {
            val selectedOptionsString = selectedOptions.joinToString (", ")

            AlertDialog.Builder(this).apply {
                setTitle("Alright!")
                setMessage("Thank you for answering. Selected options: $selectedOptionsString. Let\' login!")
                setCancelable(false)
                setPositiveButton("Login") { _, _ ->
                    val intent = Intent(context, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                create()
                show()
            }
        }

    }
}