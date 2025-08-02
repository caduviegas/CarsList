package io.github.caduviegas.carslist.data.repository

import com.google.common.truth.Truth.assertThat
import io.github.caduviegas.carslist.data.api.CarApiService
import io.github.caduviegas.carslist.data.mapper.CarMapper
import io.github.caduviegas.carslist.data.mapper.OrderCarMapper
import io.github.caduviegas.carslist.data.model.CarDTO
import io.github.caduviegas.carslist.data.model.CarsApiResponse
import io.github.caduviegas.carslist.data.model.OrderCarDTO
import io.github.caduviegas.carslist.domain.model.Car
import io.github.caduviegas.carslist.domain.model.FuelType
import io.github.caduviegas.carslist.domain.model.Lead
import io.github.caduviegas.carslist.domain.model.User
import io.github.caduviegas.carslist.domain.repository.CarApiRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class CarApiRepositoryTest {

    private val apiService: CarApiService = mockk()
    private val carMapper: CarMapper = mockk()
    private val orderCarMapper: OrderCarMapper = mockk()
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var repository: CarApiRepository

    @Before
    fun setUp() {
        repository = CarApiRepositoryImpl(apiService, carMapper, orderCarMapper, testDispatcher)
    }

    @Test
    fun `fetchCars returns mapped cars from API`() = runTest(testDispatcher) {
        val carDtoList = listOf(
            CarDTO(
                id = 1,
                registrationDate = 1700000000,
                modelId = 10,
                year = 2020,
                fuel = "FLEX",
                doors = 4,
                color = "PRETO",
                modelName = "ONIX",
                price = 50000.0
            )
        )
        val apiResponse = CarsApiResponse(carDtoList)
        val mappedCars = listOf(
            Car(
                id = 1,
                cadastro = LocalDate.of(2023, 11, 14),
                modeloId = 10,
                ano = 2020,
                fuelType = FuelType.FLEX,
                numPortas = 4,
                cor = "PRETO",
                nomeModelo = "ONIX",
                valor = 50000.0
            )
        )

        coEvery { apiService.getCars() } returns apiResponse
        coEvery { carMapper.mapList(carDtoList) } returns mappedCars

        val result = repository.fetchCars()
        assertThat(result).isEqualTo(mappedCars)
    }

    @Test
    fun `fetchCars returns empty list when API returns empty`() = runTest(testDispatcher) {
        val carDtoList = emptyList<CarDTO>()
        val apiResponse = CarsApiResponse(carDtoList)
        val mappedCars = emptyList<Car>()
        coEvery { apiService.getCars() } returns apiResponse
        coEvery { carMapper.mapList(carDtoList) } returns mappedCars

        val result = repository.fetchCars()
        assertThat(result).isEmpty()
    }

    @Test
    fun `postOrderCar calls apiService with mapped DTO from Lead`() = runTest(testDispatcher) {
        val user = User(
            cpf = "12345678900",
            name = "Maria",
            email = "maria@email.com",
            phone = "11999999999",
            birthday = LocalDate.of(1990, 1, 1)
        )
        val car = Car(
            id = 1,
            cadastro = LocalDate.of(2023, 1, 1),
            modeloId = 10,
            ano = 2022,
            fuelType = FuelType.FLEX,
            numPortas = 4,
            cor = "Preto",
            nomeModelo = "Sedan",
            valor = 50000.0
        )
        val lead = Lead(
            id = "LEAD_ID",
            date = LocalDate.of(2024, 6, 10),
            status = "PENDENTE",
            car = car,
            user = user
        )
        val expectedDTO = mockk<OrderCarDTO>(relaxed = true)

        coEvery { orderCarMapper.toOrderCarDTO(lead) } returns expectedDTO
        coEvery { apiService.postOrderCar(expectedDTO) } returns Unit

        repository.postOrderCar(lead)

        coVerify(exactly = 1) { orderCarMapper.toOrderCarDTO(lead) }
        coVerify(exactly = 1) { apiService.postOrderCar(expectedDTO) }
    }

    @Test
    fun `postOrderCar propagates exception from apiService`() = runTest(testDispatcher) {
        val user = User(
            cpf = "12345678900",
            name = "Maria",
            email = "maria@email.com",
            phone = "11999999999",
            birthday = LocalDate.of(1990, 1, 1)
        )
        val car = Car(
            id = 1,
            cadastro = LocalDate.of(2023, 1, 1),
            modeloId = 10,
            ano = 2022,
            fuelType = FuelType.FLEX,
            numPortas = 4,
            cor = "Preto",
            nomeModelo = "Sedan",
            valor = 50000.0
        )
        val lead = Lead(
            id = "LEAD_ID",
            date = LocalDate.of(2024, 6, 10),
            status = "PENDENTE",
            car = car,
            user = user
        )
        val expectedDTO = mockk<OrderCarDTO>(relaxed = true)

        coEvery { orderCarMapper.toOrderCarDTO(lead) } returns expectedDTO
        coEvery { apiService.postOrderCar(expectedDTO) } throws RuntimeException("API error")

        try {
            repository.postOrderCar(lead)
            assertThat("Exception!").isNull()
        } catch (e: Exception) {
            assertThat(e).isInstanceOf(RuntimeException::class.java)
            assertThat(e.message).isEqualTo("API error")
        }
    }

    @Test
    fun `fetchCars propagates exception from apiService`() = runTest(testDispatcher) {
        coEvery { apiService.getCars() } throws RuntimeException("API error")
        try {
            repository.fetchCars()
            assertThat("Exception!").isNull()
        } catch (e: Exception) {
            assertThat(e).isInstanceOf(RuntimeException::class.java)
            assertThat(e.message).isEqualTo("API error")
        }
    }

    @Test
    fun `postOrderCar propagates exception from mapper`() = runTest(testDispatcher) {
        val user = User(
            cpf = "12345678900",
            name = "Maria",
            email = "maria@email.com",
            phone = "11999999999",
            birthday = LocalDate.of(1990, 1, 1)
        )
        val car = Car(
            id = 1,
            cadastro = LocalDate.of(2023, 1, 1),
            modeloId = 10,
            ano = 2022,
            fuelType = FuelType.FLEX,
            numPortas = 4,
            cor = "Preto",
            nomeModelo = "Sedan",
            valor = 50000.0
        )
        val lead = Lead(
            id = "LEAD_ID",
            date = LocalDate.of(2024, 6, 10),
            status = "PENDENTE",
            car = car,
            user = user
        )

        coEvery { orderCarMapper.toOrderCarDTO(lead) } throws IllegalArgumentException("Invalid lead")

        try {
            repository.postOrderCar(lead)
            assertThat("Exception!").isNull()
        } catch (e: Exception) {
            assertThat(e).isInstanceOf(IllegalArgumentException::class.java)
            assertThat(e.message).isEqualTo("Invalid lead")
        }
    }
}
