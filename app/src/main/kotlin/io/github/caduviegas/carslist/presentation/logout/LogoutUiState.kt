package io.github.caduviegas.carslist.presentation.logout

sealed class LogoutUiState {
    object Initial : LogoutUiState()
    object Loading : LogoutUiState()
    object Success : LogoutUiState()
}
