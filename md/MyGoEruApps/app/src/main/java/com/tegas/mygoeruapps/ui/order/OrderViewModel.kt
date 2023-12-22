package com.tegas.mygoeruapps.ui.order

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.tegas.mygoeruapps.data.UserModel
import com.tegas.mygoeruapps.data.UserRepository
import com.tegas.mygoeruapps.data.local.TransactionRequest
import com.tegas.mygoeruapps.data.response.PaymentResponse
import com.tegas.mygoeruapps.data.retrofit.ApiService
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class OrderViewModel(private val repository: UserRepository): ViewModel() {
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    private val BASE_URL = "https://app.sandbox.midtrans.com/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService: ApiService = retrofit.create(ApiService::class.java)

    private val _paymentResponse = MutableLiveData<PaymentResponse>()
    val paymentResponse: LiveData<PaymentResponse> get() = _paymentResponse

    fun makePayment(authorization: String, contentType: String, accept: String, transactionRequest: TransactionRequest) {
        viewModelScope.launch {
            try {
                val response = apiService.payment(authorization, contentType, accept, transactionRequest)
                _paymentResponse.value = response
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}