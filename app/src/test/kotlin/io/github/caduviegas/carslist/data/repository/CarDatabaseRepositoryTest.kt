package io.github.caduviegas.carslist.data.repository

import com.google.common.truth.Truth.assertThat
import io.github.caduviegas.carslist.data.db.dao.PedidoCompraDao
import io.github.caduviegas.carslist.data.db.dao.UserDao
import io.github.caduviegas.carslist.data.db.entity.PedidoCompra as LeadEntity
import io.github.caduviegas.carslist.data.db.entity.User as UserEntity
import io.github.caduviegas.carslist.data.mapper.LeadMapper
import io.github.caduviegas.carslist.domain.exception.UserNotLoggedInException
import io.github.caduviegas.carslist.domain.model.*
import io.github.caduviegas.carslist.domain.util.OrderConstants
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class CarDatabaseRepositoryTest {

    private val leadDao: PedidoCompraDao = mockk(relaxed = true)
    private val userDao: UserDao = mockk(relaxed = true)
    private lateinit var repository: CarDatabaseRepositoryImpl

    @Before
    fun setUp() {
        repository = CarDatabaseRepositoryImpl(leadDao, userDao)
        mockkObject(LeadMapper)
    }

    @Test
    fun `saveLead should insert lead when user is logged in`() = runTest {
        coEvery { userDao.hasLoggedUser() } returns true
        val lead = makeLead()
        coEvery { leadDao.insertPedido(any()) } just Runs

        repository.saveLead(lead)

        coVerify(exactly = 1) { leadDao.insertPedido(withArg {
            assertThat(it.id).isEqualTo(lead.id)
            assertThat(it.cpfCliente).isEqualTo(lead.user.cpf)
        }) }
    }

    @Test(expected = UserNotLoggedInException::class)
    fun `saveLead should throw if no user logged in`() = runTest {
        coEvery { userDao.hasLoggedUser() } returns false
        repository.saveLead(makeLead())
    }

    @Test
    fun `retrieveNotSyncedLeads should return mapped leads when user is logged in`() = runTest {
        coEvery { userDao.hasLoggedUser() } returns true
        val userEntity = makeUserEntity()
        val leadEntity = makeLeadEntity()
        coEvery { userDao.getLoggedUser() } returns userEntity
        coEvery { leadDao.getAllByStatus(OrderConstants.STATUS_NEW) } returns listOf(leadEntity)
        val mappedLead = makeLead()
        every { LeadMapper.toLead(leadEntity, userEntity) } returns mappedLead

        val result = repository.retrieveNotSyncedLeads()

        assertThat(result).containsExactly(mappedLead)
    }

    @Test(expected = UserNotLoggedInException::class)
    fun `retrieveNotSyncedLeads should throw if no user logged in`() = runTest {
        coEvery { userDao.hasLoggedUser() } returns false
        repository.retrieveNotSyncedLeads()
    }

    @Test
    fun `getLoggedUser should return mapped user when logged in`() = runTest {
        coEvery { userDao.hasLoggedUser() } returns true
        val userEntity = makeUserEntity()
        coEvery { userDao.getLoggedUser() } returns userEntity

        val result = repository.getLoggedUser()

        assertThat(result.cpf).isEqualTo(userEntity.cpf)
        assertThat(result.name).isEqualTo(userEntity.name)
        assertThat(result.email).isEqualTo(userEntity.email)
        assertThat(result.phone).isEqualTo(userEntity.phone)
        assertThat(result.birthday).isEqualTo(userEntity.birthday)
    }

    @Test(expected = UserNotLoggedInException::class)
    fun `getLoggedUser should throw if no user logged in`() = runTest {
        coEvery { userDao.hasLoggedUser() } returns false
        repository.getLoggedUser()
    }

    @Test
    fun `updateLeadStatus should call dao when user is logged in`() = runTest {
        coEvery { userDao.hasLoggedUser() } returns true
        coEvery { leadDao.updateStatusPedido(any(), any()) } just Runs

        repository.updateLeadStatus("id123", "SYNCED")

        coVerify { leadDao.updateStatusPedido("id123", "SYNCED") }
    }

    @Test(expected = UserNotLoggedInException::class)
    fun `updateLeadStatus should throw if no user logged in`() = runTest {
        coEvery { userDao.hasLoggedUser() } returns false
        repository.updateLeadStatus("id123", "SYNCED")
    }

    @Test
    fun `getLeadsByCarId should return mapped leads when user is logged in`() = runTest {
        coEvery { userDao.hasLoggedUser() } returns true
        val userEntity = makeUserEntity()
        val leadEntity = makeLeadEntity()
        coEvery { userDao.getLoggedUser() } returns userEntity
        coEvery { leadDao.getPedidosByCarroId(10) } returns listOf(leadEntity)
        val mappedLead = makeLead()
        every { LeadMapper.toLead(leadEntity, userEntity) } returns mappedLead

        val result = repository.getLeadsByCarId(10)

        assertThat(result).containsExactly(mappedLead)
    }

    @Test(expected = UserNotLoggedInException::class)
    fun `getLeadsByCarId should throw if no user logged in`() = runTest {
        coEvery { userDao.hasLoggedUser() } returns false
        repository.getLeadsByCarId(10)
    }

    @Test
    fun `deleteAllData should call DAOs`() = runTest {
        coEvery { leadDao.deleteAllPedidos() } just Runs
        coEvery { userDao.deleteAllUsers() } just Runs

        repository.deleteAllData()

        coVerify { leadDao.deleteAllPedidos() }
        coVerify { userDao.deleteAllUsers() }
    }

    @Test
    fun `saveUser should insert user entity`() = runTest {
        val user = makeUser()
        coEvery { userDao.insertUser(any()) } just Runs

        repository.saveUser(user)

        coVerify { userDao.insertUser(withArg {
            assertThat(it.cpf).isEqualTo(user.cpf)
            assertThat(it.name).isEqualTo(user.name)
            assertThat(it.email).isEqualTo(user.email)
            assertThat(it.phone).isEqualTo(user.phone)
            assertThat(it.birthday).isEqualTo(user.birthday)
        }) }
    }

    private fun makeUser() = User(
        cpf = "12345678900",
        name = "João",
        email = "joao@email.com",
        phone = "11999999999",
        birthday = LocalDate.of(1990, 1, 1)
    )

    private fun makeUserEntity() = UserEntity(
        cpf = "12345678900",
        name = "João",
        email = "joao@email.com",
        phone = "11999999999",
        birthday = LocalDate.of(1990, 1, 1)
    )

    private fun makeLeadEntity() = LeadEntity(
        id = "lead-1",
        carroId = 10,
        cadastro = LocalDate.of(2024, 6, 1),
        modeloId = 20,
        ano = 2022,
        combustivel = "FLEX",
        numPortas = 4,
        cor = "Azul",
        nomeModelo = "Sedan",
        valor = 75000.0,
        dataPedido = LocalDate.of(2024, 6, 2),
        cpfCliente = "12345678900",
        statusPedido = "PENDING"
    )

    private fun makeLead() = Lead(
        id = "lead-1",
        date = LocalDate.of(2024, 6, 2),
        status = "PENDING",
        car = Car(
            id = 10,
            cadastro = LocalDate.of(2024, 6, 1),
            modeloId = 20,
            ano = 2022,
            fuelType = FuelType.FLEX,
            numPortas = 4,
            cor = "Azul",
            nomeModelo = "Sedan",
            valor = 75000.0
        ),
        user = makeUser()
    )
}