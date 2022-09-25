package com.credit.intelligencia.login

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.amrdeveloper.lottiedialog.LottieDialog
import com.credit.intelligencia.R
import com.credit.intelligencia.databinding.FragmentLoginBinding
import com.credit.intelligencia.util.observeEvent
import com.credit.intelligencia.util.viewBinding
import com.credit.intelligencia.validators.EmailValidator
import com.credit.intelligencia.validators.EmptyValidator
import com.credit.intelligencia.validators.PasswordValidator
import com.credit.intelligencia.validators.base.BaseValidator
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.credit.intelligencia.login.LoginViewModel.LoginActions
import com.credit.intelligencia.login.LoginViewModel.LoginUIState
import okhttp3.internal.userAgent
import timber.log.Timber


class LoginFragment : Fragment(R.layout.fragment_login) {
    private val viewModel: LoginViewModel by viewModel()
    private val binding by viewBinding(FragmentLoginBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        navigateToWelcomeLoan()
        setUpObservers()
        navigateToRegister()

    }


    private fun navigateToRegister() {
        binding.apply {
            toolbarHome.setNavigationIcon(R.drawable.ic_back)
            toolbarHome.setNavigationOnClickListener {
                viewModel.navigateToRegister()
            }
            signUp.setOnClickListener {
                viewModel.navigateToRegister()
            }
        }
    }

    private fun setUpObservers() {
        viewModel.uiState.observe(viewLifecycleOwner) {
            when (it) {
                LoginUIState.Loading -> {
                    showLoading(true)

                }  //renderLoading()
                is LoginUIState.Error -> {
                    renderError(it.message)

                }

                else -> {}
            }
        }

        viewModel.interactions.observeEvent(viewLifecycleOwner) {
            when (it) {
                is LoginActions.Navigate -> findNavController().navigate(it.destination)
            }
        }
    }

    private fun showLoading(loading: Boolean) {


    }

    private fun renderError(message: String) {
        binding.apply {
            loading.isVisible = false
            Toast.makeText(requireContext(),message,Toast.LENGTH_SHORT).show()
        }
    }


    private fun navigateToWelcomeLoan() {
        binding.apply {
            actionSignIn.setOnClickListener {
                when {
                    userEmail.text.toString().isEmpty() -> {
                        Toast.makeText(
                            requireContext(), "Enter Your Email",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    userPassword.text.toString().isEmpty() -> {
                        Toast.makeText(
                            requireContext(), "Enter Your Password",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                    else -> {
                        viewModel.loginUser(
                            email = binding.userEmail.text.toString(),
                            password = binding.userPassword.text.toString()


                        )
                        loading.isVisible = true
                        onLoginFinished()
                    }

                }


            }

        }
    }

        private fun onLoginFinished() {
            val sharedPref = requireActivity().getSharedPreferences("Login", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putBoolean("Finished", true)
            editor.apply()
        }




}


