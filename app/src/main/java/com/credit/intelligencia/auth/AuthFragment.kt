package com.credit.intelligencia.auth

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.credit.intelligencia.util.observeEvent
import com.credit.intelligencia.util.viewBinding
import com.credit.intelligencia.R
import com.credit.intelligencia.databinding.FragmentAuthBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

// TODO: Rename parameter arguments, choose names that match



class AuthFragment : Fragment(R.layout.fragment_auth) {
    private val binding by viewBinding(FragmentAuthBinding::bind)
    private val viewModel: AuthViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpObservers()
        onClick()

    }

    private fun onClick(){
        binding.apply {

           toSignIn.setOnClickListener {
               viewModel.navigateToLogin()
           }


           toSignUp.setOnClickListener {
               viewModel.navigateToRegister()
           }
       }


    }

    private fun setUpObservers() {
        viewModel.uiState.observe(viewLifecycleOwner) {
            when (it) {
                AuthUIState.Loading ->{}  //renderLoading()
                is AuthUIState.Error -> {
                    //renderError(errorTitle = it.title, errorMessage = it.message)
                }
                else -> {}
            }
        }

        viewModel.interactions.observeEvent(viewLifecycleOwner) {
            when (it) {
                is AuthActions.Navigate -> findNavController().navigate(it.destination)
            }
        }
    }


}