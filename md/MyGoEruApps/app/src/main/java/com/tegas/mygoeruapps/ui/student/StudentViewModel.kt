package com.tegas.mygoeruapps.ui.student

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.tegas.mygoeruapps.data.Result
import com.tegas.mygoeruapps.data.UserModel
import com.tegas.mygoeruapps.data.UserRepository
import com.tegas.mygoeruapps.data.response.GuruItem
import kotlinx.coroutines.launch

class StudentViewModel(private val repository: UserRepository) : ViewModel() {

    private val _teacherListItem = MediatorLiveData<Result<List<GuruItem>>>()
    val teacherListItem: LiveData<Result<List<GuruItem>>> = _teacherListItem

    fun getTeachers() {
        val liveData = repository.getTeacher()
        _teacherListItem.addSource(liveData) { result ->
            _teacherListItem.value = result
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
}