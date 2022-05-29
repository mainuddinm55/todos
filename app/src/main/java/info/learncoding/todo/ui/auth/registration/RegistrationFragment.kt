package info.learncoding.todo.ui.auth.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import info.learncoding.todo.databinding.FragmentRegistrationBinding
import info.learncoding.todo.ui.base.BaseFragment
import info.learncoding.todo.utils.State
import info.learncoding.todo.utils.toast

@AndroidEntryPoint
class RegistrationFragment : BaseFragment<FragmentRegistrationBinding, RegistrationViewModel>() {

    override val viewModel: RegistrationViewModel by viewModels()

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentRegistrationBinding {
        return FragmentRegistrationBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeLoginResponse()

        binding!!.loginTextView.setOnClickListener {
            findNavController().navigateUp()
        }

        binding!!.registerButton.setOnClickListener {
            viewModel.login(
                binding!!.nameEditText.text.toString(),
                binding!!.emailEditText.text.toString(),
                binding!!.passwordEditText.text.toString(),
                binding!!.confirmPasswordEditText.text.toString()
            )
        }
    }

    private fun observeLoginResponse() {
        viewModel.registrationResponse.observe(viewLifecycleOwner) {
            when (it) {
                is State.Error -> {
                    toast(it.message)
                }
                State.Loading -> {

                }
                is State.Success -> {
                    findNavController().navigate(
                        RegistrationFragmentDirections.actionRegistrationFragmentToTodoFragment()
                    )
                }
            }
        }
    }
}