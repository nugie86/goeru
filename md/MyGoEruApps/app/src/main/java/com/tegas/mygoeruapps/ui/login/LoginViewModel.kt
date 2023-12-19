package com.tegas.mygoeruapps.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.tegas.mygoeruapps.data.UserRepository
import com.tegas.mygoeruapps.data.response.LoginResponse
import com.tegas.mygoeruapps.data.Result

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    private val _loginViewModel = MediatorLiveData<Result<LoginResponse>>()
    val loginViewModel: LiveData<Result<LoginResponse>> = _loginViewModel

    fun login(email: String, password: String) {
        val liveData = repository.login(email, password)
        _loginViewModel.addSource(liveData) { result ->
            _loginViewModel.value = result
        }
    }
}