package io.github.caduviegas.carslist.data.db.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import io.github.caduviegas.carslist.data.db.CarsDatabase
import io.github.caduviegas.carslist.data.db.entity.PedidoCompra
import kotlinx.coroutines.runBlocking
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
        id: String = "id-1",
        carroId: Int = 1,
        cadastro: LocalDate = LocalDate.of(2024, 6, 1),
        modeloId: Int = 10,
        ano: Int = 2022,
        combustivel: String = "Gasolina",
        numPortas: Int = 4,
        cor: String = "Prata",
        nomeModelo: String = "Tesla Model Y",
        valor: Double = 50000.0,
        dataPedido: LocalDate = LocalDate.of(2024, 6, 2),
        cpfCliente: String = "12345678900",
        statusPedido: String = "PENDING"
    ) = PedidoCompra(
        id, carroId, cadastro, modeloId, ano, combustivel, numPortas, cor,
        nomeModelo, valor, dataPedido, cpfCliente, statusPedido
    )

    @Test
    fun `should insert an order and return all orders`() = runBlocking {
        val pedido = makePedido()
        dao.insertPedido(pedido)
        val pedidos = dao.getAllPedidos()
        assertThat(pedidos).hasSize(1)
        assertThat(pedidos.first().carroId).isEqualTo(pedido.carroId)
        assertThat(pedidos.first().statusPedido).isEqualTo("PENDING")
    }

    @Test
    fun `should fetch orders by carId correctly`() = runBlocking {
        val pedido1 = makePedido(id = "id-1", carroId = 1)
        val pedido2 = makePedido(id = "id-2", carroId = 2)
        dao.insertPedido(pedido1)
        dao.insertPedido(pedido2)
        val pedidosCarro = dao.getPedidosByCarroId(1)
        assertThat(pedidosCarro).hasSize(1)
        assertThat(pedidosCarro.first().carroId).isEqualTo(1)
    }

    @Test
    fun `should update the order status`() = runBlocking {
        val pedido = makePedido(id = "id-1")
        dao.insertPedido(pedido)
        dao.updateStatusPedido("id-1", "APPROVED")
        val updated = dao.getAllPedidos().first()
        assertThat(updated.statusPedido).isEqualTo("APPROVED")
    }

    @Test
    fun `should not update status if id does not exist`() = runBlocking {
        val pedido = makePedido(id = "id-1")
        dao.insertPedido(pedido)
        dao.updateStatusPedido("id-2", "APPROVED")
        val unchanged = dao.getAllPedidos().first()
        assertThat(unchanged.statusPedido).isEqualTo("PENDING")
    }

    @Test
    fun `should remove all orders`() = runBlocking {
        dao.insertPedido(makePedido(id = "id-1"))
        dao.insertPedido(makePedido(id = "id-2", carroId = 2))
        dao.deleteAllPedidos()
        val pedidos = dao.getAllPedidos()
        assertThat(pedidos).isEmpty()
    }

    @Test
    fun `should insert multiple orders and return all`() = runBlocking {
        val pedidos = listOf(
            makePedido(id = "id-1", carroId = 1),
            makePedido(id = "id-2", carroId = 2),
            makePedido(id = "id-3", carroId = 3)
        )
        pedidos.forEach { dao.insertPedido(it) }
        val allPedidos = dao.getAllPedidos()
        assertThat(allPedidos).hasSize(3)
    }

    @Test
    fun `should fetch orders by status correctly`() = runBlocking {
        val pedido1 = makePedido(id = "id-1", statusPedido = "PENDING")
        val pedido2 = makePedido(id = "id-2", statusPedido = "APPROVED")
        val pedido3 = makePedido(id = "id-3", statusPedido = "PENDING")
        dao.insertPedido(pedido1)
        dao.insertPedido(pedido2)
        dao.insertPedido(pedido3)
        val pendings = dao.getAllByStatus("PENDING")
        val approveds = dao.getAllByStatus("APPROVED")
        assertThat(pendings).hasSize(2)
        assertThat(approveds).hasSize(1)
        assertThat(approveds.first().id).isEqualTo("id-2")
    }
}