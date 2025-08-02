package io.github.caduviegas.carslist.presentation.carlist

import io.github.caduviegas.carslist.domain.model.Car

sealed class CarListUiState {
    object Loading : CarListUiState()
    data class Success(val cars: List<Car>) : CarListUiState()
    data class Error(val message: String) : CarListUiState()
}
