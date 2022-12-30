package hu.tandofer.android_kis_gep_szerviz.ui.common.adapter

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

abstract class DataBoundListAdapter<T, V : ViewDataBinding>(diffCallback: DiffUtil.ItemCallback<T>) :
    ListAdapter<T, DataBoundViewHolder<V>>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBoundViewHolder<V> {
        val binding = createBinding(parent, viewType)
        return DataBoundViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataBoundViewHolder<V>, position: Int) {
        bind(holder.binding, getItem(position), holder.itemViewType)
        holder.binding.executePendingBindings()
    }

    public override fun getItem(position: Int): T {
        return super.getItem(position)
    }

    protected abstract fun createBinding(parent: ViewGroup, viewType: Int): V
    protected abstract fun bind(binding: V, item: T?, viewType: Int)
}