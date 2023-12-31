package com.tegas.mygoeruapps.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.tegas.mygoeruapps.data.Result
import com.tegas.mygoeruapps.data.UserRepository
import com.tegas.mygoeruapps.data.response.LoginResponse
import com.tegas.mygoeruapps.data.response.RegisterResponse

class RegisterViewModel(private val repository: UserRepository) : ViewModel() {

    private val _loginViewModel = MediatorLiveData<Result<LoginResponse>>()
    val loginViewModel: LiveData<Result<LoginResponse>> = _loginViewModel

    private val _registerResponse = MediatorLiveData<Result<RegisterResponse>>()
    val registerResponse: LiveData<Result<RegisterResponse>> = _registerResponse

    fun register(name: String, email: String, password: String, confirmPassword: String) {
        val liveData = repository.register(name, email, password, confirmPassword)
        _registerResponse.addSource(liveData) { result ->
            _registerResponse.value = result
        }
    }

    fun login(email: String, password: String) {
        val liveData = repository.login(email, password)
        _loginViewModel.addSource(liveData) { result ->
            _loginViewModel.value = result
        }
    }
}