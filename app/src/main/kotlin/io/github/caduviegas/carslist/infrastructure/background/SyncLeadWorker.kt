package io.github.caduviegas.carslist.infrastructure.background

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import io.github.caduviegas.carslist.domain.model.LeadStatus
import io.github.caduviegas.carslist.domain.usecase.SyncLeadUseCase
import io.github.caduviegas.carslist.infrastructure.notification.NotificationUtils

class SyncLeadWorker(
    private val context: Context,
    params: WorkerParameters,
    private val syncLeadUseCase: SyncLeadUseCase
) : CoroutineWorker(context, params) {


    override suspend fun doWork(): Result {
        return try {
            val result: LeadStatus = syncLeadUseCase()
            when (result) {
                LeadStatus.UPDATED -> notifySuccess()
                LeadStatus.NO_USER_LOGGED -> notifyError()
            }
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    private fun notifyError() {
        NotificationUtils.showSilentNotification(context, "Faça login para sincronizar", "Para fazer seus pedidos de compra comece com logi, é rápido")
    }

    private fun notifySuccess() {
        NotificationUtils.showSilentNotification(context, "Pedidos de compra sincronizados", "Aguarde contato de um dos nossos consultores")
    }
}