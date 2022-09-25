package com.credit.intelligencia.di

import org.koin.dsl.module
import retrofit2.converter.moshi.MoshiConverterFactory

val networkModule = module {
    single { MoshiConverterFactory.create(get()).asLenient() }
}