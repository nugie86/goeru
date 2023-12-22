package com.tegas.mygoeruapps.ui.teacher

import android.media.Rating
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tegas.mygoeruapps.data.response.RatingsItem
import com.tegas.mygoeruapps.databinding.ReviewLayoutBinding

class ReviewAdapter : RecyclerView.Adapter<ReviewAdapter.ListViewHolder>() {

    private val listOfReview = ArrayList<RatingsItem>()

    fun addReviewList(list: List<RatingsItem>) {
        this.listOfReview.clear()
        this.listOfReview.addAll(list)
        notifyDataSetChanged()
    }
    inner class ListViewHolder(private val binding: ReviewLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val itemNow = listOfReview[position]

            binding.fullName.text = itemNow.usernamePemberiRating
            binding.value.text = itemNow.nilai.toString()
            binding.time.text = itemNow.tanggalRating
            binding.comment.text = itemNow.komentar
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            ReviewLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return listOfReview.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(position)
    }

}