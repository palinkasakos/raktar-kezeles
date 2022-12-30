package hu.tandofer.android_kis_gep_szerviz.ui.common.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class DetailFragmentAdapter(
    private val diffCallback: DiffUtil.ItemCallback<Any>,
    private val lifecycleOwner: LifecycleOwner,
    private val headerLayout: Int,
    private val rowItemLayout: Int
) : RecyclerView.Adapter<DetailFragmentViewHolder>() {

    private lateinit var differ: AsyncListDiffer<Any>

    var itemList: List<Any> = emptyList()
        set(value) {
            field = value
            differ.submitList(buildMergedList(itemList))
        }

    init {
        initDiffer()
        differ.submitList(buildMergedList())
    }

    private fun initDiffer() {
        differ = AsyncListDiffer<Any>(this, diffCallback)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailFragmentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            headerLayout -> DetailFragmentViewHolder.HeaderViewHolder(
                (DataBindingUtil.inflate(
                    inflater,
                    headerLayout,
                    parent,
                    false
                ) as ViewDataBinding).apply {
                    createHeaderBinding(parent, this)
                }
            )
            rowItemLayout -> DetailFragmentViewHolder.ListViewHolder(
                (DataBindingUtil.inflate(
                    inflater,
                    rowItemLayout,
                    parent,
                    false
                ) as ViewDataBinding).apply {
                    createRowsBinding(parent, this)
                }
            )
            else -> throw IllegalStateException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: DetailFragmentViewHolder, position: Int) {
        when (holder) {
            is DetailFragmentViewHolder.HeaderViewHolder -> {
                holder.binding.apply {
                    bindHeader(this)
                    lifecycleOwner = this@DetailFragmentAdapter.lifecycleOwner
                    executePendingBindings()
                }
            }
            is DetailFragmentViewHolder.ListViewHolder -> {
                holder.binding.apply {
                    bindRows(differ.currentList[position], this)
                    lifecycleOwner = this@DetailFragmentAdapter.lifecycleOwner
                    executePendingBindings()
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (differ.currentList[position]) {
            is Header -> headerLayout
            is Any -> rowItemLayout
            else -> throw IllegalStateException("Unknown view type at position $position")
        }
    }

    override fun getItemCount() = differ.currentList.size

    private fun buildMergedList(listSession: List<Any> = itemList): List<Any> {
        val merged = mutableListOf<Any>(Header)
        if (listSession.isNotEmpty()) {
            merged.addAll(listSession)
        }
        return merged
    }

    // Marker objects for use in our merged representation.
    object Header

    protected abstract fun bindHeader(binding: ViewDataBinding)
    protected abstract fun createHeaderBinding(parent: ViewGroup, binding: ViewDataBinding)
    protected abstract fun bindRows(currentItem: Any?, binding: ViewDataBinding)
    protected abstract fun createRowsBinding(parent: ViewGroup, binding: ViewDataBinding)
}

/**
 * [RecyclerView.ViewHolder] types used by this adapter.
 */
sealed class DetailFragmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    class HeaderViewHolder(val binding: ViewDataBinding) : DetailFragmentViewHolder(binding.root)
    class ListViewHolder(val binding: ViewDataBinding) : DetailFragmentViewHolder(binding.root)
}