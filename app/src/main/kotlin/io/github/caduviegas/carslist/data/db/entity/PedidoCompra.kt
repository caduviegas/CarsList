package io.github.caduviegas.carslist.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity
data class PedidoCompra(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "carro_id") val carroId: Int,
    @ColumnInfo(name = "timestamp_cadastro") val cadastro: LocalDate,
    @ColumnInfo(name = "modelo_id") val modeloId: Int,
    @ColumnInfo(name = "ano") val ano: Int,
    @ColumnInfo(name = "combustivel") val combustivel: String,
    @ColumnInfo(name = "num_portas") val numPortas: Int,
    @ColumnInfo(name = "cor") val cor: String,
    @ColumnInfo(name = "nome_modelo") val nomeModelo: String,
    @ColumnInfo(name = "valor") val valor: Double,
    @ColumnInfo(name = "data_pedido") val dataPedido: LocalDate,
    @ColumnInfo(name = "cpf_cliente") val cpfCliente: String,
    @ColumnInfo(name = "status_pedido") val statusPedido: String
)
