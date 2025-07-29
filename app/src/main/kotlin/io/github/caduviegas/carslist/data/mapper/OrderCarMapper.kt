package io.github.caduviegas.carslist.data.mapper

import io.github.caduviegas.carslist.domain.model.User
import io.github.caduviegas.carslist.domain.model.Car
import io.github.caduviegas.carslist.data.model.UserDTO
import io.github.caduviegas.carslist.data.model.CarInfoDTO
import io.github.caduviegas.carslist.data.model.OrderCarDTO
import java.time.LocalDate
import java.time.ZoneId

class OrderCarMapper {
    fun toUserDTO(user: User): UserDTO =
        UserDTO(
            cpf = user.cpf,
            name = user.name,
            email = user.email,
            phone = user.phone,
            birthDate = user.birthday.atStartOfDay(ZoneId.systemDefault()).toEpochSecond() * 1000
        )

    fun toCarInfoDTO(car: Car): CarInfoDTO =
        CarInfoDTO(
            id = car.id,
            modelId = car.modeloId
        )

    fun toOrderCarDTO(
        orderId: String,
        orderDate: LocalDate,
        status: String,
        user: User,
        car: Car
    ): OrderCarDTO =
        OrderCarDTO(
            orderId = orderId,
            orderDate = orderDate.atStartOfDay(ZoneId.systemDefault()).toEpochSecond() * 1000,
            status = status,
            user = toUserDTO(user),
            car = toCarInfoDTO(car)
        )
}