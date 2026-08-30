package by.jadjer.valveclearance.data.repository

import by.jadjer.shimcalculator.ClearanceCalculator
import by.jadjer.shimcalculator.models.Instruction
import by.jadjer.shimcalculator.models.Shim
import by.jadjer.shimcalculator.models.ValveMeasurement
import by.jadjer.shimcalculator.models.ValveSpecification
import by.jadjer.shimcalculator.models.ValveType
import by.jadjer.valveclearance.domain.repository.ValveClearanceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class ValveClearanceRepositoryImpl : ValveClearanceRepository {
    private val _cylinders = MutableStateFlow(1)
    private val _intakeValves = MutableStateFlow(1)
    private val _exhaustValves = MutableStateFlow(1)
    private val _measurements = MutableStateFlow<List<ValveMeasurement>>(emptyList())
    private val _specification = MutableStateFlow(ValveSpecification(intakeMin = 0f, intakeMax = 0f, exhaustMin = 0f, exhaustMax = 0f))

    override val cylinders: StateFlow<Int> = _cylinders
    override val intakeValves: StateFlow<Int> = _intakeValves
    override val exhaustValves: StateFlow<Int> = _exhaustValves
    override val measurements: StateFlow<List<ValveMeasurement>> = _measurements
    override val specification: StateFlow<ValveSpecification> = _specification

    override fun setEngineData(cylinders: Int, intakeValves: Int, exhaustValves: Int) {
        val needsReset = _cylinders.value != cylinders ||
                _intakeValves.value != intakeValves ||
                _exhaustValves.value != exhaustValves ||
                _measurements.value.isEmpty()

        if (needsReset) {
            _cylinders.value = cylinders
            _intakeValves.value = intakeValves
            _exhaustValves.value = exhaustValves

            resetMeasurements()
        }
    }

    override fun setClearanceLimit(
        intakeMin: Float,
        intakeMax: Float,
        exhaustMin: Float,
        exhaustMax: Float
    ) {
        _specification.value = ValveSpecification(intakeMin, intakeMax, exhaustMin, exhaustMax)
    }

    override fun updateMeasuredValue(valveNumber: Int, clearance: Float, shim: Float) {
        _measurements.update { currentList ->
            currentList.map { measurement ->
                if (measurement.valveNumber == valveNumber) {
                    measurement.copy(
                        clearance = clearance,
                        shim = measurement.shim.copy(size = shim)
                    )
                } else {
                    measurement
                }
            }
        }
    }

    override fun getAdjustedValves(): List<Instruction> {
        val calculator = ClearanceCalculator()
        return calculator.calculateSolutions(_measurements.value, _specification.value)
    }

    override fun reset() {
        _cylinders.value = 1
        _intakeValves.value = 1
        _exhaustValves.value = 1
        _measurements.value = emptyList()
        _specification.value = ValveSpecification(0f, 0f, 0f, 0f)
    }

    private fun resetMeasurements() {
        val currentCylinders = _cylinders.value
        val currentIntake = _intakeValves.value
        val currentExhaust = _exhaustValves.value
        
        val intakeCount = currentCylinders * currentIntake
        val exhaustCount = currentCylinders * currentExhaust

        _measurements.value = List(intakeCount + exhaustCount) { index ->
            val number = index + 1
            val type = if (number <= intakeCount) ValveType.INTAKE else ValveType.EXHAUST

            ValveMeasurement(
                valveNumber = number,
                valveType = type,
                clearance = 0f,
                shim = Shim(valveNumber = number, size = 0f),
            )
        }
    }
}