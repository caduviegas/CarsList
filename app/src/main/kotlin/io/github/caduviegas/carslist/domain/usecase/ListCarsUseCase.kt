package io.github.caduviegas.carslist.domain.usecase

import io.github.caduviegas.carslist.domain.model.Car
import io.github.caduviegas.carslist.domain.repository.CarApiRepository

class ListCarsUseCase(
    private val repository: CarApiRepository
) {
    suspend operator fun invoke(): List<Car> {
        return repository.fetchCars()
    }
}