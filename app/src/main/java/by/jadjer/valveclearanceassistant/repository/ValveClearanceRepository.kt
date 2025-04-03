package by.jadjer.valveclearanceassistant.repository

import by.jadjer.shimcalculator.ClearanceCalculator
import by.jadjer.shimcalculator.models.Instruction
import by.jadjer.shimcalculator.models.Shim
import by.jadjer.shimcalculator.models.ValveMeasurement
import by.jadjer.shimcalculator.models.ValveSpecification
import by.jadjer.shimcalculator.models.ValveType

class ValveClearanceRepository {

    private var _cylinders: Int = 1
    private var _intakeValves: Int = 1
    private var _exhaustValves: Int = 1
    private var _measurements: List<ValveMeasurement> = emptyList()
    private var _specification: ValveSpecification = ValveSpecification(0f, 0f, 0f, 0f)

    val cylinders: Int get() = _cylinders
    val intakeValves: Int get() = _intakeValves
    val exhaustValves: Int get() = _exhaustValves
    val measurements: List<ValveMeasurement> get() = _measurements
    val specification: ValveSpecification get() = _specification

    fun setEngineData(cylinders: Int, intakeValves: Int, exhaustValves: Int) {
        val needsReset = _cylinders != cylinders ||
                _intakeValves != intakeValves ||
                _exhaustValves != exhaustValves ||
                _measurements.isEmpty()

        if (needsReset) {
            _cylinders = cylinders
            _intakeValves = intakeValves
            _exhaustValves = exhaustValves

            resetMeasurements()
        }
    }

    fun setClearanceLimit(intakeMin: Float, intakeMax: Float, exhaustMin: Float, exhaustMax: Float) {
        _specification = ValveSpecification(intakeMin, intakeMax, exhaustMin, exhaustMax)
    }

    fun updateMeasuredValue(valveNumber: Int, clearance: Float, shim: Float) {
        _measurements.firstOrNull { it.valveNumber == valveNumber }?.let { measurement ->
            measurement.clearance = clearance
            measurement.shim.size = shim
        }
    }

    fun getAdjustedValves(): List<Instruction> {
        val calculator = ClearanceCalculator()
        val instructions = calculator.calculateSolutions(_measurements, _specification)

        return instructions
    }

    private fun resetMeasurements() {
        var counter = 0

        fun createValves(type: ValveType, count: Int) = List(count) {
            ValveMeasurement(
                valveNumber = ++counter,
                valveType = type,
                clearance = 0f,
                shim = Shim(valveNumber = counter, size = 0f)
            )
        }

        _measurements = createValves(ValveType.INTAKE, cylinders * intakeValves) + createValves(ValveType.EXHAUST, cylinders * exhaustValves)
    }
}
