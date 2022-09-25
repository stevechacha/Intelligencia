package com.credit.intelligencia.di

import com.credit.intelligencia.loan_disbursment.LoanDisbursmentViewModel
import com.credit.intelligencia.register.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.converter.moshi.MoshiConverterFactory


val loanDisbursmentModule = module {
    single { MoshiConverterFactory.create(get()).asLenient() }

    viewModel { LoanDisbursmentViewModel(get()) }
}