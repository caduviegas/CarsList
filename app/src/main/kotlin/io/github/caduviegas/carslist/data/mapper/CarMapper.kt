package io.github.caduviegas.carslist.data.mapper

import io.github.caduviegas.carslist.data.model.CarDTO
import io.github.caduviegas.carslist.domain.model.Car
import io.github.caduviegas.carslist.domain.model.FuelType
import java.time.Instant
import java.time.ZoneId

class CarMapper {
    fun CarDTO.mapCar(): Car {
        return Car(
            id = id,
            cadastro = Instant.ofEpochSecond(registrationDate)
                .atZone(ZoneId.systemDefault())
                .toLocalDate(),
            modeloId = modelId,
            ano = year,
            fuelType = when (fuel.uppercase()) {
                "GASOLINA" -> FuelType.GASOLINE
                "FLEX" -> FuelType.FLEX
                "ELETRICO", "ELÉTRICO" -> FuelType.ELECTRIC
                "HÍBRIDO", "HIBRIDO" -> FuelType.HYBRID
                "DIESEL" -> FuelType.DIESEL
                else -> FuelType.OTHER
            },
            numPortas = doors,
            cor = color,
            nomeModelo = modelName,
            valor = price
        )
    }

    fun mapList(cars: List<CarDTO>): List<Car> = cars.map { it.mapCar() }
}
