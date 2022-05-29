package info.learncoding.todo.ui.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import info.learncoding.todo.R
import info.learncoding.todo.databinding.FragmentAddTodoBinding
import info.learncoding.todo.ui.base.BaseFragment
import info.learncoding.todo.utils.AppConstraint
import info.learncoding.todo.utils.State
import info.learncoding.todo.utils.toast

@AndroidEntryPoint
class AddTodoFragment : BaseFragment<FragmentAddTodoBinding, AddTodoViewModel>() {

    override val viewModel: AddTodoViewModel by viewModels()
    private val args: AddTodoFragmentArgs by navArgs()

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAddTodoBinding {
        return FragmentAddTodoBinding.inflate(inflater, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding!!.viewModel = viewModel
        if (args.todo != null) {
            binding!!.toolbar.setTitle(R.string.update_todo)
        }
        observeTodoResponse()
    }

    private fun observeTodoResponse() {
        viewModel.todoResponse.observe(viewLifecycleOwner) {
            when (it) {
                is State.Error -> toast(it.message)
                State.Loading -> Unit
                is State.Success -> {
                    setFragmentResult(AppConstraint.TODO_CHANGE_LISTENER_KEY, bundleOf())
                    toast("Todo Added")
                    findNavController().navigateUp()
                }
            }
        }
    }
}