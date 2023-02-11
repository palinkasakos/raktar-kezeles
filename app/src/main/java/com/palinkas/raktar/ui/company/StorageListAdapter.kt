package com.palinkas.raktar.ui.company

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.palinkas.raktar.databinding.StorageListItemBinding
import com.palinkas.raktar.db.entities.Storage
import com.palinkas.raktar.ui.common.adapter.DataBoundListAdapter

class StorageListAdapter(
    private val itemClick: (oid: String) -> Unit
) : DataBoundListAdapter<Storage, StorageListItemBinding>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Storage>(){
            override fun areItemsTheSame(oldItem: Storage, newItem: Storage): Boolean {
                return oldItem.oid == newItem.oid
            }

            override fun areContentsTheSame(oldItem: Storage, newItem: Storage): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun createBinding(parent: ViewGroup, viewType: Int): StorageListItemBinding {
        return StorageListItemBinding.inflate(LayoutInflater.from(parent.context), parent,false)
    }

    override fun bind(binding: StorageListItemBinding, item: Storage?, viewType: Int) {
        binding.storage = item

        item?.oid?.let {oid->
            binding.main.setOnClickListener {
                itemClick.invoke(oid)
            }
        }
    }
}