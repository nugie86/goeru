package com.tegas.mygoeruapps.data.retrofit

import com.tegas.mygoeruapps.data.local.TransactionRequest
import com.tegas.mygoeruapps.data.response.DescriptionResponse
import com.tegas.mygoeruapps.data.response.DetailResponse
import com.tegas.mygoeruapps.data.response.LoginResponse
import com.tegas.mygoeruapps.data.response.PaymentResponse
import com.tegas.mygoeruapps.data.response.PreferencesResponse
import com.tegas.mygoeruapps.data.response.RegisterResponse
import com.tegas.mygoeruapps.data.response.TeacherResponse
import com.tegas.mygoeruapps.data.response.UploadResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @FormUrlEncoded
    @POST("auth/v1/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @FormUrlEncoded
    @POST("auth/v1/register")
    suspend fun register(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("confirmPassword") confirmPassword: String
    ): RegisterResponse

    @GET("user/v1/allguru")
    suspend fun getTeacher(): TeacherResponse

    @GET("user/v1/{id}")
    fun getTeacherDetail(
        @Path("id") id: String
    ): Call<DetailResponse>

    @Multipart
    @POST("guru/v1/uploadGambar")
    suspend fun postImage(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part
    ): UploadResponse

    @FormUrlEncoded
    @PUT("guru/v1/deskripsi")
    suspend fun postDescription(
        @Header("Authorization") token: String,
        @Field("deskripsi") deskripsi: String
    ): DescriptionResponse

    @POST("snap/v1/transactions")
    suspend fun payment(
        @Header("Content-Type") contentType: String,
        @Header("Accept") accept: String,
        @Header("Authorization") authorization: String,
        @Body request: TransactionRequest
    ): PaymentResponse

    @FormUrlEncoded
    @POST("user/v1/preferensi")
    suspend fun postPreferences(
        @Header("Authorization") token: String,
        @Field("usia") usia: Int,
        @Field("guru_favorit_Mathematics") math: Double,
        @Field("guru_favorit_Physics") physics: Double,
        @Field("guru_favorit_Biology") biology: Double,
        @Field("guru_favorit_Chemistry") chemistry: Double,
        @Field("guru_favorit_Economy") economy: Double,
        @Field("guru_favorit_Sosiology") sosiology: Double,
        @Field("guru_favorit_Geography") geography: Double,
        @Field("guru_favorit_History") history: Double,
        @Field("guru_favorit_Antropology") antropology: Double,
    ): PreferencesResponse
}