package io.github.caduviegas.carslist.data.mapper

import io.github.caduviegas.carslist.domain.model.Car
import io.github.caduviegas.carslist.domain.model.FuelType
import io.github.caduviegas.carslist.domain.model.Lead
import io.github.caduviegas.carslist.domain.model.User
import io.github.caduviegas.carslist.data.db.entity.PedidoCompra as LeadEntity
import io.github.caduviegas.carslist.data.db.entity.User as UserEntity

object LeadMapper {
    fun toLead(
        lead: LeadEntity,
        user: UserEntity
    ): Lead {
        val car = Car(
            id = lead.carroId,
            cadastro = lead.cadastro,
            modeloId = lead.modeloId,
            ano = lead.ano,
            fuelType = FuelType.valueOf(lead.combustivel),
            numPortas = lead.numPortas,
            cor = lead.cor,
            nomeModelo = lead.nomeModelo,
            valor = lead.valor
        )
        val domainUser = User(
            cpf = user.cpf,
            name = user.name,
            email = user.email,
            phone = user.phone,
            birthday = user.birthday
        )
        return Lead(
            id = lead.id.toString(),
            date = lead.dataPedido,
            status = lead.statusPedido,
            car = car,
            user = domainUser
        )
    }
}
