package info.learncoding.todo.ui.auth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import info.learncoding.todo.databinding.FragmentLoginBinding
import info.learncoding.todo.ui.base.BaseFragment
import info.learncoding.todo.data.models.State
import info.learncoding.todo.utils.toast

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>() {

    override val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (viewModel.isLoggedIn()) {
            findNavController().navigate(
                LoginFragmentDirections.actionLoginFragmentToTodoFragment()
            )
        }
    }

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLoginBinding {
        return FragmentLoginBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeLoginResponse()

        binding!!.signupTextView.setOnClickListener {
            findNavController().navigate(
                LoginFragmentDirections.actionLoginFragmentToRegistrationFragment()
            )
        }

        binding!!.loginButton.setOnClickListener {
            viewModel.login(
                binding!!.emailEditText.text.toString(),
                binding!!.passwordEditText.text.toString()
            )
        }
    }

    private fun observeLoginResponse() {
        viewModel.loginResponse.observe(viewLifecycleOwner) {
            when (it) {
                is State.Error -> {
                    toast(it.message)
                }
                State.Loading -> {

                }
                is State.Success -> {
                    findNavController().navigate(
                        LoginFragmentDirections.actionLoginFragmentToTodoFragment()
                    )
                }
            }
        }
    }
}