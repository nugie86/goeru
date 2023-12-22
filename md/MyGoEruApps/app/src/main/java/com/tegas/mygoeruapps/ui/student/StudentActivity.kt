package com.tegas.mygoeruapps.ui.student

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tegas.mygoeruapps.R
import com.tegas.mygoeruapps.data.Result
import com.tegas.mygoeruapps.data.ViewModelFactory
import com.tegas.mygoeruapps.data.response.GuruItem
import com.tegas.mygoeruapps.databinding.ActivityStudentBinding
import com.tegas.mygoeruapps.ui.favorite.FavoriteActivity
import com.tegas.mygoeruapps.ui.preference.AgePreferenceActivity
import com.tegas.mygoeruapps.ui.preference.NewActivity
import com.tegas.mygoeruapps.ui.preference.PreferencesActivity
import com.tegas.mygoeruapps.ui.splash.WelcomeActivity
import java.util.Locale

class StudentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStudentBinding
    private val viewModel by viewModels<StudentViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var adapter: MyAdapter
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var originalTeacherList: List<GuruItem>

    private val sharedPreferencesKey = "selected_checkboxes"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

//        if (!arePreferencesSet()) {
//            startActivity(Intent(this, AgePreferenceActivity::class.java))
//            finish()
//            return
//        } else {
//            val sharedPreferences = getSharedPreferences(sharedPreferencesKey, Context.MODE_PRIVATE)
//            val userAge = sharedPreferences.getString("$sharedPreferencesKey${0}", "")
//            val userPref1 = sharedPreferences.getString("$sharedPreferencesKey${1}", "")
//            val userPref2 = sharedPreferences.getString("$sharedPreferencesKey${2}", "")
//            val userPref3 = sharedPreferences.getString("$sharedPreferencesKey${3}", "")
//            Toast.makeText(this, "$userAge, $userPref1, $userPref2, $userPref3", Toast.LENGTH_LONG).show()
//            Log.d("age", "$userAge")
//            Log.d("pref1", "$userPref1")
//            Log.d("pref2", "$userPref2")
//            Log.d("pref3", "$userPref3")
//        }

        getSession()

        viewModel.getTeachers()
        setRecyclerView()

        binding.btnLogout.setOnClickListener {
            setLogout()
        }

        binding.btnBack.setOnClickListener {
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
        }

        setFilterData()
        setFavoriteButton()
    }

    private fun getSession() {
        showLoading(true)
        viewModel.getSession().observe(this) { user ->
            val token = user.token
            if (!user.isLogin) {
//                startActivity(Intent(this, WelcomeActivity::class.java))
//                finish()
                binding.btnLogout.visibility = View.GONE
                binding.btnBack.visibility = View.VISIBLE
            } else {
                binding.btnLogout.visibility = View.VISIBLE
                binding.btnBack.visibility = View.GONE
            }

        }
        showLoading(false)
    }

    private fun setRecyclerView() {
        viewModel.teacherListItem.observe(this) {
            when (it) {
                is Result.Loading -> {
                    Log.d("Result", "Loading")
                    showLoading(true)
                }

                is Result.Error -> {
                    Log.d("Result", "Error")
                    showLoading(false)
                    Toast.makeText(this, "Failed to retrieve data from server", Toast.LENGTH_SHORT)
                        .show()
                }

                is Result.Success -> {
                    Log.d("Result", "Success")
                    showLoading(false)
                    originalTeacherList = it.data
                    adapter = MyAdapter(originalTeacherList)
                    binding.rvTeacher.adapter = adapter
                    binding.rvTeacher.layoutManager = LinearLayoutManager(this)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility =
            when (isLoading) {
                true -> View.VISIBLE
                else -> View.GONE
            }
    }

    private fun setLogout() {
        viewModel.logout()

        Toast.makeText(this, "Your are logged out", Toast.LENGTH_SHORT).show()
//        startActivity(Intent(this, WelcomeActivity::class.java))
    }

    private fun applyFilter(
        filteredSubject: String,
        filteredName: String,
        filteredAddress: String
    ) {
        adapter.applyFilter(filteredSubject, filteredName, filteredAddress)
    }

    private fun resetFilter() {
        adapter.resetFilter()
    }

    private fun setFilterData() {
        binding.fabFilter.setBackgroundColor(resources.getColor(R.color.primary_color, null))
        binding.fabFilter.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.filter_data, null)

            val etSubject = dialogView.findViewById<EditText>(R.id.filter_subject)
            val etName = dialogView.findViewById<EditText>(R.id.filter_name)
            val etAddress = dialogView.findViewById<EditText>(R.id.filter_address)

            val applyButton = dialogView.findViewById<Button>(R.id.btn_apply_filter)
            val resetButton = dialogView.findViewById<Button>(R.id.btn_reset_filter)

            applyButton.setOnClickListener {
                applyFilter(
                    etSubject.text.toString().lowercase(Locale.getDefault()),
                    etName.text.toString().lowercase(Locale.getDefault()),
                    etAddress.text.toString().lowercase(Locale.getDefault())
                )
            }
            resetButton.setOnClickListener {
                resetFilter()
            }

            bottomSheetDialog = BottomSheetDialog(this)
            bottomSheetDialog.setContentView(dialogView)
            bottomSheetDialog.show()
        }

//      binding.fabRecomend.setBackgroundColor(resources.getColor(R.color.primary_color, null))
//      binding.fabRecomend.setOnClickListener {
//            val intent = Intent(this, )
//      }

    }

    private fun setFavoriteButton() {
        binding.fabFavorite.setBackgroundColor(resources.getColor(R.color.primary_color, null))
        binding.fabFavorite.setOnClickListener {
            val intent = Intent(this, FavoriteActivity::class.java)
            startActivity(intent)
        }
    }

    private fun arePreferencesSet(): Boolean {
        val sharedPreferences = getSharedPreferences(sharedPreferencesKey, Context.MODE_PRIVATE)
        return sharedPreferences.contains("$sharedPreferencesKey${0}")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
menuInflater.inflate(R.menu.menu_student, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
openMenu(item.itemId)
        return super.onOptionsItemSelected(item)
    }

private fun openMenu(selectedMenu: Int) {
    when(selectedMenu) {
        R.id.preference-> {
            val intent = Intent(this, PreferencesActivity::class.java)
            startActivity(intent)
        }
    }
}
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//
//
//        val searchItem = menu?.findItem(R.id.action_search)
//        val searchView = searchItem?.actionView as SearchView
//
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                return true
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                adapter.filter.filter(newText)
//                return true
//            }
//        })
//        return true
//    }
}
