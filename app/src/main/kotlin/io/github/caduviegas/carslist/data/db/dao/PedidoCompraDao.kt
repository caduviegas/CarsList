package io.github.caduviegas.carslist.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.github.caduviegas.carslist.data.db.entity.PedidoCompra

@Dao
interface PedidoCompraDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPedido(lead: PedidoCompra)

    @Query("UPDATE PedidoCompra SET status_pedido = :newStatus WHERE id = :leadId")
    suspend fun updateStatusPedido(leadId: String, newStatus: String)

    @Query("DELETE FROM PedidoCompra")
    suspend fun deleteAllPedidos()

    @Query("SELECT * FROM PedidoCompra WHERE carro_id = :carId")
    suspend fun getPedidosByCarroId(carId: Int): List<PedidoCompra>

    @Query("SELECT * FROM PedidoCompra WHERE status_pedido = :status")
    suspend fun getAllByStatus(status: String): List<PedidoCompra>

    @Query("SELECT * FROM PedidoCompra")
    suspend fun getAllPedidos(): List<PedidoCompra>
}