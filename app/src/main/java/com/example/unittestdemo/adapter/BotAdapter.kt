package com.example.unittestdemo.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.unittestdemo.data.TYPE
import com.example.unittestdemo.data.TextModel
import com.example.unittestdemo.databinding.ItemTextBinding

class BotAdapter : RecyclerView.Adapter<BotAdapter.ViewHolder>() {

    private val list: MutableList<TextModel> = mutableListOf()
    private var onHelpChooseListener: OnHelpChooseListener? = null


    @SuppressLint("NotifyDataSetChanged")
    fun setData(dataList : MutableList<TextModel>) {
        list.clear()
        list.addAll(dataList)
        notifyDataSetChanged()
    }

    fun addItem(textModel: TextModel) {
        list.add(textModel)
        notifyItemInserted(list.size - 1)
    }

    fun deleteItem(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeRemoved(position, list.size)
    }

    inner class ViewHolder(binding: ItemTextBinding) : RecyclerView.ViewHolder(binding.root) {
        private val botText = binding.tvBot
        private val userText = binding.tvUser
        private val llBot = binding.llBot
        private val hint = binding.llHint
        private val choose = binding.llChoose

        private val choose1 = binding.tvChoose1
        private val choose2 = binding.tvChoose2

        fun bind(textModel: TextModel) {

            if (textModel.type == TYPE.BOT) {
                llBot.visibility = View.VISIBLE
                userText.visibility = View.GONE

                botText.text = textModel.text
            }

            if (textModel.type == TYPE.USER) {
                llBot.visibility = View.GONE
                userText.visibility = View.VISIBLE

                userText.text = textModel.text
            }

            hint.visibility = if (textModel.isHelp) View.VISIBLE else View.GONE
            choose.visibility = if (textModel.isHelp) View.VISIBLE else View.GONE
            choose1.setOnClickListener {
                onHelpChooseListener?.onClickHelpItem(choose1.text.toString())
                hint.visibility =  View.GONE
                choose.visibility = View.GONE
            }
            choose2.setOnClickListener {
                onHelpChooseListener?.onClickHelpItem(choose2.text.toString())
                hint.visibility =  View.GONE
                choose.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemTextBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    fun setOnHelpChooseListener(onHelpChooseListener: OnHelpChooseListener) {
        this.onHelpChooseListener = onHelpChooseListener
    }
    interface OnHelpChooseListener {
        fun onClickHelpItem(text: String)
    }
}