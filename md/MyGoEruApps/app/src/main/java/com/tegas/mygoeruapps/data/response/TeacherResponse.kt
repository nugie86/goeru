package com.tegas.mygoeruapps.data.response

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class TeacherResponse(

    @field:SerializedName("guru")
    val guru: List<GuruItem> = emptyList(),
)

@Parcelize
@Entity(tableName = "teacher")
data class GuruItem(

    @ColumnInfo("asal")
    @field:SerializedName("asal")
    val asal: String,

    @ColumnInfo("gambar")
    @field:SerializedName("gambar")
    val gambar: String,

    @ColumnInfo("nama")
    @field:SerializedName("nama")
    val nama: String,

    @PrimaryKey
    @ColumnInfo("id_user")
    @field:SerializedName("id_user")
    val idUser: String,

    @ColumnInfo("mapel")
    @field:SerializedName("mapel")
    val mapel: String,

    @ColumnInfo("harga")
    @field:SerializedName("harga")
    val harga: String
) : Parcelable
