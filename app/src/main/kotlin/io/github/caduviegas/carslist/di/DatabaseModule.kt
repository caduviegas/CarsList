package io.github.caduviegas.carslist.di

import androidx.room.Room
import io.github.caduviegas.carslist.data.db.CarsDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            CarsDatabase::class.java,
            "cars_database"
        ).build()
    }
    single { get<CarsDatabase>().userDao() }
    single { get<CarsDatabase>().pedidoCompraDao() }
}