package com.tegas.mygoeruapps.ui.preference

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.tegas.mygoeruapps.R
import com.tegas.mygoeruapps.data.Result
import com.tegas.mygoeruapps.data.ViewModelFactory
import com.tegas.mygoeruapps.databinding.ActivityPreferenceBinding
import com.tegas.mygoeruapps.databinding.ActivityPreferencesBinding
import com.tegas.mygoeruapps.databinding.ActivityProfileBinding
import com.tegas.mygoeruapps.ui.login.LoginActivity
import com.tegas.mygoeruapps.ui.student.StudentActivity

class PreferenceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPreferenceBinding
    private val viewModel by viewModels<PreferencesViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private var math: Double = 0.0
    private var physics: Double = 0.0
    private var chemistry: Double = 0.0
    private var economy: Double = 0.0
    private var biology: Double = 0.0
    private var sosiology: Double = 0.0
    private var history: Double = 0.0
    private var geography: Double = 0.0
    private var antropology: Double = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreferenceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val age = binding.etAge.text
        val cb_math = binding.cbMath
        val cb_pyshic = binding.cbPhysics
        val cb_chem = binding.cbChemistry
        val cb_eco = binding.cbEconomy
        val cb_bio = binding.cbBiology
        val cb_sosio = binding.cbSociology
        val cb_history = binding.cbHistory
        val cb_geography = binding.cbGeography
        val cb_antropology = binding.cbAntro

        binding.nextButton.setOnClickListener {
            val checkedCount = countCheckedCheckboxes(
                cb_bio,
                cb_antropology,
                cb_chem,
                cb_eco,
                cb_geography,
                cb_history,
                cb_sosio,
                cb_pyshic,
                cb_math
            )

            if (checkedCount == 3 && !age.isNullOrEmpty()) {
                val userAge = age.toString().toInt()
                math = if (cb_math.isChecked) 5.5 else 0.0
                physics = if (cb_pyshic.isChecked) 5.5 else 0.0
                chemistry = if (cb_chem.isChecked) 5.5 else 0.0
                economy = if (cb_eco.isChecked) 5.5 else 0.0
                biology = if (cb_bio.isChecked) 5.5 else 0.0
                sosiology = if (cb_sosio.isChecked) 5.5 else 0.0
                history = if (cb_history.isChecked) 5.5 else 0.0
                geography = if (cb_geography.isChecked) 5.5 else 0.0
                antropology = if (cb_antropology.isChecked) 5.5 else 0.0

                showVariableToast()
                viewModel.getSession().observe(this) { user ->
                    if (!user.isLogin) {
                        Toast.makeText(this, "You need to login", Toast.LENGTH_LONG).show()

                    } else {

                        viewModel.postPreferences(
                            user.token,
                            userAge,
                            math,
                            physics,
                            chemistry,
                            economy,
                            biology,
                            sosiology,
                            history,
                            geography,
                            antropology
                        )

                        AlertDialog.Builder(this).apply {
                            setTitle("Selamat!")
                            setMessage("Preferensi telah diubah.")
                            setCancelable(false)
                            setPositiveButton("Kembali") { _, _ ->
                                val intent = Intent(context, StudentActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            create()
                            show()
                        }
                    }
                }


            } else {
                Toast.makeText(this, "Please fill your age and choose exactly 3 subjects", Toast.LENGTH_LONG)
                    .show()

            }
            Log.d("age", "age: $age")
            Log.d("math", "math: $math")
            Log.d("physics", "physics: $physics")
            Log.d("chemistry", "chemistry: $chemistry")
            Log.d("economy", "economy: $economy")
            Log.d("biology", "biology: $biology")
            Log.d("sosiology", "sosiology: $sosiology")
            Log.d("history", "history: $history")
            Log.d("geolography", "geography: $geography")
            Log.d("antropology", "antropology: $antropology")
        }

        viewModel.preferenceResponse.observe(this) {
            when (it) {
                is Result.Loading -> {

                }

                is Result.Error -> {
                    Toast.makeText(this, it.error, Toast.LENGTH_LONG).show()
                }

                is Result.Success -> {
                    Toast.makeText(this, "Thank you for filling", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun countCheckedCheckboxes(vararg checkboxes: CheckBox): Int {
        return checkboxes.count { it.isChecked }
    }

    private fun showVariableToast() {
        val variableWithFive = mutableListOf<String>()

        for (i in 1..9) {
            val variableName = "Variable $i"
            val variableValue = when (i) {
                1 -> math
                1 -> physics
                1 -> chemistry
                1 -> economy
                1 -> biology
                1 -> sosiology
                1 -> history
                1 -> geography
                1 -> antropology
                else -> 0.0
            }
            if (variableValue == 5.0) {
                variableWithFive.add(variableName)
            }
        }

        if (variableWithFive.isNotEmpty()) {
            val message = "Pilihanmu: \n${variableWithFive.joinToString(", ")}"
            showToast(message)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}