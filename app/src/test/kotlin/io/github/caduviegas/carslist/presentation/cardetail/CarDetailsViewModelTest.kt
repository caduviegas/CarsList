package io.github.caduviegas.carslist.presentation.cardetail

import io.github.caduviegas.carslist.domain.model.Car
import io.github.caduviegas.carslist.domain.model.FuelType
import io.github.caduviegas.carslist.domain.model.LeadStatus
import io.github.caduviegas.carslist.domain.usecase.HasLoggedUserUseCase
import io.github.caduviegas.carslist.domain.usecase.ListLeadsByCarUseCase
import io.github.caduviegas.carslist.domain.usecase.SaveLeadUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class CarDetailsViewModelTest {

    private val listLeadsByCarUseCase: ListLeadsByCarUseCase = mockk()
    private val saveLeadUseCase: SaveLeadUseCase = mockk()
    private val hasLoggedUserUseCase: HasLoggedUserUseCase = mockk()
    private lateinit var viewModel: CarDetailsViewModel

    private val testCar = Car(
        id = 1,
        cadastro = LocalDate.now(),
        modeloId = 1,
        ano = 2020,
        fuelType = FuelType.GASOLINE,
        numPortas = 4,
        cor = "Preto",
        nomeModelo = "Fusca",
        valor = 10000.0
    )

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = CarDetailsViewModel(
            listLeadsByCarUseCase,
            saveLeadUseCase,
            hasLoggedUserUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `setCar with no leads sets state to NoLead`() = runTest {
        coEvery { listLeadsByCarUseCase(testCar.id) } returns emptyList()
        viewModel.setCar(testCar)
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(LeadSaveUiState.NoLead, viewModel.leadSaveUiState.value)
    }

    @Test
    fun `setCar with leads sets state to AlreadyHasLead`() = runTest {
        coEvery { listLeadsByCarUseCase(testCar.id) } returns listOf(mockk())
        viewModel.setCar(testCar)
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(LeadSaveUiState.AlreadyHasLead, viewModel.leadSaveUiState.value)
    }

    @Test
    fun `saveLeadForCurrentCar with no user sets state to NoUser after delay`() = runTest {
        coEvery { listLeadsByCarUseCase(testCar.id) } returns emptyList()
        coEvery { hasLoggedUserUseCase() } returns false
        viewModel.setCar(testCar)
        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.saveLeadForCurrentCar()
        testDispatcher.scheduler.advanceTimeBy(500)
        assertEquals(LeadSaveUiState.Loading, viewModel.leadSaveUiState.value)
        testDispatcher.scheduler.advanceTimeBy(1000)
        assertEquals(LeadSaveUiState.NoUser, viewModel.leadSaveUiState.value)
    }

    @Test
    fun `saveLeadForCurrentCar with user and success sets state to Success after delay`() = runTest {
        coEvery { listLeadsByCarUseCase(testCar.id) } returns emptyList()
        coEvery { hasLoggedUserUseCase() } returns true
        coEvery { saveLeadUseCase(testCar) } returns LeadStatus.UPDATED
        viewModel.setCar(testCar)
        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.saveLeadForCurrentCar()
        testDispatcher.scheduler.advanceTimeBy(999)
        assertEquals(LeadSaveUiState.Loading, viewModel.leadSaveUiState.value)
        testDispatcher.scheduler.advanceTimeBy(1)
        assertEquals(LeadSaveUiState.Loading, viewModel.leadSaveUiState.value)
        testDispatcher.scheduler.advanceTimeBy(1)
        assertEquals(LeadSaveUiState.Success, viewModel.leadSaveUiState.value)
    }

    @Test
    fun `saveLeadForCurrentCar with user and error sets state to Error after delay`() = runTest {
        coEvery { listLeadsByCarUseCase(testCar.id) } returns emptyList()
        coEvery { hasLoggedUserUseCase() } returns true
        coEvery { saveLeadUseCase(testCar) } throws RuntimeException("fail")
        viewModel.setCar(testCar)
        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.saveLeadForCurrentCar()
        testDispatcher.scheduler.advanceTimeBy(1001)
        assertEquals(LeadSaveUiState.Error, viewModel.leadSaveUiState.value)
    }
}
