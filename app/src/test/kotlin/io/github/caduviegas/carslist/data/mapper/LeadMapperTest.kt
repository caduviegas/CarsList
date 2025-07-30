package io.github.caduviegas.carslist.data.mapper

import com.google.common.truth.Truth.assertThat
import io.github.caduviegas.carslist.data.db.entity.PedidoCompra
import io.github.caduviegas.carslist.data.db.entity.User as UserEntity
import io.github.caduviegas.carslist.domain.model.FuelType
import org.junit.Test
import java.time.LocalDate

class LeadMapperTest {

    private fun makeLeadEntity(
        id: String = "lead-1",
        carroId: Int = 10,
        cadastro: LocalDate = LocalDate.of(2024, 6, 1),
        modeloId: Int = 20,
        ano: Int = 2022,
        combustivel: String = "FLEX",
        numPortas: Int = 4,
        cor: String = "Prata",
        nomeModelo: String = "Tesla Model Y",
        valor: Double = 75000.0,
        dataPedido: LocalDate = LocalDate.of(2024, 6, 2),
        cpfCliente: String = "12345678900",
        statusPedido: String = "PENDING"
    ) = PedidoCompra(
        id, carroId, cadastro, modeloId, ano, combustivel, numPortas, cor, nomeModelo, valor, dataPedido, cpfCliente, statusPedido
    )

    private fun makeUserEntity(
        cpf: String = "12345678900",
        name: String = "João da Silva",
        email: String = "joao@email.com",
        phone: String? = "11999999999",
        birthday: LocalDate? = LocalDate.of(1990, 5, 20)
    ) = UserEntity(
        cpf, name, email, phone, birthday
    )

    @Test
    fun `should map LeadEntity and UserEntity to Lead domain correctly`() {
        val leadEntity = makeLeadEntity()
        val userEntity = makeUserEntity()

        val lead = LeadMapper.toLead(leadEntity, userEntity)

        assertThat(lead.id).isEqualTo(leadEntity.id)
        assertThat(lead.date).isEqualTo(leadEntity.dataPedido)
        assertThat(lead.status).isEqualTo(leadEntity.statusPedido)

        assertThat(lead.car.id).isEqualTo(leadEntity.carroId)
        assertThat(lead.car.cadastro).isEqualTo(leadEntity.cadastro)
        assertThat(lead.car.modeloId).isEqualTo(leadEntity.modeloId)
        assertThat(lead.car.ano).isEqualTo(leadEntity.ano)
        assertThat(lead.car.fuelType).isEqualTo(FuelType.valueOf(leadEntity.combustivel))
        assertThat(lead.car.numPortas).isEqualTo(leadEntity.numPortas)
        assertThat(lead.car.cor).isEqualTo(leadEntity.cor)
        assertThat(lead.car.nomeModelo).isEqualTo(leadEntity.nomeModelo)
        assertThat(lead.car.valor).isEqualTo(leadEntity.valor)

        assertThat(lead.user.cpf).isEqualTo(userEntity.cpf)
        assertThat(lead.user.name).isEqualTo(userEntity.name)
        assertThat(lead.user.email).isEqualTo(userEntity.email)
        assertThat(lead.user.phone).isEqualTo(userEntity.phone)
        assertThat(lead.user.birthday).isEqualTo(userEntity.birthday)
    }

    @Test
    fun `should map LeadEntity and UserEntity with null phone and birthday`() {
        val leadEntity = makeLeadEntity()
        val userEntity = makeUserEntity(phone = null, birthday = null)

        val lead = LeadMapper.toLead(leadEntity, userEntity)

        assertThat(lead.user.phone).isNull()
        assertThat(lead.user.birthday).isNull()
    }

    @Test(expected = IllegalArgumentException::class)
    fun `should throw if fuel is not a valid FuelType`() {
        val leadEntity = makeLeadEntity(combustivel = "INVALID_FUEL")
        val userEntity = makeUserEntity()
        LeadMapper.toLead(leadEntity, userEntity)
    }
}