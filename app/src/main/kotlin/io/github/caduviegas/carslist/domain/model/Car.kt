package io.github.caduviegas.carslist.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class Car(
    val id: Int,
    val cadastro: LocalDate,
    val modeloId: Int,
    val ano: Int,
    val fuelType: FuelType,
    val numPortas: Int,
    val cor: String,
    val nomeModelo: String,
    val valor: Double
): Parcelable