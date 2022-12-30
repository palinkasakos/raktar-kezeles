package hu.tandofer.android_kis_gep_szerviz.ui.common.adapter

import androidx.databinding.ViewDataBinding

class DataBoundViewHolder<T : ViewDataBinding>(val binding: T) :
    androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root)