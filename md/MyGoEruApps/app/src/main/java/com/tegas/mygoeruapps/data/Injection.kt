package com.tegas.mygoeruapps.data

import android.content.Context
import com.tegas.mygoeruapps.data.UserRepository
import com.tegas.mygoeruapps.data.pref.UserPreference
import com.tegas.mygoeruapps.data.pref.dataStore
import com.tegas.mygoeruapps.data.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.apiService()
        return UserRepository.getInstance(pref, apiService)
    }
}