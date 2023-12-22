package com.tegas.mygoeruapps.ui.teacher

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.tegas.mygoeruapps.R
import com.tegas.mygoeruapps.data.ViewModelFactory
import com.tegas.mygoeruapps.data.response.RatingsItem
import com.tegas.mygoeruapps.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val viewModel by viewModels<TeacherViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra("id")

        Log.d("teacherId", id.toString())
        viewModel.getTeacherDetail(id)
        viewModel.teacherDetail.observe(this) {
            val teacher = it.guru

            binding.tvName.text = teacher.nama
            binding.tvAddress.text = teacher.asal
            binding.tvSubject.text = teacher.mapel
            binding.tvPrice.text = teacher.harga.toString()

            binding.description.text = teacher.deskripsi
            Glide.with(this)
                .load(teacher.gambar)
                .centerCrop()
                .into(binding.ivProfile)

            val rvAdapter = ReviewAdapter()
            val layoutManager = LinearLayoutManager(this)
            layoutManager.orientation = LinearLayoutManager.VERTICAL
            binding.rvReview.layoutManager = layoutManager
            binding.rvReview.adapter = rvAdapter

            Log.d("API Response", "Entire API response: $it") // Print the entire API response

            Log.d("it.guru.rating", "it.guru.rating: ${it.guru.ratings}")

            try {
                rvAdapter.addReviewList(teacher.ratings)
            } catch (e: Exception) {
                Toast.makeText(this, "FAILED TO ADD REVIEW LIST", Toast.LENGTH_LONG).show()
                Log.e("Adapter", "ERROR TO ADD REVIEW LIST")
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
                binding.tvDescription.visibility = View.GONE
            } else {
                binding.progressBar.visibility = View.GONE
                binding.tvDescription.visibility = View.VISIBLE
            }
        }
    }
}