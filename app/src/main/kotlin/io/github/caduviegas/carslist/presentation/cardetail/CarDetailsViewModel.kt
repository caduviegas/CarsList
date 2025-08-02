package io.github.caduviegas.carslist.presentation.cardetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.caduviegas.carslist.domain.model.Car
import io.github.caduviegas.carslist.domain.usecase.HasLoggedUserUseCase
import io.github.caduviegas.carslist.domain.usecase.ListLeadsByCarUseCase
import io.github.caduviegas.carslist.domain.usecase.SaveLeadUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CarDetailsViewModel(
    private val listLeadsByCarUseCase: ListLeadsByCarUseCase,
    private val saveLeadUseCase: SaveLeadUseCase,
    private val hasLoggedUserUseCase: HasLoggedUserUseCase
) : ViewModel() {

    private val _car = MutableStateFlow<Car?>(null)
    val car: StateFlow<Car?> = _car

    private val _leadSaveUiState = MutableStateFlow<LeadSaveUiState>(LeadSaveUiState.NoLead)
    val leadSaveUiState: StateFlow<LeadSaveUiState> = _leadSaveUiState

    fun setCar(car: Car) {
        _car.value = car
        viewModelScope.launch {
            val leads = listLeadsByCarUseCase(car.id)
            _leadSaveUiState.value = if (leads.isNotEmpty()) {
                LeadSaveUiState.AlreadyHasLead
            } else {
                LeadSaveUiState.NoLead
            }
        }
    }

    fun saveLeadForCurrentCar() {
        val currentCar = _car.value ?: return
        _leadSaveUiState.value = LeadSaveUiState.Loading
        viewModelScope.launch {
            delay(1000)
            val hasUser = hasLoggedUserUseCase()
            if (!hasUser) {
                _leadSaveUiState.value = LeadSaveUiState.NoUser
                return@launch
            }
            try {
                saveLeadUseCase(currentCar)
                _leadSaveUiState.value = LeadSaveUiState.Success
            } catch (e: Exception) {
                _leadSaveUiState.value = LeadSaveUiState.Error
            }
        }
    }
}
