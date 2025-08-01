package io.github.caduviegas.carslist.data.repository

import io.github.caduviegas.carslist.data.db.dao.PedidoCompraDao
import io.github.caduviegas.carslist.data.db.dao.UserDao
import io.github.caduviegas.carslist.data.db.entity.PedidoCompra as LeadEntity
import io.github.caduviegas.carslist.data.db.entity.User as UserEntity
import io.github.caduviegas.carslist.data.mapper.LeadMapper
import io.github.caduviegas.carslist.domain.exception.UserNotLoggedInException
import io.github.caduviegas.carslist.domain.model.Lead
import io.github.caduviegas.carslist.domain.model.User
import io.github.caduviegas.carslist.domain.repository.CarDatabaseRepository
import io.github.caduviegas.carslist.domain.util.OrderConstants

class CarDatabaseRepositoryImpl(
    private val leadDao: PedidoCompraDao,
    private val userDao: UserDao
) : CarDatabaseRepository {

    private suspend fun validateIfHasLoggedUser(){
        val hasUser = userDao.hasLoggedUser()
        if (!hasUser) throw UserNotLoggedInException()
    }

    override suspend fun saveLead(lead: Lead) {
        validateIfHasLoggedUser()
        val lead = LeadEntity(
            id = lead.id,
            carroId = lead.car.id,
            cadastro = lead.car.cadastro,
            modeloId = lead.car.modeloId,
            ano = lead.car.ano,
            combustivel = lead.car.fuelType.name,
            numPortas = lead.car.numPortas,
            cor = lead.car.cor,
            nomeModelo = lead.car.nomeModelo,
            valor = lead.car.valor,
            dataPedido = lead.date,
            cpfCliente = lead.user.cpf,
            statusPedido = lead.status
        )
        leadDao.insertPedido(lead)
    }

    override suspend fun retrieveNotSyncedLeads(): List<Lead> {
        validateIfHasLoggedUser()
        val userEntity = userDao.getLoggedUser()
        val leads = leadDao.getAllByStatus(OrderConstants.STATUS_NEW)
        return leads.map { pedido ->
            LeadMapper.toLead(pedido, userEntity)
        }
    }

    override suspend fun getLoggedUser(): User {
        validateIfHasLoggedUser()
        val userEntity: UserEntity = userDao.getLoggedUser()
        return User(
            cpf = userEntity.cpf,
            name = userEntity.name,
            email = userEntity.email,
            phone = userEntity.phone,
            birthday = userEntity.birthday
        )
    }

    override suspend fun updateLeadStatus(id: String, status: String) {
        validateIfHasLoggedUser()
        leadDao.updateStatusPedido(id, status)
    }

    override suspend fun getLeadsByCarId(carId: Int): List<Lead> {
        validateIfHasLoggedUser()
        val userEntity = userDao.getLoggedUser()
        val leads = leadDao.getPedidosByCarroId(carId)
        return leads.map { pedido ->
            LeadMapper.toLead(pedido, userEntity)
        }
    }

    override suspend fun getAllLeads(): List<Lead> {
        if (userDao.hasLoggedUser().not()) return emptyList()
        val userEntity = userDao.getLoggedUser()
        val leads = leadDao.getAllPedidos()
        return leads.map { lead ->
            LeadMapper.toLead(lead, userEntity)
        }
    }

    override suspend fun deleteAllData() {
        leadDao.deleteAllPedidos()
        userDao.deleteAllUsers()
    }

    override suspend fun saveUser(user: User) {
        val userEntity = UserEntity(
            cpf = user.cpf,
            name = user.name,
            email = user.email,
            phone = user.phone,
            birthday = user.birthday
        )
        userDao.insertUser(userEntity)
    }
}