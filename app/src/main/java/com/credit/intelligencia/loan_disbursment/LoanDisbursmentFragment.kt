package com.credit.intelligencia.loan_disbursment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.credit.intelligencia.R
import com.credit.intelligencia.databinding.FragmentLoanDisbursmentBinding
import com.credit.intelligencia.util.observeEvent
import com.credit.intelligencia.util.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel




class LoanDisbursmentFragment : Fragment(R.layout.fragment_loan_disbursment) {
    // TODO: Rename and change types of parameters
    private val viewModel: LoanDisbursmentViewModel by viewModel()
    private val binding by viewBinding(FragmentLoanDisbursmentBinding::bind)
    private val args: LoanDisbursmentFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        passData()
        setUpObservers()
    }

    private fun setUpObservers() {
        viewModel.uiState.observe(viewLifecycleOwner) {
            when (it) {
                LoanDisbursmentUIState.Loading -> {}  //renderLoading()
                is LoanDisbursmentUIState.Error -> {
                    //renderError(errorTitle = it.title, errorMessage = it.message)
                }
                else -> {}
            }
        }

        viewModel.interactions.observeEvent(viewLifecycleOwner) {
            when (it) {
                is LoanDisbursmentActions.Navigate -> findNavController().navigate(it.destination)
            }
        }
    }

    private fun passData() {
        binding.apply {
            loanValue.text = args.userTransaction.amount.toString()
            totalAmountValue.text = args.userTransaction.totalAmount.toString()
            dueDateValue.text = args.userTransaction.date

        }

    }


}