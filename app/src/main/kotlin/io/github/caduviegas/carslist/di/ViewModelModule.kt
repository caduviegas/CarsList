package io.github.caduviegas.carslist.di

import io.github.caduviegas.carslist.presentation.cardetail.CarDetailsViewModel
import io.github.caduviegas.carslist.presentation.carlist.CarListViewModel
import io.github.caduviegas.carslist.presentation.home.HomeViewModel
import io.github.caduviegas.carslist.presentation.leads.LeadsViewModel
import io.github.caduviegas.carslist.presentation.login.LoginViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { CarListViewModel(get()) }
    viewModel { CarDetailsViewModel(get(), get(), get()) }
    viewModel { LeadsViewModel() }
}