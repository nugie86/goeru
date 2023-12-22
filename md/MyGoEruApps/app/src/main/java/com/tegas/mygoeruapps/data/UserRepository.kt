package com.tegas.mygoeruapps.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.tegas.mygoeruapps.data.local.TransactionRequest
import com.tegas.mygoeruapps.data.pref.UserPreference
import com.tegas.mygoeruapps.data.response.DescriptionResponse
import com.tegas.mygoeruapps.data.response.DetailResponse
import com.tegas.mygoeruapps.data.response.Guru
import com.tegas.mygoeruapps.data.response.GuruItem
import com.tegas.mygoeruapps.data.response.LoginResponse
import com.tegas.mygoeruapps.data.response.PaymentResponse
import com.tegas.mygoeruapps.data.response.PreferencesResponse
import com.tegas.mygoeruapps.data.response.RegisterResponse
import com.tegas.mygoeruapps.data.response.UploadResponse
import com.tegas.mygoeruapps.data.retrofit.ApiService
import com.tegas.mygoeruapps.ui.preference.PreferencesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {

    private suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    fun login(
        email: String,
        password: String
    ): LiveData<Result<LoginResponse>> =
        liveData(Dispatchers.IO) {
            emit(Result.Loading)
            try {
                val response = apiService.login(email, password)
                val token = response.token
                val role = response.role
                val id = response.userId
                val name = response.username
                Log.d("login", "token: $token")
                saveSession(UserModel(email, token, role = role, id = id, name = name))
                emit(Result.Success(response))
            } catch (e: Exception) {
                emit(Result.Error(e.message.toString()))
            }
        }

    fun register(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): LiveData<Result<RegisterResponse>> =
        liveData(Dispatchers.IO) {
            emit(Result.Loading)
            try {
                val response = apiService.register(name, email, password, confirmPassword)
                emit(Result.Success(response))
            } catch (e: Exception) {
                emit(Result.Error(e.message.toString()))
            }
        }

    fun getTeacher(): LiveData<Result<List<GuruItem>>> =
        liveData(Dispatchers.IO) {
            emit(Result.Loading)
            try {
                val response = apiService.getTeacher()
                val teachers = response.guru
                emit(Result.Success(teachers))
            } catch (e: Exception) {
                emit(Result.Error(e.message.toString()))
            }
        }

    fun uploadImage(
        token: String,
        file: MultipartBody.Part
    ): LiveData<Result<UploadResponse>> =
        liveData(Dispatchers.IO) {
            emit(Result.Loading)
            try {
                val response = apiService.postImage(
                    "Bearer $token", file
                )
                emit(Result.Success(response))
            } catch (e: Exception) {
                emit(Result.Error(e.message.toString()))
            }
        }

    fun postDescription(
        token: String,
        deskripsi: String
    ): LiveData<Result<DescriptionResponse>> =
        liveData(Dispatchers.IO) {
            emit(Result.Loading)
            try {
                val response = apiService.postDescription(
                    "Bearer $token", deskripsi
                )
                emit(Result.Success(response))
            } catch (e: Exception) {
                emit(Result.Error(e.message.toString()))
            }
        }

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
    ): LiveData<Result<PreferencesResponse>> =
        liveData(Dispatchers.IO) {
            emit(Result.Loading)
            try {
                val response = apiService.postPreferences(
                    "Bearer $token",
                    usia,
                    math,
                    physics,
                    biology,
                    chemistry,
                    economy,
                    sosiology,
                    geography,
                    history,
                    antropology
                )
                emit(Result.Success(response))
            } catch (e: Exception) {
                emit(Result.Error(e.message.toString()))
            }
        }

//    suspend fun makePayment(authorization: String, contentType: String, accept: String, transactionRequest: TransactionRequest): PaymentResponse {
//        return apiService.payment(authorization, contentType, accept, transactionRequest)
//    }

//    fun getTeacherDetail(id: String): LiveData<Result<Guru>> =
//        liveData(Dispatchers.IO) {
//            emit(Result.Loading)
//            try {
//                val response = apiService.getTeacherDetail(id)
//                val detail = response.guru
//                emit(Result.Success(detail))
//            } catch (e: Exception) {
//                emit(Result.Error(e.message.toString()))
//        }
//}

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, apiService)
            }.also { instance = it }
    }
}