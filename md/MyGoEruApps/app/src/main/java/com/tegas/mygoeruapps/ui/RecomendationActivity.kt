package com.tegas.mygoeruapps.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.tegas.mygoeruapps.R
import com.tegas.mygoeruapps.data.database.DataBaseModule
import com.tegas.mygoeruapps.databinding.ActivityFavoriteBinding
import com.tegas.mygoeruapps.databinding.ActivityRecomendationBinding
import com.tegas.mygoeruapps.ui.favorite.FavoriteViewModel
import com.tegas.mygoeruapps.ui.student.MyAdapter

class RecomendationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecomendationBinding
    private lateinit var adapter: MyAdapter

    private val viewModel by viewModels<FavoriteViewModel>() {
        FavoriteViewModel.Factory(DataBaseModule(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecomendationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onResume() {
        super.onResume()
        viewModel.getFavorite().observe(this) {
            adapter = MyAdapter(it)

            binding.rvFav.layoutManager = LinearLayoutManager(this)
            binding.rvFav.adapter = adapter
        }
    }
}