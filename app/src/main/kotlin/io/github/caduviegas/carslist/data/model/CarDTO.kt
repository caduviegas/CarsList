package io.github.caduviegas.carslist.data.model

import com.google.gson.annotations.SerializedName

data class CarDTO(
    @SerializedName("id")
    val id: Int,
    @SerializedName("timestamp_cadastro")
    val registrationDate: Long,
    @SerializedName("modelo_id")
    val modelId: Int,
    @SerializedName("ano")
    val year: Int,
    @SerializedName("combustivel")
    val fuel: String,
    @SerializedName("num_portas")
    val doors: Int,
    @SerializedName("cor")
    val color: String,
    @SerializedName("nome_modelo")
    val modelName: String,
    @SerializedName("valor")
    val price: Double
)

data class CarsApiResponse(
    @SerializedName("cars")
    val cars: List<CarDTO>
)