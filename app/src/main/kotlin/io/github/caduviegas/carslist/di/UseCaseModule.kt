package io.github.caduviegas.carslist.di

import io.github.caduviegas.carslist.domain.usecase.HasLoggedUserUseCase
import io.github.caduviegas.carslist.domain.usecase.ListCarsUseCase
import io.github.caduviegas.carslist.domain.usecase.ListLeadsByCarUseCase
import io.github.caduviegas.carslist.domain.usecase.LoginUseCase
import io.github.caduviegas.carslist.domain.usecase.LogoutUseCase
import io.github.caduviegas.carslist.domain.usecase.SaveLeadUseCase
import io.github.caduviegas.carslist.domain.usecase.SyncLeadUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory { HasLoggedUserUseCase(get()) }
    factory { ListCarsUseCase(get()) }
    factory { ListLeadsByCarUseCase(get()) }
    factory { LoginUseCase(get()) }
    factory { LogoutUseCase(get()) }
    factory { SaveLeadUseCase(get()) }
    factory { SyncLeadUseCase(get(), get()) }
}