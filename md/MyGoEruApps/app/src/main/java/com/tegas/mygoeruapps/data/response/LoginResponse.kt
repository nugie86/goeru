package com.tegas.mygoeruapps.data.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("role")
	val role: Int,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("userId")
	val userId: String,

	@field:SerializedName("token")
	val token: String,

	@field:SerializedName("username")
	val username: String,
)
