package by.jadjer.valveclearance.data.repository

import android.hardware.usb.UsbDevice
import by.jadjer.shimcalculator.ClearanceCalculator
import by.jadjer.shimcalculator.models.Instruction
import by.jadjer.shimcalculator.models.Shim
import by.jadjer.shimcalculator.models.ValveMeasurement
import by.jadjer.shimcalculator.models.ValveSpecification
import by.jadjer.shimcalculator.models.ValveType
import by.jadjer.valveclearance.domain.repository.ValveClearanceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ValveClearanceRepositoryImpl : ValveClearanceRepository {
    private var _cylinders = MutableStateFlow<Int>(1)
    private var _intakeValves = MutableStateFlow<Int>(1)
    private var _exhaustValves = MutableStateFlow<Int>(1)
    private var _measurements = MutableStateFlow<List<ValveMeasurement>>(emptyList())
    private var _specification = MutableStateFlow<ValveSpecification>(ValveSpecification(intakeMin = 0f, intakeMax = 0f, exhaustMin = 0f, exhaustMax = 0f))

    override val cylinders: StateFlow<Int> = _cylinders
    override val intakeValves: StateFlow<Int> = _intakeValves
    override val exhaustValves: StateFlow<Int> = _exhaustValves
    override val measurements: StateFlow<List<ValveMeasurement>> = _measurements
    override val specification: StateFlow<ValveSpecification> = _specification

    override fun setEngineData(cylinders: Int, intakeValves: Int, exhaustValves: Int) {
        val needsReset = _cylinders.value != cylinders ||
                _intakeValves.value != intakeValves ||
                _exhaustValves.value != exhaustValves ||
                _measurements.isEmpty()

        if (needsReset) {
            _cylinders = cylinders
            _intakeValves = intakeValves
            _exhaustValves = exhaustValves

            resetMeasurements()
        }
    }

    override fun setClearanceLimit(
        intakeMin: Float,
        intakeMax: Float,
        exhaustMin: Float,
        exhaustMax: Float
    ) {
        _specification = ValveSpecification(intakeMin, intakeMax, exhaustMin, exhaustMax)
    }

    override fun updateMeasuredValue(valveNumber: Int, clearance: Float, shim: Float) {
        _measurements.firstOrNull { it.valveNumber == valveNumber }?.let { measurement ->
            measurement.clearance = clearance
            measurement.shim.size = shim
        }
    }

    override fun getAdjustedValves(): List<Instruction> {
        val calculator = ClearanceCalculator()
        val instructions = calculator.calculateSolutions(_measurements, _specification)

        return instructions
    }

    private fun resetMeasurements() {
        val intakeCount = cylinders * intakeValves
        val exhaustCount = cylinders * exhaustValves

        _measurements = List(intakeCount + exhaustCount) { index ->
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