package by.jadjer.valveclearance.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import by.jadjer.shimcalculator.models.ValveMeasurement
import by.jadjer.valveclearance.domain.repository.VehicleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SessionDetailsViewModel(
    private val repository: VehicleRepository,
    private val sessionId: Long
) : ViewModel() {
    private val _measurements = MutableStateFlow<List<ValveMeasurement>>(emptyList())
    val measurements = _measurements.asStateFlow()

    init {
        viewModelScope.launch {
            _measurements.value = repository.getMeasurementsForSession(sessionId)
        }
    }

    fun deleteSession(onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.deleteSession(sessionId)
            onSuccess()
        }
    }
}

class SessionDetailsViewModelFactory(
    private val repository: VehicleRepository,
    private val sessionId: Long
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SessionDetailsViewModel(repository, sessionId) as T
    }
}
