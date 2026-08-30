package by.jadjer.valveclearance.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import by.jadjer.valveclearance.domain.model.Vehicle
import by.jadjer.valveclearance.domain.repository.MeasurementSession
import by.jadjer.valveclearance.domain.repository.VehicleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class VehicleHistoryViewModel(
    private val repository: VehicleRepository,
    private val vehicleId: Long
) : ViewModel() {
    val sessions: StateFlow<List<MeasurementSession>> = repository.getSessionsForVehicle(vehicleId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _vehicle = MutableStateFlow<Vehicle?>(null)
    val vehicle = _vehicle.asStateFlow()

    init {
        viewModelScope.launch {
            _vehicle.value = repository.getVehicleById(vehicleId)
        }
    }
}

class VehicleHistoryViewModelFactory(
    private val repository: VehicleRepository,
    private val vehicleId: Long
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return VehicleHistoryViewModel(repository, vehicleId) as T
    }
}
