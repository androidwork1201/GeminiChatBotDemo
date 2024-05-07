package com.example.unittestdemo.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.unittestdemo.databinding.ItemChangeBinding

class ChooseItemAdapter : RecyclerView.Adapter<ChooseItemAdapter.ViewHolder>() {

    private val list: MutableList<String> = mutableListOf()
    private var onItemClickListener: OnItemClickListener? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setData(dataList: List<String>) {
        list.clear()
        list.addAll(dataList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(binding: ItemChangeBinding) : RecyclerView.ViewHolder(binding.root) {
        private val itemText = binding.tvItem

        fun bind(text: String) {
            itemText.text = text
            itemText.setOnClickListener {
                onItemClickListener?.onItemClick(text)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemChangeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(text: String)
    }
}