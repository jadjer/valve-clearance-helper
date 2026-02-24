package by.jadjer.shimcalculator.models

data class Instruction(
    val valve: ValveForAdjustment,
    val action: ActionType,
    val newShim: Shim,
    val newClearance: Float,
)
