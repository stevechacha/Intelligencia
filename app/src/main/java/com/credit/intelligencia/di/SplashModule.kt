package com.credit.intelligencia.di

import com.credit.intelligencia.auth.AuthViewModel
import com.credit.intelligencia.splashscreen.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val splashModule = module {
    viewModel { SplashViewModel() }
}