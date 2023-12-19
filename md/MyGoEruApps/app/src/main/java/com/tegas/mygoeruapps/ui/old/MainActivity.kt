package com.tegas.mygoeruapps.ui.old

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tegas.mygoeruapps.data.local.Teacher
import com.tegas.mygoeruapps.data.local.TeacherList
import com.tegas.mygoeruapps.databinding.ActivityMainBinding
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var fireStore: FirebaseFirestore
    private var db = Firebase.firestore

    private lateinit var bottomSheetDialog: BottomSheetDialog
    private var originalTeacherList = ArrayList<Teacher>()
    private var filteredTeacherList = ArrayList<Teacher>()
    private lateinit var rvAdapter: TeacherAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        fireStore = FirebaseFirestore.getInstance()

        showLoading(true)

        val userId = firebaseAuth.currentUser!!.uid
        val ref = db.collection("users").document(userId)
        ref.get().addOnSuccessListener {
            if (it != null) {

                val fullname = it.data?.get("fullname").toString()
                val name = it.data?.get("name").toString()
                val email = it.data?.get("email").toString()
                val password = it.data?.get("password").toString()
                val userType = it.data?.get("usertype").toString()

                binding.userType.text = userType
                binding.fullName.text = fullname

                showLoading(false)
            }
        }
            .addOnFailureListener {

                showLoading(false)

            }

//        binding.btnLogout.setOnClickListener {
//            firebaseAuth.signOut()
//            Intent(this, WelcomeActivity::class.java).also {
//                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                startActivity(it)
//                Toast.makeText(this, "Logout berhasil!", Toast.LENGTH_SHORT).show()
//            }
//        }

        setRecyclerView()
//        setFilterData()
    }

    private fun setRecyclerView() {
        rvAdapter = TeacherAdapter()
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvList.layoutManager = layoutManager
        binding.rvList.adapter = rvAdapter

        originalTeacherList.addAll(TeacherList.teacherList)
        filteredTeacherList.addAll(originalTeacherList)
        rvAdapter.addTeacherList(TeacherList.teacherList)
    }

//    private fun setFilterData() {
//        binding.fabFilter.setBackgroundColor(resources.getColor(R.color.primary_color, null))
//        binding.fabFilter.setOnClickListener {
//            val dialogView = layoutInflater.inflate(R.layout.filter_data, null)
//
//            val etMin = dialogView.findViewById<EditText>(R.id.et_min)
//            val etMax = dialogView.findViewById<EditText>(R.id.et_max)
//            val etAddress = dialogView.findViewById<EditText>(R.id.et_address)
//            val applyButton = dialogView.findViewById<Button>(R.id.btn_apply_filter)
//            val resetButton = dialogView.findViewById<Button>(R.id.btn_reset_filter)
//            applyButton.setOnClickListener {
//                applyFilter(etMin, etMax, etAddress)
//            }
//            resetButton.setOnClickListener {
//                resetFilter(etMin, etMax, etAddress)
//            }
//
//            bottomSheetDialog = BottomSheetDialog(this)
//            bottomSheetDialog.setContentView(dialogView)
//            bottomSheetDialog.show()
//
//
//        }
//    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun applyFilter(etMin: EditText, etMax: EditText, etAddress: EditText) {
        // Filter the list based on age range
        val minAge = etMin.text.toString().toIntOrNull() ?: Int.MIN_VALUE
        val maxAge = etMax.text.toString().toIntOrNull() ?: Int.MAX_VALUE
        val filterAddress = etAddress.text.toString().toLowerCase(Locale.getDefault())

        filteredTeacherList.clear()
        for (teacher in originalTeacherList) {
            val teacherAge = teacher.age
            val teacherAddress = teacher.address.toLowerCase(Locale.getDefault())

            if (teacherAge in minAge..maxAge && teacherAddress.contains(filterAddress)) {
                filteredTeacherList.add(teacher)
            }
        }

        // Update the RecyclerView with the filtered list
        rvAdapter.addTeacherList(filteredTeacherList)
        bottomSheetDialog.dismiss()
    }

    private fun resetFilter(etMin: EditText, etMax: EditText, etAddress: EditText) {
        // Clear the filter and show the original list
        etMin.text.clear()
        etMax.text.clear()
        etAddress.text.clear()

        filteredTeacherList.clear()
        filteredTeacherList.addAll(originalTeacherList)

        // Update the RecyclerView with the original list
        rvAdapter.addTeacherList(filteredTeacherList)
        bottomSheetDialog.dismiss()
    }

}