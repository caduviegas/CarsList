package io.github.caduviegas.carslist.domain.repository

import io.github.caduviegas.carslist.domain.model.Lead
import io.github.caduviegas.carslist.domain.model.User

interface CarDatabaseRepository {
    suspend fun retrieveNotSyncedLeads(): List<Lead>
    suspend fun saveLead(lead: Lead)
    suspend fun getLoggedUser(): User
    suspend fun updateLeadStatus(id: String, status: String)
    suspend fun getLeadsByCarId(carId: Int): List<Lead>
    suspend fun deleteAllData()
    suspend fun saveUser(user: User)
}