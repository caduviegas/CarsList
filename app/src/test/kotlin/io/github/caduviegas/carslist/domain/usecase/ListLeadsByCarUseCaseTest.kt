package io.github.caduviegas.carslist.domain.usecase

import com.google.common.truth.Truth.assertThat
import io.github.caduviegas.carslist.domain.model.Car
import io.github.caduviegas.carslist.domain.model.FuelType
import io.github.caduviegas.carslist.domain.model.Lead
import io.github.caduviegas.carslist.domain.model.User
import io.github.caduviegas.carslist.domain.repository.CarDatabaseRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class ListLeadsByCarUseCaseTest {

    private val repository: CarDatabaseRepository = mockk()
    private lateinit var useCase: ListLeadsByCarUseCase

    @Before
    fun setUp() {
        useCase = ListLeadsByCarUseCase(repository)
    }

    @Test
    fun `should return list of leads from repository`() = runTest {
        val carId = 1
        val user = User(
            cpf = "12345678900",
            name = "João da Silva",
            email = "joao@gmail.com",
            phone = "11999999999",
            birthday = LocalDate.of(1991, 12, 20)
        )
        val car = Car(
            id = carId,
            cadastro = LocalDate.of(2023, 1, 1),
            modeloId = 10,
            ano = 2022,
            fuelType = FuelType.FLEX,
            numPortas = 4,
            cor = "Prata",
            nomeModelo = "Tesla Model Y",
            valor = 50000.0
        )
        val leads = listOf(
            Lead(
                id = "lead-1",
                date = LocalDate.of(2024, 6, 1),
                status = "NEW",
                car = car,
                user = user
            )
        )
        coEvery { repository.getLeadsByCarId(carId) } returns leads

        val result = useCase(carId)

        assertThat(result).isEqualTo(leads)
    }

    @Test
    fun `should propagate exception from repository`() = runTest {
        val carId = 1
        coEvery { repository.getLeadsByCarId(carId) } throws RuntimeException("Repository error")

        try {
            useCase(carId)
            assertThat("Exception").isNull() // Should not reach here
        } catch (e: Exception) {
            assertThat(e).isInstanceOf(RuntimeException::class.java)
            assertThat(e.message).isEqualTo("Repository error")
        }
    }
}