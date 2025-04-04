package by.jadjer.shimcalculator.models

enum class ActionType { KEEP, MOVE, REPLACE }

data class Instruction(
    val valve: ValveForAdjustment,
    val action: ActionType,
    val newShim: Shim,
    val newClearance: Float,
)
