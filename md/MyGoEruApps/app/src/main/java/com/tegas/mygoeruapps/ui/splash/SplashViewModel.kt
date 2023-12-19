package com.tegas.mygoeruapps.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.tegas.mygoeruapps.data.Result
import com.tegas.mygoeruapps.data.UserModel
import com.tegas.mygoeruapps.data.UserRepository
import com.tegas.mygoeruapps.data.response.LoginResponse

class SplashViewModel(private val repository: UserRepository) : ViewModel() {
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    private val _splashViewModel = MediatorLiveData<Result<LoginResponse>>()
    val splashViewModel: LiveData<Result<LoginResponse>> = _splashViewModel

}