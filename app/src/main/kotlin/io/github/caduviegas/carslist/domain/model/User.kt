 package io.github.caduviegas.carslist.domain.model

import java.time.LocalDate

data class User(
    val cpf: String,
    val name: String,
    val email: String,
    val phone: String,
    val birthday: LocalDate
)