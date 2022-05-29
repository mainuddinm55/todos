package info.learncoding.todo.ui.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T : Any, VH : ViewDataBinding>(
    diffUtil: DiffUtil.ItemCallback<T>,
    private val listener: (item: T, isLong: Boolean) -> Unit
) : PagingDataAdapter<T, BaseAdapter.BaseViewHolder<VH>>(diffUtil) {

    protected abstract val layout: Int

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<VH> {
        val binding = DataBindingUtil.inflate<VH>(
            LayoutInflater.from(parent.context),
            layout,
            parent,
            false
        )

        val viewHolder = BaseViewHolder(binding)
        viewHolder.itemView.setOnClickListener {
            getItem(viewHolder.absoluteAdapterPosition)?.let {
                listener.invoke(it, false)
            }
        }
        viewHolder.itemView.setOnLongClickListener {
            getItem(viewHolder.absoluteAdapterPosition)?.let {
                listener.invoke(it, true)
                return@setOnLongClickListener true
            }
            return@setOnLongClickListener false
        }
        return viewHolder
    }

    class BaseViewHolder<VH : ViewDataBinding?>(val binding: VH) : RecyclerView.ViewHolder(
        binding!!.root
    )

}