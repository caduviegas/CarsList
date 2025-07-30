package io.github.caduviegas.carslist.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.github.caduviegas.carslist.data.db.entity.PedidoCompra

@Dao
interface PedidoCompraDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPedido(pedido: PedidoCompra)

    @Query("UPDATE PedidoCompra SET status_pedido = :novoStatus WHERE id = :pedidoId")
    fun updateStatusPedido(pedidoId: Int, novoStatus: String)

    @Query("DELETE FROM PedidoCompra")
    fun deleteAllPedidos()

    @Query("SELECT * FROM PedidoCompra WHERE carro_id = :carroId")
    fun getPedidosByCarroId(carroId: Int): List<PedidoCompra>

    @Query("SELECT * FROM PedidoCompra")
    fun getAllPedidos(): List<PedidoCompra>
}