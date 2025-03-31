package by.jadjer.shimcalculator.models

enum class ValveType { INTAKE, EXHAUST }

data class ValveSpecification(
    val intakeMin: Float,
    val intakeMax: Float,
    val exhaustMin: Float,
    val exhaustMax: Float
)

data class ValveMeasurement(
    val valveNumber: Int,
    val valveType: ValveType,
    val currentClearance: Float,
    val currentShimSize: Float
)