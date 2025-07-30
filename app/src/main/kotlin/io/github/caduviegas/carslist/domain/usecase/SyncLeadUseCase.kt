package io.github.caduviegas.carslist.domain.usecase

import io.github.caduviegas.carslist.domain.exception.UserNotLoggedInException
import io.github.caduviegas.carslist.domain.model.LeadStatus
import io.github.caduviegas.carslist.domain.repository.CarApiRepository
import io.github.caduviegas.carslist.domain.repository.CarDatabaseRepository
import io.github.caduviegas.carslist.domain.util.OrderConstants

class SyncLeadUseCase(
    private val apiRepository: CarApiRepository,
    private val databaseRepository: CarDatabaseRepository
) {
    suspend operator fun invoke(): LeadStatus {
        return try {
            val notSyncedLeads = databaseRepository.retrieveNotSyncedLeads()
            notSyncedLeads.forEach { lead ->
                apiRepository.postOrderCar(lead)
                databaseRepository.updateLeadStatus(lead.id, OrderConstants.STATUS_SYNCED)
            }
            LeadStatus.UPDATED
        } catch (_: UserNotLoggedInException) {
            LeadStatus.NO_USER_LOGGED
        }
    }
}