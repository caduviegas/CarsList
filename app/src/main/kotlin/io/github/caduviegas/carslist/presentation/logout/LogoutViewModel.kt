package io.github.caduviegas.carslist.presentation.logout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.caduviegas.carslist.domain.usecase.LogoutUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LogoutViewModel(
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<LogoutUiState>(LogoutUiState.Initial)
    val uiState: StateFlow<LogoutUiState> = _uiState

    fun logout() {
        _uiState.value = LogoutUiState.Loading
        viewModelScope.launch {
                logoutUseCase()
                _uiState.value = LogoutUiState.Success
        }
    }
}