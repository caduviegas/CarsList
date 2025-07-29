package io.github.caduviegas.carslist.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.github.caduviegas.carslist.data.db.converter.LocalDateConverter
import io.github.caduviegas.carslist.data.db.dao.PedidoCompraDao
import io.github.caduviegas.carslist.data.db.dao.UserDao
import io.github.caduviegas.carslist.data.db.entity.PedidoCompra
import io.github.caduviegas.carslist.data.db.entity.User

@Database(entities = [User::class, PedidoCompra::class], version = 1)
@TypeConverters(LocalDateConverter::class)
abstract class CarsDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun pedidoCompraDao(): PedidoCompraDao
}