package com.tegas.mygoeruapps.data.response

import com.google.gson.annotations.SerializedName

data class PaymentResponse(

	@field:SerializedName("redirect_url")
	val redirectUrl: String,

	@field:SerializedName("token")
	val token: String
)
