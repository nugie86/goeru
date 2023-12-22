package com.tegas.mygoeruapps.ui.preference

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CheckBox
import android.widget.Toast
import com.tegas.mygoeruapps.R
import com.tegas.mygoeruapps.databinding.ActivityNewBinding
import com.tegas.mygoeruapps.ui.login.LoginActivity
import com.tegas.mygoeruapps.ui.student.StudentActivity

class NewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewBinding

    private val sharedPreferencesKey = "selected_checkboxes"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val checkBoxes = arrayOf(
            findViewById<CheckBox>(R.id.cb_math),
            findViewById(R.id.cb_physics),
            findViewById(R.id.cb_chemistry),
            findViewById(R.id.cb_economy),
            findViewById(R.id.cb_biology),
            findViewById(R.id.cb_sociology)
        )

        loadCheckBoxState(checkBoxes)

        binding.nextButton.setOnClickListener {
            val selectedCheckBoxes = checkBoxes.filter { it.isChecked }
            if (selectedCheckBoxes.size == 3) {
                saveCheckBoxState(checkBoxes)

                val selectedOptionsText = selectedCheckBoxes.joinToString(", ") { checkBox ->
                    checkBox.text.toString()
                }
                Toast.makeText(this, "Selected options: $selectedOptionsText", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, StudentActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Please select 3", Toast.LENGTH_SHORT).show()
            }
//            val intent = Intent(this, LoginActivity::class.java)
//            startActivity(intent)
        }

    }

    private fun saveCheckBoxState(checkBoxes: Array<CheckBox>) {
        val sharedPreferences = getSharedPreferences(sharedPreferencesKey, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        for ((index, checkBox) in checkBoxes.withIndex()) {
            editor.putBoolean("$sharedPreferencesKey$index", checkBox.isChecked)
        }
    }

    private fun loadCheckBoxState(checkBoxes: Array<CheckBox>) {
        val sharedPreferences = getSharedPreferences(sharedPreferencesKey, Context.MODE_PRIVATE)

        for ((index, checkBox) in checkBoxes.withIndex()) {
            checkBox.isChecked = sharedPreferences.getBoolean("$sharedPreferences$index", false)
        }
    }
}