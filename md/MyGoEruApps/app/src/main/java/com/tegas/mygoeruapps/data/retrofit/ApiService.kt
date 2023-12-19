package com.tegas.mygoeruapps.data.retrofit

import com.tegas.mygoeruapps.data.response.DetailResponse
import com.tegas.mygoeruapps.data.response.LoginResponse
import com.tegas.mygoeruapps.data.response.RegisterResponse
import com.tegas.mygoeruapps.data.response.TeacherResponse
import com.tegas.mygoeruapps.data.response.UploadResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
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
}