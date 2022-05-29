package info.learncoding.todo.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel

abstract class BaseFragment<VB : ViewDataBinding, V : ViewModel> : Fragment() {
    protected var binding: VB? = null
        private set

    protected abstract val viewModel: V

    abstract fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = inflateViewBinding(inflater, container)
        return binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}