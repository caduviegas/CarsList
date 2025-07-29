package io.github.caduviegas.carslist.domain.repository

import io.github.caduviegas.carslist.domain.model.Car

interface CarRepository {
    suspend fun fetchCars(): List<Car>
}