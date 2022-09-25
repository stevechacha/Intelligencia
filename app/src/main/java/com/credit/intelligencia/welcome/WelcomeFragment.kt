package com.credit.intelligencia.welcome

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.credit.intelligencia.R
import com.credit.intelligencia.databinding.FragmentWelcomeBinding
import com.credit.intelligencia.loan_application.LoanApplicationFragmentArgs
import com.credit.intelligencia.util.observeEvent
import com.credit.intelligencia.util.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class WelcomeFragment : Fragment(R.layout.fragment_welcome) {
    private val binding by viewBinding (FragmentWelcomeBinding::bind)
    private val viewModel: WelcomeViewModel by viewModel()
    private val args: WelcomeFragmentArgs by navArgs()

//    private val userEmail = args.email


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        navigateToPermission()
        setUpObservers()

    }

    private fun navigateToPermission() {
        binding.apply {

            applyy.setOnClickListener {
                loading.isVisible = true
                viewModel.navigateToPermission(
                    email = args.email
                )
            }
        }

    }

    private fun setUpObservers() {
            viewModel.uiState.observe(viewLifecycleOwner) {
                when (it) {
                    WelcomeUIState.Loading -> {

                    }  //renderLoading()
                    is WelcomeUIState.Error -> {
                        //renderError(errorTitle = it.title, errorMessage = it.message)
                    }

                    else -> {}
                }
            }

            viewModel.interactions.observeEvent(viewLifecycleOwner) {
                when (it) {
                    is WelcomeActions.Navigate -> findNavController().navigate(it.destination)
                }
            }
        }



}