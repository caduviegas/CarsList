package io.github.caduviegas.carslist.data.repository

import io.github.caduviegas.carslist.data.api.CarApiService
import io.github.caduviegas.carslist.data.mapper.CarMapper
import io.github.caduviegas.carslist.data.mapper.OrderCarMapper
import io.github.caduviegas.carslist.domain.model.Car
import io.github.caduviegas.carslist.domain.model.Lead
import io.github.caduviegas.carslist.domain.repository.CarApiRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class CarApiRepositoryImpl(
    private val apiService: CarApiService,
    private val mapper: CarMapper,
    private val orderCarMapper: OrderCarMapper,
    private val dispatcher: CoroutineDispatcher
) : CarApiRepository {

    override suspend fun fetchCars(): List<Car> = withContext(dispatcher) {
        val response = apiService.getCars()
        mapper.mapList(response.cars)
    }

    override suspend fun postOrderCar(lead: Lead) = withContext(dispatcher) {
        val orderCarDTO = orderCarMapper.toOrderCarDTO(lead)
        apiService.postOrderCar(orderCarDTO)
    }
}
