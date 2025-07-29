package io.github.caduviegas.carslist.data.model

import com.google.gson.annotations.SerializedName

data class UserDTO(
    @SerializedName("cpf") val cpf: String,
    @SerializedName("nome") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("telefone") val phone: String,
    @SerializedName("data_nascimento") val birthDate: Long
)

data class CarInfoDTO(
    @SerializedName("id") val id: Int,
    @SerializedName("modelo_id") val modelId: Int
)

data class OrderCarDTO(
    @SerializedName("pedido_id") val orderId: String,
    @SerializedName("data_pedido") val orderDate: Long,
    @SerializedName("status") val status: String,
    @SerializedName("usuario") val user: UserDTO,
    @SerializedName("carro") val car: CarInfoDTO
)