package by.jadjer.shimcalculator.models

data class ValveMeasurement(
    val valveNumber: Int,
    val valveType: ValveType,
    var clearance: Float,
    val shim: Shim,
)
