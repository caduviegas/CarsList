package io.github.caduviegas.carslist.data.api

import io.github.caduviegas.carslist.data.model.CarsApiResponse
import io.github.caduviegas.carslist.data.model.OrderCarDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface CarApiService {
    @GET("cars.json")
    suspend fun getCars(): CarsApiResponse

    @POST("cars/leads")
    suspend fun postOrderCar(@Body order: OrderCarDTO)
}
