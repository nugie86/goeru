package com.tegas.mygoeruapps.data.response

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class DetailResponse(

	@field:SerializedName("guru")
	val guru: Guru
)

data class RatingsItem(

	@field:SerializedName("nilai")
	val nilai: Any,

	@field:SerializedName("komentar")
	val komentar: String,

	@field:SerializedName("username_pemberi_rating")
	val usernamePemberiRating: String,

	@field:SerializedName("tanggal_rating")
	val tanggalRating: String
)


data class Guru(

	@field:SerializedName("asal")
	val asal: String,

	@field:SerializedName("similarGurus")
	val similarGurus: List<SimilarGurusItem>,

	@field:SerializedName("nama")
	val nama: String,

	@field:SerializedName("harga")
	val harga: Int,

	@field:SerializedName("ratings")
	val ratings: List<RatingsItem>,

	@field:SerializedName("id_user")
	val idUser: String,

	@field:SerializedName("deskripsi")
	val deskripsi: String,

	@field:SerializedName("gambar")
	val gambar: String,

	@field:SerializedName("mapel")
	val mapel: String
)

@Parcelize
data class SimilarGurusItem(

	@field:SerializedName("asal")
	val asal: String,

	@field:SerializedName("nama")
	val nama: String,

	@field:SerializedName("harga")
	val harga: Int,

	@field:SerializedName("id_user")
	val idUser: String,

	@field:SerializedName("gambar")
	val gambar: String,

	@field:SerializedName("mapel")
	val mapel: String
): Parcelable
