package com.tegas.mygoeruapps.data

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tegas.mygoeruapps.data.Injection.provideRepository
import com.tegas.mygoeruapps.ui.login.LoginViewModel
import com.tegas.mygoeruapps.ui.register.RegisterViewModel
import com.tegas.mygoeruapps.ui.splash.SplashViewModel
import com.tegas.mygoeruapps.ui.student.StudentViewModel
import com.tegas.mygoeruapps.ui.teacher.TeacherViewModel

class ViewModelFactory(private val repository: UserRepository): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(StudentViewModel::class.java) -> {
                StudentViewModel(repository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> {
                SplashViewModel(repository) as T
            }
            modelClass.isAssignableFrom(TeacherViewModel::class.java) -> {
                TeacherViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(provideRepository(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}