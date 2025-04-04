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

        val availableShims: MutableMap<Int, Shim> = valves.associate {
            it.measurement.valveNumber to it.measurement.shim
        }.toMutableMap()

        valves.forEach { valve ->
            val currentValveNumber = valve.measurement.valveNumber
            val currentClearance = valve.measurement.clearance
            val currentShim = valve.measurement.shim
            val targetShimSize = valve.getTargetShimSize().roundTo0025()

            val (bestSourceValve, bestShim) = availableShims
                .minBy { (_, shim) -> abs(shim.size - targetShimSize) }

            val resultClearance = calculateNewClearance(currentClearance, currentShim.size, bestShim.size)

            if (resultClearance !in valve.getMinClearance()..valve.getMaxClearance()) {
                instructions.add(
                    Instruction(
                        valve = valve,
                        action = ActionType.REPLACE,
                        newShim = Shim(currentValveNumber, targetShimSize),
                        newClearance = calculateNewClearance(currentClearance, currentShim.size, targetShimSize)
                    )
                )
                return@forEach
            }

            when {
                (bestSourceValve == currentValveNumber) -> {
                    instructions.add(
                        Instruction(
                            valve = valve,
                            action = ActionType.KEEP,
                            newShim = currentShim,
                            newClearance = calculateNewClearance(currentClearance, currentShim.size, currentShim.size)
                        )
                    )
                    availableShims.remove(bestSourceValve)
                }

                else -> {
                    instructions.add(
                        Instruction(
                            valve = valve,
                            action = ActionType.MOVE,
                            newShim = bestShim,
                            newClearance = calculateNewClearance(currentClearance, currentShim.size, bestShim.size)
                        )
                    )
                    availableShims.remove(bestSourceValve)
                }
            }
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
