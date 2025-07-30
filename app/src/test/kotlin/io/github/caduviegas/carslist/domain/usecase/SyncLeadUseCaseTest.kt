package io.github.caduviegas.carslist.domain.usecase

import com.google.common.truth.Truth.assertThat
import io.github.caduviegas.carslist.domain.exception.UserNotLoggedInException
import io.github.caduviegas.carslist.domain.model.Car
import io.github.caduviegas.carslist.domain.model.FuelType
import io.github.caduviegas.carslist.domain.model.Lead
import io.github.caduviegas.carslist.domain.model.LeadStatus
import io.github.caduviegas.carslist.domain.model.User
import io.github.caduviegas.carslist.domain.repository.CarApiRepository
import io.github.caduviegas.carslist.domain.repository.CarDatabaseRepository
import io.github.caduviegas.carslist.domain.util.OrderConstants
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class SyncLeadUseCaseTest {

    private val apiRepository: CarApiRepository = mockk(relaxed = true)
    private val databaseRepository: CarDatabaseRepository = mockk(relaxed = true)
    private lateinit var useCase: SyncLeadUseCase

    @Before
    fun setUp() {
        useCase = SyncLeadUseCase(apiRepository, databaseRepository)
    }

    @Test
    fun `should sync all not synced leads and update their status`() = runTest {
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
        val lead1 = Lead(car = car, user = user)
        val lead2 = Lead(car = car, user = user)
        val notSyncedLeads = listOf(lead1, lead2)

        coEvery { databaseRepository.retrieveNotSyncedLeads() } returns notSyncedLeads
        coEvery { apiRepository.postOrderCar(any()) } returns Unit
        coEvery { databaseRepository.updateLeadStatus(any(), any()) } returns Unit

        val result = useCase()

        coVerify(exactly = 1) { databaseRepository.retrieveNotSyncedLeads() }
        coVerify(exactly = 1) { apiRepository.postOrderCar(lead1) }
        coVerify(exactly = 1) { apiRepository.postOrderCar(lead2) }
        coVerify(exactly = 1) { databaseRepository.updateLeadStatus(lead1.id, OrderConstants.STATUS_SYNCED) }
        coVerify(exactly = 1) { databaseRepository.updateLeadStatus(lead2.id, OrderConstants.STATUS_SYNCED) }
        assertThat(result).isEqualTo(LeadStatus.UPDATED)
    }

    @Test
    fun `should return NO_USER_LOGGED when UserNotLoggedInException is thrown`() = runTest {
        coEvery { databaseRepository.retrieveNotSyncedLeads() } throws UserNotLoggedInException()

        val result = useCase()

        assertThat(result).isEqualTo(LeadStatus.NO_USER_LOGGED)
    }

    @Test
    fun `should not update status if postOrderCar throws exception for a lead`() = runTest {
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
        val lead = Lead(car = car, user = user)
        coEvery { databaseRepository.retrieveNotSyncedLeads() } returns listOf(lead)
        coEvery { apiRepository.postOrderCar(lead) } throws RuntimeException("API error")

        try {
            useCase()
            assertThat("Should throw exception").isNull()
        } catch (e: Exception) {
            assertThat(e).isInstanceOf(RuntimeException::class.java)
            assertThat(e.message).isEqualTo("API error")
            coVerify(exactly = 0) { databaseRepository.updateLeadStatus(any(), any()) }
        }
    }

    @Test
    fun `should do nothing if there are no not synced leads`() = runTest {
        coEvery { databaseRepository.retrieveNotSyncedLeads() } returns emptyList()

        val result = useCase()

        coVerify(exactly = 1) { databaseRepository.retrieveNotSyncedLeads() }
        coVerify(exactly = 0) { apiRepository.postOrderCar(any()) }
        coVerify(exactly = 0) { databaseRepository.updateLeadStatus(any(), any()) }
        assertThat(result).isEqualTo(LeadStatus.UPDATED)
    }
}