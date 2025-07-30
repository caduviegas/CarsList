package io.github.caduviegas.carslist.data.mapper

import io.github.caduviegas.carslist.domain.model.User
import io.github.caduviegas.carslist.domain.model.Car
import io.github.caduviegas.carslist.data.model.UserDTO
import io.github.caduviegas.carslist.data.model.CarInfoDTO
import io.github.caduviegas.carslist.data.model.OrderCarDTO
import io.github.caduviegas.carslist.domain.model.Lead
import java.time.LocalDate
import java.time.ZoneId

class OrderCarMapper {
    fun toUserDTO(user: User): UserDTO =
        UserDTO(
            cpf = user.cpf,
            name = user.name,
            email = user.email,
            phone = user.phone,
            birthDate = user.birthday
                ?.atStartOfDay(ZoneId.systemDefault())
                ?.toEpochSecond()
                ?.times(1000)
        )

    fun toCarInfoDTO(car: Car): CarInfoDTO =
        CarInfoDTO(
            id = car.id,
            modelId = car.modeloId
        )

    fun toOrderCarDTO(lead: Lead): OrderCarDTO =
        OrderCarDTO(
            orderId = lead.id,
            orderDate = lead.date.atStartOfDay(ZoneId.systemDefault()).toEpochSecond() * 1000,
            status = lead.status,
            user = toUserDTO(lead.user),
            car = toCarInfoDTO(lead.car)
        )
}