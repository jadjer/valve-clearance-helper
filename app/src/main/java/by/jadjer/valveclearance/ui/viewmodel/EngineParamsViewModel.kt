package by.jadjer.valveclearance.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import by.jadjer.valveclearance.domain.repository.ValveClearanceRepository
import by.jadjer.valveclearance.domain.repository.VehicleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EngineParamsViewModel(
    private val repository: ValveClearanceRepository,
    private val vehicleRepository: VehicleRepository? = null,
    private val vehicleId: Long = -1L
) : ViewModel() {

    private val _cylinders = MutableStateFlow(repository.cylinders.value)
    private val _intakeValves = MutableStateFlow(repository.intakeValves.value)
    private val _exhaustValves = MutableStateFlow(repository.exhaustValves.value)

    val cylinders: StateFlow<Int> = _cylinders.asStateFlow()
    val intakeValves: StateFlow<Int> = _intakeValves.asStateFlow()
    val exhaustValves: StateFlow<Int> = _exhaustValves.asStateFlow()

    init {
        if (vehicleId != -1L && vehicleRepository != null) {
            viewModelScope.launch {
                vehicleRepository.getVehicleById(vehicleId)?.let { vehicle ->
                    val cylinder = vehicle.engine.cylinders.firstOrNull()
                    _cylinders.value = vehicle.engine.cylinders.size
                    _intakeValves.value = cylinder?.intakePerCylinder ?: 1
                    _exhaustValves.value = cylinder?.exhaustPerCylinder ?: 1
                    
                    // Also auto-save to measurement repo
                    repository.setEngineData(_cylinders.value, _intakeValves.value, _exhaustValves.value)
                    repository.setClearanceLimit(
                        vehicle.specification.intakeMin,
                        vehicle.specification.intakeMax,
                        vehicle.specification.exhaustMin,
                        vehicle.specification.exhaustMax
                    )
                }
            }
        }
    }

    fun setCylinders(value: Int) {
        _cylinders.value = value
    }

    fun setIntakeValves(value: Int) {
        _intakeValves.value = value
    }

    fun setExhaustValves(value: Int) {
        _exhaustValves.value = value
    }

    fun saveData() : Boolean {
        repository.setEngineData(_cylinders.value, _intakeValves.value, _exhaustValves.value)
        return true
    }
}

class EngineParamsViewModelFactory(
    private val repository: ValveClearanceRepository,
    private val vehicleRepository: VehicleRepository? = null,
    private val vehicleId: Long = -1L
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EngineParamsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EngineParamsViewModel(repository, vehicleRepository, vehicleId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
