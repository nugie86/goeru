package com.tegas.mygoeruapps.ui.old

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tegas.mygoeruapps.data.local.Teacher
import com.tegas.mygoeruapps.databinding.ItemRowBinding

class TeacherAdapter : RecyclerView.Adapter<TeacherAdapter.ListViewHolder>() {

    private var listOfTeacher = ArrayList<Teacher>()

    fun addTeacherList(list: List<Teacher>) {
        this.listOfTeacher.clear()
        this.listOfTeacher.addAll(list)
        notifyDataSetChanged()
    }

    inner class ListViewHolder(private val binding: ItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val itemNow = listOfTeacher[position]

            Glide
                .with(itemView.context)
                .load(itemNow.photo)
                .fitCenter()
                .into(binding.ivPhoto)

            binding.fullName.text = itemNow.name
            binding.subject.text = itemNow.subject
            binding.address.text = itemNow.address
            binding.age.text = itemNow.age.toString()
//
//            binding.itemLayout.setOnClickListener {
//                val intent = Intent(itemView.context, DetailActivity::class.java)
//                intent.putExtra("teacherData", itemNow)
//                itemView.context.startActivity(intent)
//            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            ItemRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return listOfTeacher.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(position)
    }
}