package io.github.caduviegas.carslist.domain.usecase

import io.github.caduviegas.carslist.domain.model.Lead
import io.github.caduviegas.carslist.domain.repository.CarDatabaseRepository

class ListLeadsByCarUseCase(
    private val repository: CarDatabaseRepository
) {
    suspend operator fun invoke(carId: Int): List<Lead> {
        return repository.getLeadsByCarId(carId)
    }
}