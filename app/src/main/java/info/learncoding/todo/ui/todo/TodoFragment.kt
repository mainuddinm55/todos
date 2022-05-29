package info.learncoding.todo.ui.todo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import dagger.hilt.android.AndroidEntryPoint
import info.learncoding.todo.R
import info.learncoding.todo.data.models.Todo
import info.learncoding.todo.databinding.FragmentTodoBinding
import info.learncoding.todo.ui.base.BaseFragment
import info.learncoding.todo.utils.AppConstraint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class TodoFragment : BaseFragment<FragmentTodoBinding, TodoViewModel>() {

    override val viewModel: TodoViewModel by viewModels()
    private lateinit var todoAdapter: TodoAdapter

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentTodoBinding {
        return FragmentTodoBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setupRecyclerView()
        initClickEvent()
        registerFragmentListener()

        observeTodos()

    }

    private fun initClickEvent() {
        binding!!.addFab.setOnClickListener {
            findNavController().navigate(
                TodoFragmentDirections.actionTodoFragmentToAddTodoFragment(null)
            )
        }
        binding!!.toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.action_delete) {
                if (todoAdapter.hasSelectedItem()) {
                    deleteTodos(todoAdapter.getSelectedItems())
                }
                return@setOnMenuItemClickListener true
            } else if (it.itemId == R.id.action_logout) {
                viewModel.logout()
                findNavController().navigate(
                    TodoFragmentDirections.actionTodoFragmentToLoginFragment()
                )
                return@setOnMenuItemClickListener true
            }
            return@setOnMenuItemClickListener false
        }
    }

    private fun setupRecyclerView() {
        todoAdapter = TodoAdapter { item, isLongPressed ->
            if (!todoAdapter.hasSelectedItem() && !isLongPressed) {
                findNavController().navigate(
                    TodoFragmentDirections.actionTodoFragmentToAddTodoFragment(item)
                )
            }
            selectionItem(item, isLongPressed)
            toggleDeleteMenu()
            updateToolbarTitle()

        }
        todoAdapter.setDeletedItemListener {
            deleteTodos(listOf(it))
        }
        binding!!.todosRecyclerView.apply {
            adapter = todoAdapter
        }
        lifecycleScope.launchWhenResumed {
            todoAdapter.loadStateFlow.collectLatest {
                Log.d("TAG", "Todo : $it")
                when (it.refresh) {
                    is LoadState.Error -> {
                        showError((it.refresh as LoadState.Error).error.message)
                    }
                    is LoadState.Loading -> {
                        showProgress(true)
                    }
                    is LoadState.NotLoading -> {
                        showProgress(false)
                    }
                }
            }
        }
    }

    private fun registerFragmentListener() {
        setFragmentResultListener(AppConstraint.TODO_CHANGE_LISTENER_KEY) { requestKey, _ ->
            if (requestKey == AppConstraint.TODO_CHANGE_LISTENER_KEY) {
                todoAdapter.refresh()
            }
        }
    }

    private fun deleteTodos(todos: List<Todo>) {
        viewModel.deleteTodos(todos).observe(viewLifecycleOwner) {
            todoAdapter.refresh()
        }
    }

    private fun observeTodos() {
        lifecycleScope.launchWhenResumed {
            viewModel.todos.collectLatest {
                todoAdapter.submitData(it)
                showProgress(false)
            }
        }
    }

    private fun selectionItem(item: Todo, isLongPressed: Boolean) {
        if (todoAdapter.hasSelectedItem()) {
            item.selected = !todoAdapter.isItemSelected(item)
        } else {
            item.selected = isLongPressed
        }
    }

    private fun toggleDeleteMenu() {
        val menuItem = binding!!.toolbar.menu.findItem(R.id.action_delete)
        menuItem.isVisible = todoAdapter.hasSelectedItem()
    }

    private fun updateToolbarTitle() {
        if (todoAdapter.hasSelectedItem()) {
            binding!!.toolbar.title = getString(
                R.string.num_todos_selected, todoAdapter.getSelectedItems().size
            )
        } else {
            binding!!.toolbar.title = getString(R.string.todos)
        }
    }

    private fun showProgress(isShow: Boolean) {
        binding!!.progressBar.isVisible = isShow
        binding!!.errorMessageTextView.isVisible = false
    }

    private fun showError(msg: String?) {
        showProgress(false)
        binding!!.errorMessageTextView.text = msg
        binding!!.errorMessageTextView.isVisible = true
    }
}