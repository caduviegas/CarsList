package io.github.caduviegas.carslist.presentation.login

import com.google.common.truth.Truth.assertThat
import io.github.caduviegas.carslist.domain.usecase.LoginUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var loginUseCase: LoginUseCase
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        loginUseCase = mockk()
        viewModel = LoginViewModel(loginUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `uiState is Error when cpf is null`() = runTest {
        viewModel.registerUser(
            cpf = null,
            name = "Nome",
            email = "email@email.com",
            phone = null,
            birthday = null
        )
        assertThat(viewModel.uiState.value).isEqualTo(LoginUiState.Error("CPF é obrigatório"))
    }

    @Test
    fun `uiState is Error when cpf is blank`() = runTest {
        viewModel.registerUser(
            cpf = "   ",
            name = "Nome",
            email = "email@email.com",
            phone = null,
            birthday = null
        )
        assertThat(viewModel.uiState.value).isEqualTo(LoginUiState.Error("CPF é obrigatório"))
    }

    @Test
    fun `uiState is Error when name is null`() = runTest {
        viewModel.registerUser(
            cpf = "123",
            name = null,
            email = "email@email.com",
            phone = null,
            birthday = null
        )
        assertThat(viewModel.uiState.value).isEqualTo(LoginUiState.Error("Nome é obrigatório"))
    }

    @Test
    fun `uiState is Error when name is blank`() = runTest {
        viewModel.registerUser(
            cpf = "123",
            name = "   ",
            email = "email@email.com",
            phone = null,
            birthday = null
        )
        assertThat(viewModel.uiState.value).isEqualTo(LoginUiState.Error("Nome é obrigatório"))
    }

    @Test
    fun `uiState is Error when email is null`() = runTest {
        viewModel.registerUser(
            cpf = "123",
            name = "Nome",
            email = null,
            phone = null,
            birthday = null
        )
        assertThat(viewModel.uiState.value).isEqualTo(LoginUiState.Error("Email é obrigatório"))
    }

    @Test
    fun `uiState is Error when email is blank`() = runTest {
        viewModel.registerUser(
            cpf = "123",
            name = "Nome",
            email = "   ",
            phone = null,
            birthday = null
        )
        assertThat(viewModel.uiState.value).isEqualTo(LoginUiState.Error("Email é obrigatório"))
    }

    @Test
    fun `uiState is Success when register is successful`() = runTest {
        coEvery { loginUseCase(any()) } returns Unit
        viewModel.registerUser(
            cpf = "123",
            name = "Nome",
            email = "email@email.com",
            phone = "99999999",
            birthday = LocalDate.of(2000, 1, 1)
        )
        testDispatcher.scheduler.advanceTimeBy(1000)
        testDispatcher.scheduler.advanceUntilIdle()
        assertThat(viewModel.uiState.value).isEqualTo(LoginUiState.Success)
        coVerify(exactly = 1) { loginUseCase(any()) }
    }

    @Test
    fun `uiState is Error when usecase throws exception`() = runTest {
        coEvery { loginUseCase(any()) } throws RuntimeException("Falha")
        viewModel.registerUser(
            cpf = "123",
            name = "Nome",
            email = "email@email.com",
            phone = "99999999",
            birthday = LocalDate.of(2000, 1, 1)
        )
        testDispatcher.scheduler.advanceTimeBy(1000)
        testDispatcher.scheduler.advanceUntilIdle()
        assertThat(viewModel.uiState.value).isEqualTo(LoginUiState.Error("Erro ao cadastrar usuário"))
        coVerify(exactly = 1) { loginUseCase(any()) }
    }
}
