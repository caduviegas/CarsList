package io.github.caduviegas.carslist.data.mapper

import com.google.common.truth.Truth.assertThat
import io.github.caduviegas.carslist.data.model.CarInfoDTO
import io.github.caduviegas.carslist.data.model.OrderCarDTO
import io.github.caduviegas.carslist.data.model.UserDTO
import io.github.caduviegas.carslist.domain.model.Car
import io.github.caduviegas.carslist.domain.model.FuelType
import io.github.caduviegas.carslist.domain.model.Lead
import io.github.caduviegas.carslist.domain.model.User
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneId
import java.util.UUID

class OrderCarMapperTest {

    private val mapper = OrderCarMapper()

    @Test
    fun `toUserDTO maps User to UserDTO correctly`() {
        val birthday = LocalDate.of(1990, 5, 20)
        val user = User(
            cpf = "12345678900",
            name = "João da Silva",
            email = "joao@email.com",
            phone = "11999999999",
            birthday = birthday
        )

        val userDTO = mapper.toUserDTO(user)
        val expectedBirthDate = birthday.atStartOfDay(ZoneId.systemDefault()).toEpochSecond() * 1000

        assertThat(userDTO.cpf).isEqualTo(user.cpf)
        assertThat(userDTO.name).isEqualTo(user.name)
        assertThat(userDTO.email).isEqualTo(user.email)
        assertThat(userDTO.phone).isEqualTo(user.phone)
        assertThat(userDTO.birthDate).isEqualTo(expectedBirthDate)
    }

    @Test
    fun `toUserDTO maps User with null birthday to UserDTO with null birthDate`() {
        val user = User(
            cpf = "12345678900",
            name = "João da Silva",
            email = "joao@email.com",
            phone = "11999999999",
            birthday = null
        )

        val userDTO = mapper.toUserDTO(user)
        assertThat(userDTO.birthDate).isNull()
    }

    @Test
    fun `toCarInfoDTO maps Car to CarInfoDTO correctly`() {
        val car = Car(
            id = 1,
            cadastro = LocalDate.of(2020, 1, 1),
            modeloId = 42,
            ano = 2020,
            fuelType = FuelType.FLEX,
            numPortas = 4,
            cor = "Prata",
            nomeModelo = "Tesla Model Y",
            valor = 100000.0
        )

        val carInfoDTO = mapper.toCarInfoDTO(car)

        assertThat(carInfoDTO.id).isEqualTo(car.id)
        assertThat(carInfoDTO.modelId).isEqualTo(car.modeloId)
    }

    @Test
    fun `toOrderCarDTO maps all fields from Lead correctly`() {
        val birthday = LocalDate.of(1990, 5, 20)
        val user = User(
            cpf = "12345678900",
            name = "João da Silva",
            email = "joao@email.com",
            phone = "11999999999",
            birthday = birthday
        )
        val car = Car(
            id = 1,
            cadastro = LocalDate.of(2020, 1, 1),
            modeloId = 42,
            ano = 2020,
            fuelType = FuelType.FLEX,
            numPortas = 4,
            cor = "Prata",
            nomeModelo = "Model Y",
            valor = 100000.0
        )
        val orderId = UUID.randomUUID().toString()
        val orderDate = LocalDate.of(2024, 6, 1)
        val status = "PENDING"
        val lead = Lead(
            id = orderId,
            date = orderDate,
            status = status,
            car = car,
            user = user
        )

        val orderCarDTO = mapper.toOrderCarDTO(lead)
        val expectedOrderDate = orderDate.atStartOfDay(ZoneId.systemDefault()).toEpochSecond() * 1000
        val expectedUserDTO = mapper.toUserDTO(user)
        val expectedCarInfoDTO = mapper.toCarInfoDTO(car)

        assertThat(orderCarDTO.orderId).isEqualTo(orderId)
        assertThat(orderCarDTO.orderDate).isEqualTo(expectedOrderDate)
        assertThat(orderCarDTO.status).isEqualTo(status)
        assertThat(orderCarDTO.user).isEqualTo(expectedUserDTO)
        assertThat(orderCarDTO.car).isEqualTo(expectedCarInfoDTO)
    }
}