package com.tegas.mygoeruapps.ui.teacher

import android.content.ContentValues
import android.provider.MediaStore.Audio.Media
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.tegas.mygoeruapps.data.Result
import com.tegas.mygoeruapps.data.UserModel
import com.tegas.mygoeruapps.data.UserRepository
import com.tegas.mygoeruapps.data.response.DescriptionResponse
import com.tegas.mygoeruapps.data.response.DetailResponse
import com.tegas.mygoeruapps.data.response.UploadResponse
import com.tegas.mygoeruapps.data.retrofit.ApiConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TeacherViewModel(private val repository: UserRepository) : ViewModel() {

    private val _postResponse = MediatorLiveData<Result<UploadResponse>>()
    val postResponse: LiveData<Result<UploadResponse>> = _postResponse

    private val _descriptionResponse = MediatorLiveData<Result<DescriptionResponse>>()
    val descriptionResponse: LiveData<Result<DescriptionResponse>> = _descriptionResponse

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun postImage(
        token: String,
        file: MultipartBody.Part
    ) {
        val liveData = repository.uploadImage(token, file)
        _postResponse.addSource(liveData) { result ->
            _postResponse.value = result
        }
    }

    fun postDescription(
        token: String,
        deskripsi: String
    ) {
        val liveData = repository.postDescription(token, deskripsi)
        _descriptionResponse.addSource(liveData) { result ->
            _descriptionResponse.value = result
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

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
                        Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                    _isLoading.value = false
                    Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
                }

            })
        }
    }
}