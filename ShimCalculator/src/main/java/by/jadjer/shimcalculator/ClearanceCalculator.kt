package by.jadjer.shimcalculator

import by.jadjer.shimcalculator.exceptions.ClearanceException
import by.jadjer.shimcalculator.models.AdjustmentSolution
import by.jadjer.shimcalculator.models.Shim
import by.jadjer.shimcalculator.models.ValveMeasurement
import by.jadjer.shimcalculator.models.ValveSpecification
import by.jadjer.shimcalculator.models.ValveType

class ClearanceCalculator {

    fun calculateSolutions(
        measurements: List<ValveMeasurement>,
        specs: ValveSpecification,
        availableShims: List<Shim>
    ): List<AdjustmentSolution> {
        validateInput(measurements, specs, availableShims)

        val valvesToAdjust = filterValvesNeedingAdjustment(measurements, specs)
        val requiredChanges = calculateRequiredShimChanges(valvesToAdjust, specs)

        return findOptimalSolutions(
            requiredChanges = requiredChanges,
            allMeasurements = measurements,
            availableShims = availableShims
        )
    }

    private fun validateInput(
        measurements: List<ValveMeasurement>,
        specs: ValveSpecification,
        shims: List<Shim>
    ) {
        if (measurements.isEmpty()) throw ClearanceException("No measurements provided")
        if (shims.isEmpty()) throw ClearanceException("No shims available")
    }

    private fun filterValvesNeedingAdjustment(
        measurements: List<ValveMeasurement>,
        specs: ValveSpecification
    ): List<ValveMeasurement> {
        return measurements.filter { measurement ->
            val (min, max) = when (measurement.valveType) {
                ValveType.INTAKE -> specs.intakeMin to specs.intakeMax
                ValveType.EXHAUST -> specs.exhaustMin to specs.exhaustMax
            }
            measurement.currentClearance < min || measurement.currentClearance > max
        }
    }

    private fun calculateRequiredShimChanges(
        valves: List<ValveMeasurement>,
        specs: ValveSpecification
    ): Map<ValveMeasurement, Float> {
        return valves.associateWith { valve ->
            val target = when (valve.valveType) {
                ValveType.INTAKE -> (specs.intakeMin + specs.intakeMax) / 2f
                ValveType.EXHAUST -> (specs.exhaustMin + specs.exhaustMax) / 2f
            }
            calculateNewShimSize(
                currentClearance = valve.currentClearance,
                targetClearance = target,
                currentShimSize = valve.currentShimSize
            )
        }
    }

    private fun calculateNewShimSize(
        currentClearance: Float,
        targetClearance: Float,
        currentShimSize: Float
    ): Float {
        return currentShimSize + (currentClearance - targetClearance)
    }

    private fun findOptimalSolutions(
        requiredChanges: Map<ValveMeasurement, Float>,
        allMeasurements: List<ValveMeasurement>,
        availableShims: List<Shim>
    ): List<AdjustmentSolution> {
        val optimizer = ShimOptimizer()
        return optimizer.findBestSolutions(requiredChanges, allMeasurements, availableShims)
    }
}
