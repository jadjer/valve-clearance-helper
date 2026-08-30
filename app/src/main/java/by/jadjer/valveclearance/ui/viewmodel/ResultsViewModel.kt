package by.jadjer.valveclearance.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import by.jadjer.shimcalculator.models.Instruction
import by.jadjer.valveclearance.domain.repository.ValveClearanceRepository
import by.jadjer.valveclearance.domain.repository.VehicleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ResultsViewModel(
    private val repository: ValveClearanceRepository,
    private val vehicleRepository: VehicleRepository? = null,
    private val vehicleId: Long = -1L
) : ViewModel() {

    private val _uiState = MutableStateFlow<ValveAdjustmentUiState>(ValveAdjustmentUiState.Loading)
    val uiState: StateFlow<ValveAdjustmentUiState> = _uiState.asStateFlow()

    fun calculateAdjustments() {
        viewModelScope.launch {
            _uiState.value = ValveAdjustmentUiState.Loading

            try {
                val instructions: List<Instruction> = repository.getAdjustedValves()
                _uiState.value = ValveAdjustmentUiState.Success(instructions)
            } catch (e: Exception) {
                _uiState.value = ValveAdjustmentUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
    
    fun saveSession(onComplete: () -> Unit) {
        if (vehicleId == -1L || vehicleRepository == null) {
            onComplete()
            return
        }
        
        viewModelScope.launch {
            val vehicle = vehicleRepository.getVehicleById(vehicleId) ?: return@launch
            vehicleRepository.saveMeasurementSession(vehicle, repository.measurements.value)
            onComplete()
        }
    }
}

sealed interface ValveAdjustmentUiState {
    object Loading : ValveAdjustmentUiState
    data class Success(val instructions: List<Instruction>) : ValveAdjustmentUiState
    data class Error(val message: String) : ValveAdjustmentUiState
}

class ResultsViewModelFactory(
    private val repository: ValveClearanceRepository,
    private val vehicleRepository: VehicleRepository? = null,
    private val vehicleId: Long = -1L
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ResultsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ResultsViewModel(repository, vehicleRepository, vehicleId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
