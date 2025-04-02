package by.jadjer.valveclearanceassistant.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import by.jadjer.shimcalculator.models.Instruction
import by.jadjer.valveclearanceassistant.repository.ValveClearanceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ResultsViewModel(private val repository: ValveClearanceRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<ValveAdjustmentUiState>(ValveAdjustmentUiState.Loading)
    val uiState: StateFlow<ValveAdjustmentUiState> = _uiState.asStateFlow()

    fun calculateAdjustments() {
        viewModelScope.launch {
            _uiState.value = ValveAdjustmentUiState.Loading

            try {
                val instructions: List<Instruction> = repository.getAdjustedValves()

                _uiState.value = ValveAdjustmentUiState.Success(instructions)

            } catch (e: Exception) {
                println(e.message.toString())
                _uiState.value = ValveAdjustmentUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed interface ValveAdjustmentUiState {
    object Loading : ValveAdjustmentUiState
    data class Success(val instructions: List<Instruction>) : ValveAdjustmentUiState
    data class Error(val message: String) : ValveAdjustmentUiState
}

class ResultsViewModelFactory(private val repository: ValveClearanceRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ResultsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ResultsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
