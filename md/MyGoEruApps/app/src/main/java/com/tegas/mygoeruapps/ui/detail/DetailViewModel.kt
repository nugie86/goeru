package com.tegas.mygoeruapps.ui.detail

import android.content.ContentValues
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tegas.mygoeruapps.data.UserRepository
import com.tegas.mygoeruapps.data.database.DataBaseModule
import com.tegas.mygoeruapps.data.response.DetailResponse
import com.tegas.mygoeruapps.data.response.Guru
import com.tegas.mygoeruapps.data.response.GuruItem
import com.tegas.mygoeruapps.data.retrofit.ApiConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.tegas.mygoeruapps.data.Result

class DetailViewModel(private val db: DataBaseModule) : ViewModel() {

//    private val _teacherDetail = MediatorLiveData<Result<Guru>>()
//    val teacherDetail: LiveData<Result<Guru>> = _teacherDetail
//
//    fun getTeacherDetail(id: String?) {
//        val liveData = repository.getTeacherDetail(id!!)
//        _teacherDetail.addSource(liveData) { result ->
//            _teacherDetail.value = result
//        }
//    }


    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    val resultFavorite = MutableLiveData<Boolean>()
    val resultDeleteFavorite = MutableLiveData<Boolean>()

    private var isFavorite = false

    private val _teacherDetail = MutableLiveData<DetailResponse>()
    val teacherDetail: LiveData<DetailResponse> = _teacherDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getTeacherDetail(id: String?) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val client = ApiConfig.apiService().getTeacherDetail(id!!)
            client.enqueue(object : Callback<DetailResponse> {
                override fun onResponse(
                    call: Call<DetailResponse>,
                    response: Response<DetailResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        _teacherDetail.value = response.body()
                    } else {
                        val error = "Error: ${response.message()}"
                        _errorMessage.postValue(error)
                        Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                    _isLoading.value = false
                    Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
                    val error = "Failure: ${t.message.toString()}"
                    _errorMessage.postValue(error)
                }
            })
        }
    }

    fun saveUser(teacher: GuruItem?) {
        viewModelScope.launch {
            teacher?.let {
                if (isFavorite) {
                    db.teacherDao.delete(teacher)
                    resultDeleteFavorite.value = true
                } else {
                    db.teacherDao.insert(teacher)
                    resultFavorite.value = true
                }
            }
            isFavorite = !isFavorite
        }
    }

    fun findFavorite(id: String, listenFavorite: () -> Unit) {
        viewModelScope.launch {
            val teacher = db.teacherDao.findById(id)
            if (teacher != null) {
                listenFavorite()
                isFavorite = true
            }
        }
    }

    class Factory(private val db: DataBaseModule): ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = DetailViewModel(db) as T
    }
}