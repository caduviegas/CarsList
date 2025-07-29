package io.github.caduviegas.carslist.data.mapper

import com.google.common.truth.Truth.assertThat
import io.github.caduviegas.carslist.data.model.CarDTO
import io.github.caduviegas.carslist.domain.model.FuelType
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneId

class CarMapperTest {

    private val mapper = CarMapper()

    @Test
    fun `should map GASOLINA to GASOLINE`() {
        val dto = CarDTO(1, 1700000000, 10, 2020, "GASOLINA", 4, "PRETO", "ONIX", 50000.0)
        val car = mapper.run { dto.mapCar() }
        assertThat(car.fuelType).isEqualTo(FuelType.GASOLINE)
    }

    @Test
    fun `should map FLEX to FLEX`() {
        val dto = CarDTO(2, 1700000000, 11, 2021, "FLEX", 4, "BRANCO", "CORSA", 40000.0)
        val car = mapper.run { dto.mapCar() }
        assertThat(car.fuelType).isEqualTo(FuelType.FLEX)
    }

    @Test
    fun `should map ELETRICO and ELÉTRICO to ELECTRIC`() {
        val dto1 = CarDTO(3, 1700000000, 12, 2022, "ELETRICO", 4, "AZUL", "TESLA", 300000.0)
        val dto2 = CarDTO(4, 1700000000, 13, 2022, "ELÉTRICO", 4, "VERDE", "TESLA", 300000.0)
        assertThat(mapper.run { dto1.mapCar() }.fuelType).isEqualTo(FuelType.ELECTRIC)
        assertThat(mapper.run { dto2.mapCar() }.fuelType).isEqualTo(FuelType.ELECTRIC)
    }

    @Test
    fun `should map HÍBRIDO and HIBRIDO to HYBRID`() {
        val dto1 = CarDTO(5, 1700000000, 14, 2023, "HÍBRIDO", 4, "CINZA", "PRIUS", 120000.0)
        val dto2 = CarDTO(6, 1700000000, 15, 2023, "HIBRIDO", 4, "CINZA", "PRIUS", 120000.0)
        assertThat(mapper.run { dto1.mapCar() }.fuelType).isEqualTo(FuelType.HYBRID)
        assertThat(mapper.run { dto2.mapCar() }.fuelType).isEqualTo(FuelType.HYBRID)
    }

    @Test
    fun `should map DIESEL to DIESEL`() {
        val dto = CarDTO(7, 1700000000, 16, 2019, "DIESEL", 4, "VERMELHO", "RANGER", 150000.0)
        val car = mapper.run { dto.mapCar() }
        assertThat(car.fuelType).isEqualTo(FuelType.DIESEL)
    }

    @Test
    fun `should map unknown fuel to OTHER`() {
        val dto = CarDTO(8, 1700000000, 17, 2018, "GNV", 4, "AMARELO", "UNO", 20000.0)
        val car = mapper.run { dto.mapCar() }
        assertThat(car.fuelType).isEqualTo(FuelType.OTHER)
    }

    @Test
    fun `should map all fields correctly`() {
        val timestamp = 1700000000L
        val expectedDate = LocalDate.ofInstant(
            java.time.Instant.ofEpochSecond(timestamp),
            ZoneId.systemDefault()
        )
        val dto = CarDTO(9, timestamp, 18, 2017, "FLEX", 2, "AZUL", "GOL", 25000.0)
        val car = mapper.run { dto.mapCar() }
        assertThat(car.id).isEqualTo(dto.id)
        assertThat(car.cadastro).isEqualTo(expectedDate)
        assertThat(car.modeloId).isEqualTo(dto.modelId)
        assertThat(car.ano).isEqualTo(dto.year)
        assertThat(car.fuelType).isEqualTo(FuelType.FLEX)
        assertThat(car.numPortas).isEqualTo(dto.doors)
        assertThat(car.cor).isEqualTo(dto.color)
        assertThat(car.nomeModelo).isEqualTo(dto.modelName)
        assertThat(car.valor).isEqualTo(dto.price)
    }

    @Test
    fun `should map list of CarDTO to list of Car`() {
        val dtos = listOf(
            CarDTO(1, 1700000000, 10, 2020, "GASOLINA", 4, "PRETO", "ONIX", 50000.0),
            CarDTO(2, 1700000000, 11, 2021, "FLEX", 4, "BRANCO", "CORSA", 40000.0)
        )
        val cars = mapper.mapList(dtos)
        assertThat(cars).hasSize(2)
        assertThat(cars[0].fuelType).isEqualTo(FuelType.GASOLINE)
        assertThat(cars[1].fuelType).isEqualTo(FuelType.FLEX)
    }
}