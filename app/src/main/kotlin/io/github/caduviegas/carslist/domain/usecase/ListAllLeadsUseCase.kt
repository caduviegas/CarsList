package io.github.caduviegas.carslist.domain.usecase

import io.github.caduviegas.carslist.domain.model.Lead
import io.github.caduviegas.carslist.domain.repository.CarDatabaseRepository

class ListAllLeadsUseCase(
    private val repository: CarDatabaseRepository
) {
    suspend operator fun invoke(): List<Lead> {
        return repository.getAllLeads()
    }
}
