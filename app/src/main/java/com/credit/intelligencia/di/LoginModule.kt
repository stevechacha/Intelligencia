package com.credit.intelligencia.di

import com.credit.intelligencia.login.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.converter.moshi.MoshiConverterFactory

val loginModule = module {
    single { MoshiConverterFactory.create(get()).asLenient() }

    viewModel { LoginViewModel(get()) }
}