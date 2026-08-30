package by.jadjer.valveclearance.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import by.jadjer.shimcalculator.models.ValveSpecification
import by.jadjer.valveclearance.domain.model.Cylinder
import by.jadjer.valveclearance.domain.model.Engine
import by.jadjer.valveclearance.domain.model.Vehicle
import by.jadjer.valveclearance.domain.repository.VehicleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class VehicleFormViewModel(
    private val repository: VehicleRepository,
    private val vehicleId: Long = -1L
) : ViewModel() {
    private val _brand = MutableStateFlow("")
    val brand = _brand.asStateFlow()

    private val _model = MutableStateFlow("")
    val model = _model.asStateFlow()

    private val _year = MutableStateFlow("")
    val year = _year.asStateFlow()

    private val _cylinders = MutableStateFlow(1)
    val cylinders = _cylinders.asStateFlow()

    private val _intakeValves = MutableStateFlow(1)
    val intakeValves = _intakeValves.asStateFlow()

    private val _exhaustValves = MutableStateFlow(1)
    val exhaustValves = _exhaustValves.asStateFlow()

    private val _intakeMin = MutableStateFlow("0.10")
    val intakeMin = _intakeMin.asStateFlow()

    private val _intakeMax = MutableStateFlow("0.20")
    val intakeMax = _intakeMax.asStateFlow()

    private val _exhaustMin = MutableStateFlow("0.20")
    val exhaustMin = _exhaustMin.asStateFlow()

    private val _exhaustMax = MutableStateFlow("0.30")
    val exhaustMax = _exhaustMax.asStateFlow()

    init {
        if (vehicleId != -1L) {
            viewModelScope.launch {
                repository.getVehicleById(vehicleId)?.let { vehicle ->
                    _brand.value = vehicle.brand
                    _model.value = vehicle.model
                    _year.value = vehicle.year.toString()
                    _cylinders.value = vehicle.engine.cylinders.size
                    val cylinder = vehicle.engine.cylinders.firstOrNull()
                    _intakeValves.value = cylinder?.intakePerCylinder ?: 1
                    _exhaustValves.value = cylinder?.exhaustPerCylinder ?: 1
                    _intakeMin.value = vehicle.specification.intakeMin.toString()
                    _intakeMax.value = vehicle.specification.intakeMax.toString()
                    _exhaustMin.value = vehicle.specification.exhaustMin.toString()
                    _exhaustMax.value = vehicle.specification.exhaustMax.toString()
                }
            }
        }
    }

    fun setBrand(value: String) { _brand.value = value }
    fun setModel(value: String) { _model.value = value }
    fun setYear(value: String) { _year.value = value }
    
    fun setCylinders(value: Int) { _cylinders.value = value }
    fun setIntakeValves(value: Int) { _intakeValves.value = value }
    fun setExhaustValves(value: Int) { _exhaustValves.value = value }
    
    fun setIntakeMin(value: String) { _intakeMin.value = value }
    fun setIntakeMax(value: String) { _intakeMax.value = value }
    fun setExhaustMin(value: String) { _exhaustMin.value = value }
    fun setExhaustMax(value: String) { _exhaustMax.value = value }

    fun saveVehicle(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val vehicle = Vehicle(
                id = if (vehicleId == -1L) 0 else vehicleId,
                brand = _brand.value,
                model = _model.value,
                year = _year.value.toIntOrNull() ?: 0,
                engine = Engine(List(_cylinders.value) { 
                    Cylinder(_intakeValves.value, _exhaustValves.value) 
                }),
                specification = ValveSpecification(
                    intakeMin = _intakeMin.value.toFloatOrNull() ?: 0f,
                    intakeMax = _intakeMax.value.toFloatOrNull() ?: 0f,
                    exhaustMin = _exhaustMin.value.toFloatOrNull() ?: 0f,
                    exhaustMax = _exhaustMax.value.toFloatOrNull() ?: 0f
                )
            )
            if (vehicleId == -1L) {
                repository.addVehicle(vehicle)
            } else {
                repository.updateVehicle(vehicle)
            }
            onSuccess()
        }
    }
}

class VehicleFormViewModelFactory(
    private val repository: VehicleRepository,
    private val vehicleId: Long = -1L
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return VehicleFormViewModel(repository, vehicleId) as T
    }
}
