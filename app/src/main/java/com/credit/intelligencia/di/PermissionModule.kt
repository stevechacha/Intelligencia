package com.credit.intelligencia.di


import com.credit.intelligencia.permission.PermissionViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.converter.moshi.MoshiConverterFactory

val permissionModule = module {
    viewModel { PermissionViewModel(api=get(),context =get()) }


}