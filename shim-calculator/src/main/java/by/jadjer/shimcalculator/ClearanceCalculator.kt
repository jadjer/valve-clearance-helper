package by.jadjer.shimcalculator

import by.jadjer.shimcalculator.exceptions.ClearanceException
import by.jadjer.shimcalculator.models.Instruction
import by.jadjer.shimcalculator.models.ValveForAdjustment
import by.jadjer.shimcalculator.models.ValveMeasurement
import by.jadjer.shimcalculator.models.ValveSpecification
import by.jadjer.shimcalculator.models.ValveType

class ClearanceCalculator {

    fun calculateSolutions(valveMeasurements: List<ValveMeasurement>, valveSpecification: ValveSpecification): List<Instruction> {
        validateInput(valveMeasurements, valveSpecification)

        val valves: List<ValveForAdjustment> = getValvesForAdjustment(valveMeasurements, valveSpecification)

        return findOptimalSolutions(valves)
    }

    private fun validateInput(valveMeasurements: List<ValveMeasurement>, valveSpecification: ValveSpecification) {
        if (valveMeasurements.isEmpty()) {
            throw ClearanceException("No measurements provided")
        }

        valveMeasurements.forEach { measurement ->
            if (measurement.clearance <= 0) throw ClearanceException("Wrong clearance for valve ${measurement.valveNumber}")
            if (measurement.shim.size <= 0) throw ClearanceException("Wrong shim size for valve ${measurement.valveNumber}")
        }

        with(valveSpecification) {
            if (intakeMin <= 0 || intakeMax <= 0 || intakeMin > intakeMax) {
                throw ClearanceException("Wrong intake specification")
            }
            if (exhaustMin <= 0 || exhaustMax <= 0 || exhaustMin > exhaustMax) {
                throw ClearanceException("Wrong exhaust specification")
            }
        }
    }

    private fun getValvesForAdjustment(valveMeasurements: List<ValveMeasurement>, valveSpecification: ValveSpecification): List<ValveForAdjustment> {
        return valveMeasurements.map { valveMeasurement ->
            val valveTargetClearance = when (valveMeasurement.valveType) {
                ValveType.INTAKE -> (valveSpecification.intakeMin + valveSpecification.intakeMax) / 2f
                ValveType.EXHAUST -> (valveSpecification.exhaustMin + valveSpecification.exhaustMax) / 2f
            }

            ValveForAdjustment(
                measurement = valveMeasurement,
                specification = valveSpecification,
                targetClearance = valveTargetClearance,
            )
        }
    }

    private fun findOptimalSolutions(valves: List<ValveForAdjustment>): List<Instruction> {
        val optimizer = ShimOptimizer()
        return optimizer.adjustValves(valves)
    }
}
