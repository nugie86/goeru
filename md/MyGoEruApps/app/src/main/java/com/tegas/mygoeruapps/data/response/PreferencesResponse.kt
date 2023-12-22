package com.tegas.mygoeruapps.data.response

import com.google.gson.annotations.SerializedName

data class PreferencesResponse(

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("error")
	val error: String? = null
)
