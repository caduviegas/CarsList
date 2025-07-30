package io.github.caduviegas.carslist.infrastructure.background

import android.content.Context
import androidx.work.ListenableWorker.Result
import androidx.work.WorkerParameters
import io.github.caduviegas.carslist.domain.model.LeadStatus
import io.github.caduviegas.carslist.domain.usecase.SyncLeadUseCase
import io.github.caduviegas.carslist.infrastructure.notification.NotificationUtils
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SyncLeadWorkerTest {

    private val context = mockk<Context>(relaxed = true)
    private val params = mockk<WorkerParameters>(relaxed = true)
    private val useCase = mockk<SyncLeadUseCase>()
    private lateinit var worker: SyncLeadWorker

    @Before
    fun setUp() {
        mockkObject(NotificationUtils)
        worker = SyncLeadWorker(context, params, useCase)
    }

    @Test
    fun `should notify success when LeadStatus is UPDATED`() = runTest {
        coEvery { useCase.invoke() } returns LeadStatus.UPDATED
        every { NotificationUtils.showSilentNotification(any(), any(), any()) } just Runs

        val result = worker.doWork()

        verify { NotificationUtils.showSilentNotification(context, any(), any()) }
        assert(result == Result.success())
    }

    @Test
    fun `should notify error when LeadStatus is NO_USER_LOGGED`() = runTest {
        coEvery { useCase.invoke() } returns LeadStatus.NO_USER_LOGGED
        every { NotificationUtils.showSilentNotification(any(), any(), any()) } just Runs

        val result = worker.doWork()

        verify { NotificationUtils.showSilentNotification(context, any(), any()) }
        assert(result == Result.success())
    }

    @Test
    fun `should retry on exception`() = runTest {
        coEvery { useCase.invoke() } throws RuntimeException("Exception!")
        val result = worker.doWork()
        assert(result == Result.retry())
    }
}