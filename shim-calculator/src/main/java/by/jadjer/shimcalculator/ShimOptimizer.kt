package by.jadjer.shimcalculator

import by.jadjer.shimcalculator.models.ActionType
import by.jadjer.shimcalculator.models.Instruction
import by.jadjer.shimcalculator.models.Shim
import by.jadjer.shimcalculator.models.ValveForAdjustment
import kotlin.math.abs
import kotlin.math.roundToInt

class ShimOptimizer {

    fun adjustValves(valves: List<ValveForAdjustment>): List<Instruction> {
        val instructions = mutableListOf<Instruction>()

        val availableShims = valves.associate {
            it.measurement.valveNumber to it.measurement.shim
        }.toMutableMap()

        valves.forEach { valve ->
            val currentValveNumber = valve.measurement.valveNumber
            val currentClearance = valve.measurement.clearance
            val currentShim = valve.measurement.shim
            val targetShimSize = valve.getTargetShimSize().roundTo0025()

            // 1. Try to KEEP the current shim if it's within limits
            if (currentClearance in valve.getMinClearance()..valve.getMaxClearance()) {
                // If it's still available (not taken by another valve)
                if (availableShims.containsKey(currentValveNumber)) {
                    instructions.add(
                        Instruction(
                            valve = valve,
                            action = ActionType.KEEP,
                            newShim = currentShim,
                            newClearance = currentClearance
                        )
                    )
                    availableShims.remove(currentValveNumber)
                    return@forEach
                }
            }

            // 2. Try to MOVE a shim from another valve that gets us closest to target and within limits
            val bestMoveEntry = availableShims
                .filter { (_, shim) ->
                    val newClearance = calculateNewClearance(currentClearance, currentShim.size, shim.size)
                    newClearance in valve.getMinClearance()..valve.getMaxClearance()
                }
                .minByOrNull { (_, shim) -> abs(shim.size - targetShimSize) }

            if (bestMoveEntry != null) {
                val (sourceValveNumber, bestShim) = bestMoveEntry
                instructions.add(
                    Instruction(
                        valve = valve,
                        action = if (sourceValveNumber == currentValveNumber) ActionType.KEEP else ActionType.MOVE,
                        newShim = bestShim,
                        newClearance = calculateNewClearance(currentClearance, currentShim.size, bestShim.size)
                    )
                )
                availableShims.remove(sourceValveNumber)
                return@forEach
            }

            // 3. If no suitable shim found, REPLACE with the ideal one
            val resultClearanceAfterReplace = calculateNewClearance(currentClearance, currentShim.size, targetShimSize)
            instructions.add(
                Instruction(
                    valve = valve,
                    action = ActionType.REPLACE,
                    newShim = Shim(currentValveNumber, targetShimSize),
                    newClearance = resultClearanceAfterReplace
                )
            )
            // If we decided to REPLACE, we should still "use up" the current shim if it was available, 
            // but actually we don't HAVE to. The old shim is now a spare.
        }

        return instructions
    }

    private fun Float.roundTo0025(): Float {
        return (this * 40).roundToInt() / 40f
    }

    private fun calculateNewClearance(currentClearance: Float, currentShimSize: Float, newShimSize: Float): Float {
        return currentClearance + (currentShimSize - newShimSize)
    }
}
