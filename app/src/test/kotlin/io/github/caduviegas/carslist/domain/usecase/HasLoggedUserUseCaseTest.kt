package io.github.caduviegas.carslist.domain.usecase

import com.google.common.truth.Truth.assertThat
import io.github.caduviegas.carslist.domain.exception.UserNotLoggedInException
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
class HasLoggedUserUseCaseTest {

    private val repository: CarDatabaseRepository = mockk()
    private lateinit var useCase: HasLoggedUserUseCase

    @Before
    fun setUp() {
        useCase = HasLoggedUserUseCase(repository)
    }

    @Test
    fun `should return true when user is logged in`() = runTest {
        val user = User(
            cpf = "12345678900",
            name = "João da Silva",
            email = "joao@gmail.com",
            phone = "11999999999",
            birthday = LocalDate.of(1991, 12, 20)
        )
        coEvery { repository.getLoggedUser() } returns user

        val result = useCase()

        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when user is not logged in`() = runTest {
        coEvery { repository.getLoggedUser() } throws UserNotLoggedInException()

        val result = useCase()

        assertThat(result).isFalse()
    }
}
