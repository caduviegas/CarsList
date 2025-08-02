package io.github.caduviegas.carslist.presentation.leads

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.caduviegas.carslist.domain.model.Lead
import io.github.caduviegas.carslist.domain.usecase.ListAllLeadsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LeadsViewModel(
    private val listAllLeadsUseCase: ListAllLeadsUseCase
) : ViewModel() {

    private val _leads = MutableStateFlow<List<Lead>>(emptyList())
    val leads: StateFlow<List<Lead>> = _leads

    fun fetchLeads() {
        viewModelScope.launch {
            val result = listAllLeadsUseCase()
            _leads.value = result
        }
    }
}
