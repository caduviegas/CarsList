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
import io.github.caduviegas.carslist.domain.model.User
import io.github.caduviegas.carslist.domain.repository.CarRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class CarRepositoryImplTest {

    private val apiService: CarApiService = mockk()
    private val carMapper: CarMapper = mockk()
    private val orderCarMapper: OrderCarMapper = mockk()
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var repository: CarRepository

    @Before
    fun setUp() {
        repository = CarRepositoryImpl(apiService, carMapper, orderCarMapper, testDispatcher)
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
    fun `postOrderCar calls apiService with mapped DTO`() = runTest(testDispatcher) {
        val pedidoId = "RANDOM_UUID"
        val dataPedido = LocalDate.of(2024, 6, 10)
        val status = "PENDENTE"
        val usuario = User(
            cpf = "12345678900",
            name = "Maria",
            email = "maria@email.com",
            phone = "11999999999",
            birthday = LocalDate.of(1990, 1, 1)
        )
        val carro = Car(
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
        val expectedDTO = mockk<OrderCarDTO>(relaxed = true)

        coEvery { orderCarMapper.toOrderCarDTO(pedidoId, dataPedido, status, usuario, carro) } returns expectedDTO
        coEvery { apiService.postOrderCar(expectedDTO) } returns Unit

        repository.postOrderCar(pedidoId, dataPedido, status, usuario, carro)

        coVerify(exactly = 1) { orderCarMapper.toOrderCarDTO(pedidoId, dataPedido, status, usuario, carro) }
        coVerify(exactly = 1) { apiService.postOrderCar(expectedDTO) }
    }

    @Test
    fun `postOrderCar propagates exception from apiService`() = runTest(testDispatcher) {
        val pedidoId = "RANDOM_UUID"
        val dataPedido = LocalDate.of(2024, 6, 10)
        val status = "PENDENTE"
        val usuario = User(
            cpf = "12345678900",
            name = "Maria",
            email = "maria@email.com",
            phone = "11999999999",
            birthday = LocalDate.of(1990, 1, 1)
        )
        val carro = Car(
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
        val expectedDTO = mockk<OrderCarDTO>(relaxed = true)

        coEvery { orderCarMapper.toOrderCarDTO(pedidoId, dataPedido, status, usuario, carro) } returns expectedDTO
        coEvery { apiService.postOrderCar(expectedDTO) } throws RuntimeException("API error")

        try {
            repository.postOrderCar(pedidoId, dataPedido, status, usuario, carro)
            assertThat("Exception!").isNull()
        } catch (e: Exception) {
            assertThat(e).isInstanceOf(RuntimeException::class.java)
            assertThat(e.message).isEqualTo("API error")
        }
    }
}