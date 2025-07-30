package io.github.caduviegas.carslist.di

import io.github.caduviegas.carslist.data.mapper.CarMapper
import io.github.caduviegas.carslist.data.mapper.OrderCarMapper
import org.koin.dsl.module

val mapperModule = module {
    single { CarMapper() }
    single { OrderCarMapper() }
}