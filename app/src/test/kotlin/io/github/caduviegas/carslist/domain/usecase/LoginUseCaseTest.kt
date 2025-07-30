package io.github.caduviegas.carslist.domain.usecase

import com.google.common.truth.Truth.assertThat
import io.github.caduviegas.carslist.domain.model.User
import io.github.caduviegas.carslist.domain.repository.CarDatabaseRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class LoginUseCaseTest {

    private val repository: CarDatabaseRepository = mockk(relaxed = true)
    private lateinit var useCase: LoginUseCase

    @Before
    fun setUp() {
        useCase = LoginUseCase(repository)
    }

    @Test
    fun `should call saveUser on repository`() = runTest {
        val user = User(
            cpf = "12345678900",
            name = "João da Silva",
            email = "joao@gmail.com",
            phone = "11999999999",
            birthday = LocalDate.of(1991, 12, 20)
        )

        useCase(user)

        coVerify(exactly = 1) { repository.saveUser(user) }
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
        coEvery { repository.saveUser(user) } throws RuntimeException("Repository error")

        try {
            useCase(user)
            assertThat("Exception").isNull()
        } catch (e: Exception) {
            assertThat(e).isInstanceOf(RuntimeException::class.java)
            assertThat(e.message).isEqualTo("Repository error")
        }
    }
}