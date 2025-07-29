package io.github.caduviegas.carslist.data.repository

import io.github.caduviegas.carslist.data.api.CarApiService
import io.github.caduviegas.carslist.data.mapper.CarMapper
import io.github.caduviegas.carslist.domain.model.Car
import io.github.caduviegas.carslist.domain.repository.CarRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class CarRepositoryImpl(
    private val apiService: CarApiService,
    private val mapper: CarMapper,
    private val dispatcher: CoroutineDispatcher
): CarRepository {

    override suspend fun fetchCars(): List<Car> = withContext(dispatcher) {
        val response = apiService.getCars()
        mapper.mapList(response.cars)
    }
}