package info.learncoding.todo.ui.todo

import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.recyclerview.widget.DiffUtil
import info.learncoding.todo.R
import info.learncoding.todo.data.models.Todo
import info.learncoding.todo.databinding.RowItemTodoBinding
import info.learncoding.todo.ui.base.BaseAdapter


class TodoAdapter(
    private val listener: (item: Todo, isLongPressed: Boolean) -> Unit
) : BaseAdapter<Todo, RowItemTodoBinding>(diffUtilCallback, listener) {

    private var deletedItem: ((item: Todo) -> Unit)? = null
    fun setDeletedItemListener(listener: (item: Todo) -> Unit) {
        this.deletedItem = listener
    }

     private val isSelectionMode = ObservableBoolean(false)

    companion object {
        private val diffUtilCallback = object : DiffUtil.ItemCallback<Todo>() {
            override fun areItemsTheSame(oldItem: Todo, newItem: Todo): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean {
                return oldItem == newItem
            }
        }

    }

    override val layout: Int = R.layout.row_item_todo

    override fun onBindViewHolder(holder: BaseViewHolder<RowItemTodoBinding>, position: Int) {
        holder.binding.todo = getItem(position) ?: return
        holder.binding.isSelectionMode = isSelectionMode
        holder.binding.deleteImageView.setOnClickListener {
            deletedItem?.invoke(getItem(position)!!)
        }
        holder.binding.selectCheckBox.setOnCheckedChangeListener { _, isChecked ->
            val item = getItem(holder.absoluteAdapterPosition)?:return@setOnCheckedChangeListener
            if (item.selected!=isChecked){
                listener.invoke(getItem(position) ?: return@setOnCheckedChangeListener, true)
            }
            isSelectionMode.set(hasSelectedItem())
        }
    }


    fun getSelectedItems(): List<Todo> {
        return snapshot().items.filter { it.selected }
    }

    fun hasSelectedItem(): Boolean {
        return snapshot().items.find {
            it.selected
        } != null
    }

    fun isItemSelected(item: Todo): Boolean {
        return snapshot().items.find {
            it.selected && it.id == item.id
        } != null
    }
}