package by.jadjer.valveclearance.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import by.jadjer.shimcalculator.models.ValveMeasurement
import by.jadjer.valveclearance.repository.ValveClearanceRepository

class MeasurementsViewModel(private val repository: ValveClearanceRepository) : ViewModel() {

    private val _measurements = mutableStateListOf<ValveMeasurement>()
    val measurements: List<ValveMeasurement> get() = _measurements

    init {
        loadMeasurements()
    }

    fun updateMeasuredValue(valveNumber: Int, clearance: Float, shim: Float) {
        repository.updateMeasuredValue(valveNumber, clearance, shim)
        loadMeasurements()
    }

    fun isValid(): Boolean {
        return _measurements.all { measurement ->
            measurement.clearance > 0 && measurement.shim.size > 0
        }
    }

    private fun loadMeasurements() {
        _measurements.clear()
        _measurements.addAll(repository.measurements)
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
