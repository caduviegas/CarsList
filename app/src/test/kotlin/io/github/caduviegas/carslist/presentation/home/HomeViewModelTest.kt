package io.github.caduviegas.carslist.presentation.home

import com.google.common.truth.Truth.assertThat
import io.github.caduviegas.carslist.domain.usecase.HasLoggedUserUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var hasLoggedUserUseCase: HasLoggedUserUseCase

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        hasLoggedUserUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `isLoggedIn emits true when user is logged in`() = runTest(testDispatcher) {
        coEvery { hasLoggedUserUseCase.invoke() } returns true
        val viewModel = HomeViewModel(hasLoggedUserUseCase)

        assertThat(viewModel.isLoggedIn.value).isNull()
        viewModel.validateUser()
        testDispatcher.scheduler.advanceTimeBy(2000)
        testDispatcher.scheduler.advanceUntilIdle()
        assertThat(viewModel.isLoggedIn.value).isTrue()
    }

    @Test
    fun `isLoggedIn emits false when user is not logged in`() = runTest(testDispatcher) {
        coEvery { hasLoggedUserUseCase.invoke() } returns false
        val viewModel = HomeViewModel(hasLoggedUserUseCase)

        assertThat(viewModel.isLoggedIn.value).isNull()
        viewModel.validateUser()
        testDispatcher.scheduler.advanceUntilIdle()
        assertThat(viewModel.isLoggedIn.value).isFalse()
    }
}