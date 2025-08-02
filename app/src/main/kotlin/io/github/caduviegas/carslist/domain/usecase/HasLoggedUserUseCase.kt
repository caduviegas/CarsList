package io.github.caduviegas.carslist.domain.usecase

import io.github.caduviegas.carslist.domain.exception.UserNotLoggedInException
import io.github.caduviegas.carslist.domain.repository.CarDatabaseRepository

class HasLoggedUserUseCase(
    private val repository: CarDatabaseRepository
) {
    suspend operator fun invoke(): Boolean {
        return try {
            repository.getLoggedUser()
            true
        } catch (_: UserNotLoggedInException) {
            false
        }
    }
}
