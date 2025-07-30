package io.github.caduviegas.carslist.domain.usecase

import io.github.caduviegas.carslist.domain.model.User
import io.github.caduviegas.carslist.domain.repository.CarDatabaseRepository

class LoginUseCase(
    private val repository: CarDatabaseRepository
) {
    suspend operator fun invoke(user: User) {
        repository.saveUser(user)
    }
}