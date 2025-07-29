package io.github.caduviegas.carslist.domain.usecase

import io.github.caduviegas.carslist.domain.model.Car
import io.github.caduviegas.carslist.domain.model.User
import io.github.caduviegas.carslist.domain.repository.CarApiRepository
import io.github.caduviegas.carslist.domain.util.OrderConstants
import java.time.LocalDate
import java.util.UUID

class SyncLeadUseCase(
    private val repository: CarApiRepository
) {
    suspend operator fun invoke(
        user: User,
        car: Car
    ) {
        val pedidoId = UUID.randomUUID().toString()
        val dataPedido = LocalDate.now()
        val status = OrderConstants.STATUS_ORDERED

        repository.postOrderCar(
            orderId = pedidoId,
            orderDate = dataPedido,
            status = status,
            user = user,
            car = car
        )
    }
}