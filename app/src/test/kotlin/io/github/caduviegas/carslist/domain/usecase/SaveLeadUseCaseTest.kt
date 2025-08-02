package io.github.caduviegas.carslist.domain.usecase

import com.google.common.truth.Truth.assertThat
import io.github.caduviegas.carslist.domain.exception.UserNotLoggedInException
import io.github.caduviegas.carslist.domain.model.Car
import io.github.caduviegas.carslist.domain.model.FuelType
import io.github.caduviegas.carslist.domain.model.Lead
import io.github.caduviegas.carslist.domain.model.LeadStatus
import io.github.caduviegas.carslist.domain.model.User
import io.github.caduviegas.carslist.domain.repository.CarDatabaseRepository
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
class SaveLeadUseCaseTest {

    private val repository: CarDatabaseRepository = mockk(relaxed = true)
    private lateinit var useCase: SaveLeadUseCase

    @Before
    fun setUp() {
        useCase = SaveLeadUseCase(repository)
    }

    @Test
    fun `should save lead and return UPDATED when user is logged in`() = runTest {
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
        coEvery { repository.getLoggedUser() } returns user
        val leadSlot = slot<Lead>()
        coEvery { repository.saveLead(capture(leadSlot)) } returns Unit

        val result = useCase(car)

        coVerify(exactly = 1) { repository.getLoggedUser() }
        coVerify(exactly = 1) { repository.saveLead(any()) }
        assertThat(result).isEqualTo(LeadStatus.UPDATED)

        val capturedLead = leadSlot.captured
        assertThat(capturedLead.user).isEqualTo(user)
        assertThat(capturedLead.car).isEqualTo(car)
        assertThat(capturedLead.status).isEqualTo(OrderConstants.STATUS_NEW)
        assertThat(capturedLead.id).isNotEmpty()
        assertThat(capturedLead.date).isEqualTo(LocalDate.now())
    }

    @Test
    fun `should return NO_USER_LOGGED when UserNotLoggedInException is thrown`() = runTest {
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
        coEvery { repository.getLoggedUser() } throws UserNotLoggedInException()

        val result = useCase(car)

        coVerify(exactly = 1) { repository.getLoggedUser() }
        coVerify(exactly = 0) { repository.saveLead(any()) }
        assertThat(result).isEqualTo(LeadStatus.NO_USER_LOGGED)
    }
}
