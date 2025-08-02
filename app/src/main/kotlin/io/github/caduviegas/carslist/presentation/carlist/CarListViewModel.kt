package io.github.caduviegas.carslist.presentation.carlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.caduviegas.carslist.domain.usecase.ListCarsUseCase
import io.github.caduviegas.carslist.domain.usecase.HasLoggedUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CarListViewModel(
    private val listCarsUseCase: ListCarsUseCase,
    private val hasLoggedUserUseCase: HasLoggedUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<CarListUiState>(CarListUiState.Loading)
    val uiState: StateFlow<CarListUiState> = _uiState

    private val _isUserLoggedIn = MutableStateFlow<Boolean>(false)
    val isUserLoggedIn: StateFlow<Boolean> = _isUserLoggedIn

    fun fetchCars() {
        _uiState.value = CarListUiState.Loading
        viewModelScope.launch {
            try {
                val cars = listCarsUseCase()
                _uiState.value = CarListUiState.Success(cars)
            } catch (_: Exception) {
                _uiState.value = CarListUiState.Error("Erro ao carregar carros")
            }
        }
    }

    fun checkUserLoggedIn() {
        viewModelScope.launch {
            val isLogged = hasLoggedUserUseCase()
            _isUserLoggedIn.value = isLogged
        }
    }
}