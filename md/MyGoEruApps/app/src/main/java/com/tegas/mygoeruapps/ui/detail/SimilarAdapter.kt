package com.tegas.mygoeruapps.ui.detail

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tegas.mygoeruapps.data.response.RatingsItem
import com.tegas.mygoeruapps.data.response.SimilarGurusItem
import com.tegas.mygoeruapps.databinding.ReviewLayoutBinding
import com.tegas.mygoeruapps.databinding.TeacherRowBinding

class SimilarAdapter : RecyclerView.Adapter<SimilarAdapter.ListViewHolder>() {

    private val listOfSimilar = ArrayList<SimilarGurusItem>()

    fun addSimilarList(list: List<SimilarGurusItem>) {
        this.listOfSimilar.clear()
        this.listOfSimilar.addAll(list)
        notifyDataSetChanged()
    }

    inner class ListViewHolder(private val binding: TeacherRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(teacher: SimilarGurusItem) {
            val itemNow = listOfSimilar[position]

            binding.fullName.text = itemNow.nama
            binding.subject.text = itemNow.mapel
            binding.address.text = itemNow.asal
            binding.price.text = itemNow.harga.toString()

            binding.teacherRow.setOnClickListener {
                val intent = Intent(itemView.context, SimilarDetailActivity::class.java)
                intent.putExtra("teacherItem", teacher)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            TeacherRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return listOfSimilar.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listOfSimilar[position])
    }

}