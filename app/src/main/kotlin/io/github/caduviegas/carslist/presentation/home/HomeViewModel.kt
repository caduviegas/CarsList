package io.github.caduviegas.carslist.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.caduviegas.carslist.domain.usecase.HasLoggedUserUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val hasLoggedUserUseCase: HasLoggedUserUseCase
) : ViewModel() {

    private val _isLoggedIn = MutableStateFlow<Boolean?>(null)
    val isLoggedIn: StateFlow<Boolean?> = _isLoggedIn

    fun validateUser() {
        checkIfUserIsLogged()
    }

    private fun checkIfUserIsLogged() {
        viewModelScope.launch {
            delay(2000)
            val logged = hasLoggedUserUseCase()
            _isLoggedIn.value = logged
        }
    }
}