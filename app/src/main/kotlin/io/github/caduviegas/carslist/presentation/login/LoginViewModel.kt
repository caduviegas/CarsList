package io.github.caduviegas.carslist.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.caduviegas.carslist.domain.model.User
import io.github.caduviegas.carslist.domain.usecase.LoginUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Initial)
    val uiState: StateFlow<LoginUiState> = _uiState

    fun registerUser(
        cpf: String?,
        name: String?,
        email: String?,
        phone: String?,
        birthday: LocalDate?
    ) {
        if (cpf.isNullOrBlank()) {
            _uiState.value = LoginUiState.Error("CPF é obrigatório")
            return
        }
        if (name.isNullOrBlank()) {
            _uiState.value = LoginUiState.Error("Nome é obrigatório")
            return
        }
        if (email.isNullOrBlank()) {
            _uiState.value = LoginUiState.Error("Email é obrigatório")
            return
        }

        _uiState.value = LoginUiState.Loading

        viewModelScope.launch {
            try {
                val user = User(
                    cpf = cpf,
                    name = name,
                    email = email,
                    phone = phone,
                    birthday = birthday
                )
                loginUseCase(user)
                delay(1000)
                _uiState.value = LoginUiState.Success
            } catch (_: Exception) {
                delay(1000)
                _uiState.value = LoginUiState.Error("Erro ao cadastrar usuário")
            }
        }
    }
}