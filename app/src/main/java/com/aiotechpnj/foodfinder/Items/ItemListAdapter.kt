package com.aiotechpnj.foodfinder.Items

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aiotechpnj.foodfinder.R
import com.aiotechpnj.foodfinder.data.Item
import com.aiotechpnj.foodfinder.databinding.ItemLayoutBinding
import com.bumptech.glide.Glide

class ItemListAdapter: ListAdapter<Item, ItemListAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
    }

    inner class ViewHolder(private val binding: ItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Item){
            binding.apply {
                Glide.with(imgItem.context)
                    .load(Uri.parse(data.image))
                    .error(R.drawable.error_image)
                    .into(imgItem)

                nameItem.text = data.name
                typeItem.text = data.category
                caloriesItem.text = data.calories.toString()
                carbohydrateItem.text = data.carbohydrate.toString()
                fatItem.text = data.fat.toString()
                proteinItem.text = data.protein.toString()
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Item>(){
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem.name == newItem.name
            }
        }
    }
}