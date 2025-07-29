package io.github.caduviegas.carslist.db.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import io.github.caduviegas.carslist.db.CarsDatabase
import io.github.caduviegas.carslist.db.entity.PedidoCompra
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.time.LocalDate

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class PedidoCompraDaoTest {

    private lateinit var db: CarsDatabase
    private lateinit var dao: PedidoCompraDao

    @Before
    fun setUp() {
        val context: Context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(context, CarsDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.pedidoCompraDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    private fun makePedido(
        id: Int = 0,
        carroId: Int = 1,
        cadastro: LocalDate = LocalDate.of(2024, 6, 1),
        modeloId: Int = 10,
        ano: Int = 2022,
        combustivel: String = "Gasoline",
        numPortas: Int = 4,
        cor: String = "Black",
        nomeModelo: String = "Sedan",
        valor: Double = 50000.0,
        dataPedido: LocalDate = LocalDate.of(2024, 6, 2),
        cpfCliente: String = "12345678900",
        statusPedido: String = "PENDING"
    ) = PedidoCompra(
        id, carroId, cadastro, modeloId, ano, combustivel, numPortas, cor,
        nomeModelo, valor, dataPedido, cpfCliente, statusPedido
    )

    @Test
    fun `should insert an order and return all orders`() {
        val pedido = makePedido()
        dao.insertPedido(pedido)
        val pedidos = dao.getAllPedidos()
        assertThat(pedidos).hasSize(1)
        assertThat(pedidos.first().carroId).isEqualTo(pedido.carroId)
        assertThat(pedidos.first().statusPedido).isEqualTo("PENDING")
    }

    @Test
    fun `should fetch orders by carId correctly`() {
        val pedido1 = makePedido(carroId = 1)
        val pedido2 = makePedido(carroId = 2)
        dao.insertPedido(pedido1)
        dao.insertPedido(pedido2)
        val pedidosCarro = dao.getPedidosByCarroId(1)
        assertThat(pedidosCarro).hasSize(1)
        assertThat(pedidosCarro.first().carroId).isEqualTo(1)
    }

    @Test
    fun `should update the order status`() {
        val pedido = makePedido()
        dao.insertPedido(pedido)
        val pedidoId = dao.getAllPedidos().first().id
        dao.updateStatusPedido(pedidoId, "APPROVED")
        val updated = dao.getAllPedidos().first()
        assertThat(updated.statusPedido).isEqualTo("APPROVED")
    }

    @Test
    fun `should remove all orders`() {
        dao.insertPedido(makePedido())
        dao.insertPedido(makePedido(carroId = 2))
        dao.deleteAllPedidos()
        val pedidos = dao.getAllPedidos()
        assertThat(pedidos).isEmpty()
    }

    @Test
    fun `should insert multiple orders and return all`() {
        val pedidos = listOf(
            makePedido(carroId = 1),
            makePedido(carroId = 2),
            makePedido(carroId = 3)
        )
        pedidos.forEach { dao.insertPedido(it) }
        val allPedidos = dao.getAllPedidos()
        assertThat(allPedidos).hasSize(3)
    }
}