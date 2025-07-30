package io.github.caduviegas.carslist.domain.usecase

import io.github.caduviegas.carslist.domain.model.Car
import io.github.caduviegas.carslist.domain.model.Lead
import io.github.caduviegas.carslist.domain.model.LeadStatus
import io.github.caduviegas.carslist.domain.repository.CarDatabaseRepository
import io.github.caduviegas.carslist.domain.util.OrderConstants
import io.github.caduviegas.carslist.domain.exception.UserNotLoggedInException
import java.time.LocalDate
import java.util.UUID

class SaveLeadUseCase(
    private val repository: CarDatabaseRepository
) {
    suspend operator fun invoke(car: Car): LeadStatus {
        return try {
            val user = repository.getLoggedUser()
            val lead = Lead(
                id = UUID.randomUUID().toString(),
                date = LocalDate.now(),
                status = OrderConstants.STATUS_NEW,
                car = car,
                user = user
            )
            repository.saveLead(lead)
            LeadStatus.UPDATED
        } catch (_: UserNotLoggedInException) {
            LeadStatus.NO_USER_LOGGED
        }
    }
}