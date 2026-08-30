package by.jadjer.shimcalculator.models

data class ValveMeasurement(
    val valveNumber: Int,
    val valveType: ValveType,
    val clearance: Float,
    val shim: Shim,
)
