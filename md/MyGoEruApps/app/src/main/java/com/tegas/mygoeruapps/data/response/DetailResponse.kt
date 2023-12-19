package com.tegas.mygoeruapps.data.response

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class DetailResponse(

	@field:SerializedName("guru")
	val guru: Guru,

	@field:SerializedName("rating")
	val rating: Rating,
)

@Entity(tableName = "detail")
data class Guru(

	@ColumnInfo("asal")
	@field:SerializedName("asal")
	val asal: String,

	@ColumnInfo("nama")
	@field:SerializedName("nama")
	val nama: String,

	@PrimaryKey
	@ColumnInfo("id_user")
	@field:SerializedName("id_user")
	val idUser: String,

	@ColumnInfo("gambar")
	@field:SerializedName("gambar")
	val gambar: String,

	@ColumnInfo("mapel")
	@field:SerializedName("mapel")
	val mapel: String,

	@ColumnInfo("harga")
	@field:SerializedName("harga")
	val harga: Int,

	@ColumnInfo("deskripsi")
	@field:SerializedName("deskripsi")
	val deskripsi: String,

)
data class Rating(

	@field:SerializedName("nilai")
	val nilai: Any,

	@field:SerializedName("komentar")
	val komentar: String,

	@field:SerializedName("tanggal_rating")
	val tanggalRating: String,

	@field:SerializedName("username_pemberi_rating")
	val usernamePemberiRating: String
)
