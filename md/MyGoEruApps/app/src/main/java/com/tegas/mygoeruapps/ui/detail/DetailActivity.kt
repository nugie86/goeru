package com.tegas.mygoeruapps.ui.detail

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tegas.mygoeruapps.R
import com.tegas.mygoeruapps.data.database.DataBaseModule
import com.tegas.mygoeruapps.data.response.Guru
import com.tegas.mygoeruapps.data.response.GuruItem
import com.tegas.mygoeruapps.data.response.Rating
import com.tegas.mygoeruapps.databinding.ActivityDetailBinding
import com.tegas.mygoeruapps.ui.order.OrderActivity

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel by viewModels<DetailViewModel>() {
        DetailViewModel.Factory(DataBaseModule(this))
    }

    private lateinit var teacher: Guru
    private var rating: Rating? = null
    private var teacherId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        teacherId = intent.getStringExtra("teacherId")
        Log.d("Teacher", "teacher id: $teacherId")

        val teacherItem = intent.getParcelableExtra<GuruItem>("teacherItem")
        val id = teacherItem?.idUser

        viewModel.getTeacherDetail(id)

        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
                binding.tvDescription.visibility = View.GONE
            } else {
                binding.progressBar.visibility = View.GONE
                binding.tvDescription.visibility = View.VISIBLE
            }
        }

        viewModel.teacherDetail.observe(this) { detailResponse ->
            teacher = detailResponse.guru
            rating = detailResponse.rating

            if (teacher.gambar.isEmpty()) {
                Glide
                    .with(this)
                    .load(R.drawable.avatar)
                    .centerCrop()
                    .into(binding.ivProfile)
            } else {
                Glide
                    .with(this)
                    .load(teacher.gambar)
                    .centerCrop()
                    .into(binding.ivProfile)
            }

            binding.tvName.text = HtmlCompat.fromHtml(
                getString(R.string.name, teacher.nama),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            binding.tvAddress.text = getString(R.string.address, teacher.asal)
            binding.tvSubject.text = getString(R.string.subject, teacher.mapel)
            binding.tvPrice.text = getString(R.string.price, teacher.harga.toString())
            binding.description.text = teacher.deskripsi
            binding.tvRating.text = rating?.nilai.toString()

            Log.d("rating", "rating: ${rating?.nilai}")
        }

        binding.btnFav.setBackgroundColor(resources.getColor(R.color.primary_color, null))

        viewModel.resultFavorite.observe(this) {
            binding.btnFav.changeIconColor(R.color.primary_color)
        }
        viewModel.resultDeleteFavorite.observe(this) {
            binding.btnFav.changeIconColor(R.color.white)
        }
        binding.btnFav.setOnClickListener {
            viewModel.saveUser(teacherItem)
        }
        viewModel.findFavorite(teacherItem?.idUser ?: "g0") {
            binding.btnFav.changeIconColor(R.color.primary_color)
        }

        binding.btnOrder.setBackgroundColor(resources.getColor(R.color.primary_color, null))
        binding.btnOrder.setOnClickListener {
            val intent = Intent(this, OrderActivity::class.java)
            intent.putExtra("price", teacherItem?.harga)
            intent.putExtra("name", teacherItem?.nama)
            startActivity(intent)
        }

        viewModel.errorMessage.observe(this) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun FloatingActionButton.changeIconColor(@ColorRes color: Int) {
        imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this.context, color))
    }

}