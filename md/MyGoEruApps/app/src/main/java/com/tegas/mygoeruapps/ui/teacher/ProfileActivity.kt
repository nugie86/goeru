package com.tegas.mygoeruapps.ui.teacher

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.tegas.mygoeruapps.R
import com.tegas.mygoeruapps.data.ViewModelFactory
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

            Glide.with(this)
                .load(teacher.gambar)
                .centerCrop()
                .into(binding.ivProfile)
        }
    }
}