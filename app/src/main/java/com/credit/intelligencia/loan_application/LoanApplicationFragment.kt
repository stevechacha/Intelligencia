package com.credit.intelligencia.loan_application

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.credit.intelligencia.R
import com.credit.intelligencia.databinding.FragmentLoanApplicationBinding
import com.credit.intelligencia.util.observeEvent
import com.credit.intelligencia.util.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDate
import java.time.ZoneId


class LoanApplicationFragment : Fragment(R.layout.fragment_loan_application) {

    private val viewModel: LoanApplicationViewModel by viewModel()
    private val binding by viewBinding(FragmentLoanApplicationBinding::bind)
    private val args: LoanApplicationFragmentArgs by navArgs()




    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        passData()
        setUpObservers()
        loanApply()

    }



    private fun passData() {
        binding.apply {
            creditScoreValue.text = args.creditLimits.score.toString()
            creditLimitValue.text = args.creditLimits.limit.toString()
            userUUID.text = args.email
        }
    }
    private fun setUpObservers() {
        viewModel.uiState.observe(viewLifecycleOwner) {
            when (it) {
                LoanApplicationtUIState.Loading -> {}  //renderLoading()
                is LoanApplicationtUIState.Error -> {
                    //renderError(errorTitle = it.title, errorMessage = it.message)
                }
                else -> {}
            }
        }
        viewModel.interactions.observeEvent(viewLifecycleOwner) {
            when (it) {
                is LoanApplicationActions.Navigate -> findNavController().navigate(it.destination)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loanApply() {
        binding.apply {
            submitLoan.setOnClickListener {
                when{
                    amount.text.toString().isEmpty() ->{
                        Toast.makeText(context,"Enter the amount you want to apply" ,Toast.LENGTH_LONG).show()


                    }
                    account.text.toString().isEmpty() ->{
                        Toast.makeText(context,"Enter Your account" ,Toast.LENGTH_LONG).show()

                    }
                    else ->{
                        val amount = binding.amount.text.toString().toInt()
                        val z: ZoneId = ZoneId.systemDefault()
                        val today: LocalDate = LocalDate.now(z)
                        val oneMonthLater = today.plusMonths(1)


                        if (amount <= args.creditLimits.limit){
                            val total = amount * 1 * 0.13 + amount
                            viewModel.navigateToDisbursements(
                                total,
                                amount,
                                date= oneMonthLater.toString(),
                            )
                        }
                        else{
                            showAlert()

                        }
                    }
                }

            }

        }

    }


    private fun showAlert() {

        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Loan Application")
        builder.setMessage(" You have exceeded your Limit, Check your Limit and try again.")
        builder.setNeutralButton("Cancel", null)
        val dialog = builder.create()
        dialog.show()


    }


}