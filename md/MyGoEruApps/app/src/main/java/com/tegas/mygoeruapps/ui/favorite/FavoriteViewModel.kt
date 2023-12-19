package com.tegas.mygoeruapps.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tegas.mygoeruapps.data.database.DataBaseModule

class FavoriteViewModel(private val db: DataBaseModule) : ViewModel() {

    fun getFavorite() = db.teacherDao.loadAll()

    class Factory(private val db: DataBaseModule) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = FavoriteViewModel(db) as T
    }
}
