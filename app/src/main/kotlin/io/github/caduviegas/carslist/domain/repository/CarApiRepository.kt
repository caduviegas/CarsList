package io.github.caduviegas.carslist.domain.repository

import io.github.caduviegas.carslist.domain.model.Car
import io.github.caduviegas.carslist.domain.model.Lead
import io.github.caduviegas.carslist.domain.model.User
import java.time.LocalDate

interface CarApiRepository {
    suspend fun fetchCars(): List<Car>
    suspend fun postOrderCar(lead: Lead)
}