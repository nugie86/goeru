package com.tegas.mygoeruapps.data.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("token")
	val token: String
)
