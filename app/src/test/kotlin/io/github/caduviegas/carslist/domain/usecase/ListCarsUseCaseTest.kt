package io.github.caduviegas.carslist.domain.usecase

import com.google.common.truth.Truth.assertThat
import io.github.caduviegas.carslist.domain.model.Car
import io.github.caduviegas.carslist.domain.model.FuelType
import io.github.caduviegas.carslist.domain.repository.CarApiRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class ListCarsUseCaseTest {

    private val repository: CarApiRepository = mockk()
    private lateinit var useCase: ListCarsUseCase

    @Before
    fun setUp() {
        useCase = ListCarsUseCase(repository)
    }

    @Test
    fun `should return list of cars from repository`() = runTest {
        val cars = listOf(
            Car(
                id = 1,
                cadastro = LocalDate.of(2023, 1, 1),
                modeloId = 10,
                ano = 2022,
                fuelType = FuelType.FLEX,
                numPortas = 4,
                cor = "Prata",
                nomeModelo = "Tesla Model Y",
                valor = 50000.0
            )
        )
        coEvery { repository.fetchCars() } returns cars

        val result = useCase()

        assertThat(result).isEqualTo(cars)
    }

    @Test
    fun `should propagate exception from repository`() = runTest {
        coEvery { repository.fetchCars() } throws RuntimeException("Repository error")

        try {
            useCase()
            assertThat("Exception").isNull()
        } catch (e: Exception) {
            assertThat(e).isInstanceOf(RuntimeException::class.java)
            assertThat(e.message).isEqualTo("Repository error")
        }
    }
}