package hu.tandofer.android_kis_gep_szerviz.ui.common.adapter

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil

abstract class DataBoundPagedListAdapter<T : Any, V : ViewDataBinding>(diffCallback: DiffUtil.ItemCallback<T>) :
    PagingDataAdapter<T, DataBoundViewHolder<V>>(diffCallback) {

    var changedListener: ListChangedListener? = null

    override fun onBindViewHolder(holder: DataBoundViewHolder<V>, position: Int) {
        bind(holder.binding, getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBoundViewHolder<V> {
        val binding = createBinding(parent)
        return DataBoundViewHolder(binding)
    }

    protected abstract fun createBinding(parent: ViewGroup): V
    protected abstract fun bind(binding: V, item: T?)
}

interface ListChangedListener {
    fun onChanged()
}