package com.credit.intelligencia.di

import com.credit.intelligencia.util.Environment
import org.koin.dsl.module

val environmentModule = module {
    single<Environment> { Environment.Main }
}