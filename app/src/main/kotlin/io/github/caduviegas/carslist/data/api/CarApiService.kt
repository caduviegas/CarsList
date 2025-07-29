package io.github.caduviegas.carslist.data.api

import io.github.caduviegas.carslist.data.model.CarsApiResponse
import retrofit2.http.GET

interface CarApiService {
    @GET("cars.json")
    suspend fun getCars(): CarsApiResponse
}