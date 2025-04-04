package by.jadjer.shimcalculator.models

enum class ValveType { INTAKE, EXHAUST }

data class ValveMeasurement(
    val valveNumber: Int,
    val valveType: ValveType,
    var clearance: Float,
    val shim: Shim,
)
