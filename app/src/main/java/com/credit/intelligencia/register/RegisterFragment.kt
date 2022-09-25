package com.credit.intelligencia.register

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.credit.intelligencia.R
import com.credit.intelligencia.databinding.FragmentRegisterBinding
import com.credit.intelligencia.util.observeEvent
import com.credit.intelligencia.util.viewBinding
import com.credit.intelligencia.validators.EmailValidator
import com.credit.intelligencia.validators.EmptyValidator
import com.credit.intelligencia.validators.PasswordValidator
import com.credit.intelligencia.validators.base.BaseValidator
import org.koin.androidx.viewmodel.ext.android.viewModel



class RegisterFragment : Fragment(R.layout.fragment_register) {
    private val binding by viewBinding(FragmentRegisterBinding::bind)
    private val viewModel: RegisterViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        passData()
        setUpObservers()
        navigateToLogin()

    }

    private fun navigateToLogin() {
        binding.apply {
            signIn.setOnClickListener {
                viewModel.navigateToLogin()
            }
            toolbarHome.setNavigationIcon(R.drawable.ic_back)
            toolbarHome.setNavigationOnClickListener {
                viewModel.navigateToLogin()
            }
        }
    }

    private fun passData() {
        binding.apply {
            regContunue.setOnClickListener {
                when{
                    firtName.text.toString().isEmpty() ->{
                        Toast.makeText(
                            requireContext(), "Enter your FirstName",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    lastName.text.toString().isEmpty() ->{
                        Toast.makeText(
                            requireContext(), "Enter your LastName",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    regEmail.text.toString().isEmpty() ->{
                        Toast.makeText(
                            requireContext(), "Enter your Email",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    phoneNo.text.toString().isEmpty() ->{
                        Toast.makeText(
                            requireContext(), "Enter your Phone Number",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    idNo.text.toString().isEmpty() ->{
                        Toast.makeText(
                            requireContext(), "Enter your Id Number",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    password.text.toString().isEmpty() ->{
                        Toast.makeText(
                            requireContext(), "Enter your password",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else ->{
                        viewModel.register(
                            firstname = binding.firtName.text.toString() ,
                            lastname =binding.lastName.text.toString(),
                            email =binding.regEmail.text.toString(),
                            phone = binding.phoneNo.text.toString(),
                            idnum = binding.idNo.text.toString(),
                            password = binding.password.text.toString()
                        )
                        loading.isVisible = true
                        onRegisterFinished()


                    }

                }


            }

        }


}



    private fun setUpObservers() {
        viewModel.uiState.observe(viewLifecycleOwner) {
            when (it) {
                RegisterViewModel.RegisterUIState.Loading ->{}  //renderLoading()
                is RegisterViewModel.RegisterUIState.Error -> {
                    //renderError(errorTitle = it.title, errorMessage = it.message)
                }
                else -> {}
            }
        }

        viewModel.interactions.observeEvent(viewLifecycleOwner) {
            when (it) {
                is RegisterViewModel.RegisterActions.Navigate -> findNavController().navigate(it.destination)
            }
        }
    }


    private fun onRegisterFinished() {

        val sharedPref = requireActivity().getSharedPreferences("Register", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("Finished", true)
        editor.apply()

    }
}