package com.tegas.mygoeruapps.ui.student

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tegas.mygoeruapps.R
import com.tegas.mygoeruapps.data.response.GuruItem
import com.tegas.mygoeruapps.databinding.TeacherRowBinding
import com.tegas.mygoeruapps.ui.detail.DetailActivity
import java.util.Locale

class MyAdapter(private val originalTeacherList: List<GuruItem>) :
    RecyclerView.Adapter<MyAdapter.ListViewHolder>(), Filterable {

    private var filteredTeacherList: List<GuruItem> = originalTeacherList.toList()

    fun applyFilter(filteredSubject: String, filteredName: String, filterAddress: String) {
        filteredTeacherList = originalTeacherList.filter { teacher ->
            val teacherSubject = teacher.mapel?.toLowerCase(Locale.getDefault())
            val teacherName = teacher.nama?.toLowerCase(Locale.getDefault())
            val teacherAddress = teacher.asal?.toLowerCase(Locale.getDefault())

            teacherSubject!!.contains(filteredSubject) && teacherName!!.contains(filteredName) && teacherAddress!!.contains(filterAddress)
        }

        notifyDataSetChanged()
    }

    fun resetFilter() {
        filteredTeacherList = originalTeacherList.toList()
        notifyDataSetChanged()
    }


    inner class ListViewHolder(private val binding: TeacherRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(teacher: GuruItem) {

            binding.progressBar.visibility = View.VISIBLE

            if (teacher.gambar.isNullOrEmpty()) {
                Glide
                    .with(itemView)
                    .load(R.drawable.avatar)
                    .fitCenter()
                    .into(binding.ivPhoto)
            } else {
                Glide
                    .with(itemView)
                    .load(teacher.gambar)
                    .fitCenter()
                    .into(binding.ivPhoto)
            }
            binding.progressBar.visibility = View.GONE

            binding.fullName.text = teacher.nama
            binding.subject.text = teacher.mapel
            binding.address.text = teacher.asal
            binding.price.text = teacher.harga

            binding.teacherRow.setOnClickListener {
                val intent =Intent(itemView.context, DetailActivity::class.java)
//                intent.putExtra("teacherId", teacher.idUser)
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
        return filteredTeacherList.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val teacher = filteredTeacherList[position]
        holder.bind(teacher)
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = mutableListOf<GuruItem>()

                if (constraint.isNullOrBlank()) {
                    filterResults.addAll(originalTeacherList)
                } else {
                    val filterPattern = constraint.toString().toLowerCase(Locale.getDefault()).trim()

                    originalTeacherList.forEach { teacher ->
                        if (teacher.mapel?.toLowerCase(Locale.getDefault())?.contains(filterPattern) == true ||
                            teacher.nama?.toLowerCase(Locale.getDefault())?.contains(filterPattern) == true ||
                            teacher.asal?.toLowerCase(Locale.getDefault())?.contains(filterPattern) == true
                                ) {
                            filterResults.add(teacher)
                        }
                    }
                }
                val results = FilterResults()
                results.values = filterResults
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredTeacherList = results?.values as? List<GuruItem> ?: emptyList()
                notifyDataSetChanged()
            }
        }
    }
}