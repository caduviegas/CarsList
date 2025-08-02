package io.github.caduviegas.carslist.presentation.carlist

import com.google.common.truth.Truth.assertThat
import io.github.caduviegas.carslist.domain.model.Car
import io.github.caduviegas.carslist.domain.model.FuelType
import io.github.caduviegas.carslist.domain.usecase.ListCarsUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class CarListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var listCarsUseCase: ListCarsUseCase

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        listCarsUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `uiState is Loading initially`() = runTest {
        val viewModel = CarListViewModel(listCarsUseCase)
        assertThat(viewModel.uiState.value).isInstanceOf(CarListUiState.Loading::class.java)
    }

    @Test
    fun `fetchCars sets Loading then Success when usecase returns cars`() = runTest {
        val cars = listOf(
            Car(
                id = 1,
                cadastro = LocalDate.now(),
                modeloId = 10,
                ano = 2020,
                fuelType = FuelType.GASOLINE,
                numPortas = 4,
                cor = "Prata",
                nomeModelo = "Tesla Model Y",
                valor = 50000.0
            )
        )
        coEvery { listCarsUseCase.invoke() } returns cars
        val viewModel = CarListViewModel(listCarsUseCase)

        viewModel.fetchCars()
        assertThat(viewModel.uiState.value).isInstanceOf(CarListUiState.Loading::class.java)

        testDispatcher.scheduler.advanceUntilIdle()
        val state = viewModel.uiState.value
        assertThat(state).isInstanceOf(CarListUiState.Success::class.java)
        assertThat((state as CarListUiState.Success).cars).isEqualTo(cars)
    }

    @Test
    fun `fetchCars sets Loading then Error when usecase throws exception`() = runTest {
        coEvery { listCarsUseCase.invoke() } throws RuntimeException("Falha")
        val viewModel = CarListViewModel(listCarsUseCase)

        viewModel.fetchCars()
        assertThat(viewModel.uiState.value).isInstanceOf(CarListUiState.Loading::class.java)

        testDispatcher.scheduler.advanceUntilIdle()
        val state = viewModel.uiState.value
        assertThat(state).isInstanceOf(CarListUiState.Error::class.java)
        assertThat((state as CarListUiState.Error).message).isEqualTo("Erro ao carregar carros")
    }
}
