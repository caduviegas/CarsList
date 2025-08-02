package io.github.caduviegas.carslist.di

import io.github.caduviegas.carslist.data.repository.CarApiRepositoryImpl
import io.github.caduviegas.carslist.data.repository.CarDatabaseRepositoryImpl
import io.github.caduviegas.carslist.domain.repository.CarApiRepository
import io.github.caduviegas.carslist.domain.repository.CarDatabaseRepository
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val repositoryModule = module {
    single<CarApiRepository> {
        CarApiRepositoryImpl(
            apiService = get(),
            mapper = get(),
            orderCarMapper = get(),
            dispatcher = Dispatchers.IO
        )
    }
    single<CarDatabaseRepository> {
        CarDatabaseRepositoryImpl(
            leadDao = get(),
            userDao = get()
        )
    }
}
