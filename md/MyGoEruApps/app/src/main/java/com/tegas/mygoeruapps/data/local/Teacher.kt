package com.tegas.mygoeruapps.data.local

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Teacher(
    val photo: Int,
    val name: String,
    val subject: String,
    val address: String,
    val age: Int
):Parcelable
