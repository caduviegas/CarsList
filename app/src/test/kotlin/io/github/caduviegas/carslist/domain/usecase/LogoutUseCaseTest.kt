package io.github.caduviegas.carslist.domain.usecase

import com.google.common.truth.Truth.assertThat
import io.github.caduviegas.carslist.domain.repository.CarDatabaseRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LogoutUseCaseTest {

    private val repository: CarDatabaseRepository = mockk(relaxed = true)
    private lateinit var useCase: LogoutUseCase

    @Before
    fun setUp() {
        useCase = LogoutUseCase(repository)
    }

    @Test
    fun `should call deleteAllData on repository`() = runTest {
        useCase()
        coVerify(exactly = 1) { repository.deleteAllData() }
    }

    @Test
    fun `should propagate exception from repository`() = runTest {
        coEvery { repository.deleteAllData() } throws RuntimeException("Repository error")
        try {
            useCase()
            assertThat("Exception").isNull()
        } catch (e: Exception) {
            assertThat(e).isInstanceOf(RuntimeException::class.java)
            assertThat(e.message).isEqualTo("Repository error")
        }
    }
}
