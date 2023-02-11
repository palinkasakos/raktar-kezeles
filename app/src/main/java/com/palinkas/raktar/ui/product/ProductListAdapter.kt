package com.palinkas.raktar.ui.product

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.palinkas.raktar.R
import com.palinkas.raktar.databinding.ProductListItemBinding
import com.palinkas.raktar.db.entities.Product

class ProductListAdapter(
    private val lifecycle: LifecycleOwner,
    private val itemClick: (oid: String) -> Unit
) : RecyclerView.Adapter<ProductListAdapter.ViewHolders>() {
    private val differ = AsyncListDiffer<Any>(this, DIFF_CALLBACK)

    var items: List<Any> = emptyList()
        set(value) {
            field = buildMergedList(value)
            differ.submitList(field)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolders {
        return when (viewType) {
            R.layout.product_list_item -> ViewHolders.ProductListItemViewHolder(
                ProductListItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            else -> throw IllegalStateException("Unknown viewType")
        }
    }

    override fun onBindViewHolder(holder: ViewHolders, position: Int) {
        when (holder) {
            is ViewHolders.ProductListItemViewHolder -> holder.binding.apply {
                val model1 =
                    differ.currentList[position] as Product

                lifecycleOwner = lifecycle

                product = model1

                product?.oid?.let { oid ->
                    main.setOnClickListener {
                        itemClick.invoke(oid)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private fun buildMergedList(list: List<Any> = this.items): List<Any> {
        val merged = mutableListOf<Any>()

        merged.addAll(list)

        return merged
    }

    override fun getItemViewType(position: Int): Int =
        when (differ.currentList[position]) {
            is Product -> R.layout.product_list_item
            else -> super.getItemViewType(position)
        }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Any>() {
            override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean = when {
                oldItem::class != newItem::class -> false
                else -> oldItem == newItem
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
                return oldItem == newItem
            }
        }
    }

    sealed class ViewHolders(itemView: View) : RecyclerView.ViewHolder(itemView) {
        class ProductListItemViewHolder(val binding: ProductListItemBinding) :
            ViewHolders(binding.root)
    }
}