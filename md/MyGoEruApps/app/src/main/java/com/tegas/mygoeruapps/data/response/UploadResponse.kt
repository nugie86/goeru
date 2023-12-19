package com.tegas.mygoeruapps.data.response

import com.google.gson.annotations.SerializedName

data class UploadResponse(
    @SerializedName("message")
    val message: String
)
