package io.github.caduviegas.carslist.domain.model

import io.github.caduviegas.carslist.domain.util.OrderConstants
import java.time.LocalDate
import java.util.UUID

data class Lead(
    val id: String = UUID.randomUUID().toString(),
    val date: LocalDate = LocalDate.now(),
    val status: String = OrderConstants.STATUS_NEW,
    val car: Car,
    val user: User
)
