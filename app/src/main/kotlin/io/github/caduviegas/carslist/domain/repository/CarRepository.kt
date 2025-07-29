package io.github.caduviegas.carslist.domain.repository

import io.github.caduviegas.carslist.domain.model.Car
import io.github.caduviegas.carslist.domain.model.User
import java.time.LocalDate

interface CarRepository {
    suspend fun fetchCars(): List<Car>
    suspend fun postOrderCar(
        pedidoId: String,
        dataPedido: LocalDate,
        status: String,
        usuario: User,
        carro: Car
    )
}