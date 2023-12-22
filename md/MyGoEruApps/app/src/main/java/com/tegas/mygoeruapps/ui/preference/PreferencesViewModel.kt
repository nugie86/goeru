package com.tegas.mygoeruapps.ui.preference

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.tegas.mygoeruapps.data.Result
import com.tegas.mygoeruapps.data.UserModel
import com.tegas.mygoeruapps.data.UserRepository
import com.tegas.mygoeruapps.data.response.PreferencesResponse

class PreferencesViewModel(private val repository: UserRepository): ViewModel() {
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    private val _preferenceResponse = MediatorLiveData<Result<PreferencesResponse>>()
    val preferenceResponse: LiveData<Result<PreferencesResponse>> = _preferenceResponse

    fun postPreferences(
        token: String,
        usia: Int,
        math: Double,
        physics: Double,
        biology: Double,
        chemistry: Double,
        economy: Double,
        sosiology: Double,
        geography: Double,
        history: Double,
        antropology: Double
    ) {
        val liveData = repository.postPreferences(token,usia,math,physics,biology,chemistry,economy,sosiology,
            geography,history,antropology)
        _preferenceResponse.addSource(liveData) { result ->
            _preferenceResponse.value = result
        }
    }
}