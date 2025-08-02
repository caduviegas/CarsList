package io.github.caduviegas.carslist.domain.usecase

import io.github.caduviegas.carslist.domain.repository.CarDatabaseRepository

class LogoutUseCase(
    private val repository: CarDatabaseRepository
) {
    suspend operator fun invoke() {
        repository.deleteAllData()
    }
}
