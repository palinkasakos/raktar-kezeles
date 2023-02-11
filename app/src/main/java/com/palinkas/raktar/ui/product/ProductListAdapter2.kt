package com.palinkas.raktar.ui.product

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.palinkas.raktar.databinding.ProductListItemBinding
import com.palinkas.raktar.db.entities.Product
import hu.tandofer.android_kis_gep_szerviz.ui.common.adapter.DataBoundPagedListAdapter

class ProductListAdapter2(
    private val itemClick: (oid: String) -> Unit
) : DataBoundPagedListAdapter<Product,ProductListItemBinding>(DIFF_CALLBACK) {

    override fun createBinding(parent: ViewGroup): ProductListItemBinding {
        return ProductListItemBinding.inflate(LayoutInflater.from(parent.context), parent,false)
    }

    override fun bind(binding: ProductListItemBinding, item: Product?) {
        binding.product = item

        item?.oid?.let {oid->
            binding.main.setOnClickListener {
                itemClick.invoke(oid)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Product>(){
            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem.oid == newItem.oid
            }

            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem == newItem
            }
        }
    }
}