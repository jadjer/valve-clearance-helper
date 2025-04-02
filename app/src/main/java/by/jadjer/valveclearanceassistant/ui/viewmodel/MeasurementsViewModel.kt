package by.jadjer.valveclearanceassistant.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import by.jadjer.shimcalculator.models.ValveMeasurement
import by.jadjer.shimcalculator.models.ValveType
import by.jadjer.valveclearanceassistant.repository.ValveClearanceRepository
import kotlinx.coroutines.flow.StateFlow

class MeasurementsViewModel(private val repository: ValveClearanceRepository) : ViewModel() {
    val measurements: StateFlow<List<ValveMeasurement>> = repository.measurements

    fun addMeasuredValve(valveIndex: Int, valveType: ValveType, clearance: Float, shim: Float) {
        repository.addMeasuredValve(
            valveIndex = valveIndex,
            valveType = valveType,
            clearance = clearance,
            shim = shim,
        )
    }
}

class MeasurementsViewModelFactory(private val repository: ValveClearanceRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MeasurementsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MeasurementsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
