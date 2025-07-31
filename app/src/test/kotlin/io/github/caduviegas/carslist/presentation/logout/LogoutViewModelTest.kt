package io.github.caduviegas.carslist.presentation.logout

import com.google.common.truth.Truth.assertThat
import io.github.caduviegas.carslist.domain.usecase.LogoutUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LogoutViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var logoutUseCase: LogoutUseCase
    private lateinit var viewModel: LogoutViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        logoutUseCase = mockk()
        viewModel = LogoutViewModel(logoutUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `uiState should be Initial at start`() = runTest {
        assertThat(viewModel.uiState.value).isEqualTo(LogoutUiState.Initial)
    }

    @Test
    fun `logout success should emit Loading then Success`() = runTest {
        coEvery { logoutUseCase.invoke() } returns Unit
        assertThat(viewModel.uiState.value).isEqualTo(LogoutUiState.Initial)

        viewModel.logout()
        testDispatcher.scheduler.advanceUntilIdle()

        assertThat(viewModel.uiState.value).isEqualTo(LogoutUiState.Success)
        coVerify(exactly = 1) { logoutUseCase.invoke() }
    }
}