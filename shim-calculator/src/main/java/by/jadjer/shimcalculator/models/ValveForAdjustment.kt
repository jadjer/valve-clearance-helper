package by.jadjer.shimcalculator.models

data class ValveForAdjustment(
    val measurement: ValveMeasurement,
    val specification: ValveSpecification,
    val targetClearance: Float,
) {
    fun getTargetShimSize(): Float {
        return measurement.shim.size + (measurement.clearance - targetClearance)
    }

    fun getMinClearance(): Float {
        return when (measurement.valveType) {
            ValveType.INTAKE -> specification.intakeMin
            ValveType.EXHAUST -> specification.exhaustMin
        }
    }

    fun getMaxClearance(): Float {
        return when (measurement.valveType) {
            ValveType.INTAKE -> specification.intakeMax
            ValveType.EXHAUST -> specification.exhaustMax
        }
    }
}
