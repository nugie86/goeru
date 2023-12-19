package com.tegas.mygoeruapps.ui.favorite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.tegas.mygoeruapps.R
import com.tegas.mygoeruapps.data.database.DataBaseModule
import com.tegas.mygoeruapps.databinding.ActivityFavoriteBinding
import com.tegas.mygoeruapps.ui.student.MyAdapter

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter: MyAdapter

    private val viewModel by viewModels<FavoriteViewModel>() {
        FavoriteViewModel.Factory(DataBaseModule(this))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onResume() {
        super.onResume()
        viewModel.getFavorite().observe(this)  {
            adapter = MyAdapter(it)

            binding.rvFav.layoutManager = LinearLayoutManager(this)
            binding.rvFav.adapter = adapter
        }
    }
}