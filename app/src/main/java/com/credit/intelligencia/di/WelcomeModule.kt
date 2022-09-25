package com.credit.intelligencia.di

import com.credit.intelligencia.welcome.WelcomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val welcomeModule = module {

    viewModel { WelcomeViewModel() }
}