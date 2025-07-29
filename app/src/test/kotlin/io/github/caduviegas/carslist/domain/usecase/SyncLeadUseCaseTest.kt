package io.github.caduviegas.carslist.domain.usecase

import com.google.common.truth.Truth.assertThat
import io.github.caduviegas.carslist.domain.model.Car
import io.github.caduviegas.carslist.domain.model.FuelType
import io.github.caduviegas.carslist.domain.model.User
import io.github.caduviegas.carslist.domain.repository.CarApiRepository
import io.github.caduviegas.carslist.domain.util.OrderConstants
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class SyncLeadUseCaseTest {

    private val repository: CarApiRepository = mockk(relaxed = true)
    private lateinit var useCase: SyncLeadUseCase

    @Before
    fun setUp() {
        useCase = SyncLeadUseCase(repository)
    }

    @Test
    fun `should call repository with correct parameters`() = runTest {
        val user = User(
            cpf = "12345678900",
            name = "João da Silva",
            email = "joao@gmail.com",
            phone = "11999999999",
            birthday = LocalDate.of(1991, 12, 20)
        )
        val car = Car(
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

        val orderIdSlot = slot<String>()
        val orderDateSlot = slot<LocalDate>()
        val statusSlot = slot<String>()
        val userSlot = slot<User>()
        val carSlot = slot<Car>()

        coEvery {
            repository.postOrderCar(
                orderId = capture(orderIdSlot),
                orderDate = capture(orderDateSlot),
                status = capture(statusSlot),
                user = capture(userSlot),
                car = capture(carSlot)
            )
        } returns Unit

        useCase(user, car)

        coVerify(exactly = 1) {
            repository.postOrderCar(
                orderId = any(),
                orderDate = any(),
                status = any(),
                user = any(),
                car = any()
            )
        }
        assertThat(statusSlot.captured).isEqualTo(OrderConstants.STATUS_ORDERED)
        assertThat(userSlot.captured).isEqualTo(user)
        assertThat(carSlot.captured).isEqualTo(car)
        assertThat(orderIdSlot.captured).isNotEmpty()
        assertThat(orderDateSlot.captured).isEqualTo(LocalDate.now())
    }

    @Test
    fun `should propagate exception from repository`() = runTest {
        val user = User(
            cpf = "12345678900",
            name = "João da Silva",
            email = "joao@gmail.com",
            phone = "11999999999",
            birthday = LocalDate.of(1991, 12, 20)
        )
        val car = Car(
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

        coEvery { repository.postOrderCar(any(), any(), any(), any(), any()) } throws RuntimeException("Repository error")

        try {
            useCase(user, car)
            assertThat("Exception").isNull()
        } catch (e: Exception) {
            assertThat(e).isInstanceOf(RuntimeException::class.java)
            assertThat(e.message).isEqualTo("Repository error")
        }
    }
}