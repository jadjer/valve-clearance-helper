package by.jadjer.valveclearanceassistant.repository

import by.jadjer.shimcalculator.ClearanceCalculator
import by.jadjer.shimcalculator.models.Instruction
import by.jadjer.shimcalculator.models.Shim
import by.jadjer.shimcalculator.models.ValveMeasurement
import by.jadjer.shimcalculator.models.ValveSpecification
import by.jadjer.shimcalculator.models.ValveType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class ValveClearanceRepository {

    private val _measurements = MutableStateFlow<List<ValveMeasurement>>(emptyList())
    val measurements: StateFlow<List<ValveMeasurement>> = _measurements

    private var specification = ValveSpecification(0f, 0f, 0f, 0f)

    fun setEngineData(cylinders: Int, intakeValves: Int, exhaustValves: Int) {
        println("Cylinders: $cylinders, in: $intakeValves, ex: $exhaustValves")

        var itemIndex = 0

        val initialIntakeValves = List(cylinders * intakeValves) { index ->
            ValveMeasurement(
                valveNumber = ++itemIndex,
                valveType = ValveType.INTAKE,
                clearance = 0f,
                shim = Shim(
                    valveNumber = itemIndex,
                    size = 0f,
                )
            )
        }

        val initialExhaustValves = List(cylinders * exhaustValves) { index ->
            ValveMeasurement(
                valveNumber = ++itemIndex,
                valveType = ValveType.EXHAUST,
                clearance = 0f,
                shim = Shim(
                    valveNumber = itemIndex,
                    size = 0f,
                )
            )
        }

        _measurements.update {
            initialIntakeValves + initialExhaustValves
        }

        println("Meas size: ${_measurements.value.size}")
    }

    fun setClearanceLimit(intakeMin: Float, intakeMax: Float, exhaustMin: Float, exhaustMax: Float) {
        specification = ValveSpecification(
            intakeMin = intakeMin,
            intakeMax = intakeMax,
            exhaustMin = exhaustMin,
            exhaustMax = exhaustMax,
        )
        println("in: $intakeMin..$intakeMax, ex: $exhaustMin..$exhaustMax")
    }

    fun addMeasuredValve(valveIndex: Int, valveType: ValveType, clearance: Float, shim: Float) {
        _measurements.update { currentList ->
            currentList + ValveMeasurement(
                valveNumber = valveIndex,
                valveType = valveType,
                clearance = clearance,
                shim = Shim(
                    valveNumber = valveIndex,
                    size = shim,
                )
            )
        }
    }

    fun getAdjustedValves(): List<Instruction> {
        val calculator = ClearanceCalculator()
        val instructions = calculator.calculateSolutions(measurements.value, specification)

        return instructions
    }
}