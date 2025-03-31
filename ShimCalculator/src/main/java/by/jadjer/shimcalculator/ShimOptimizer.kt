package by.jadjer.shimcalculator

import by.jadjer.shimcalculator.models.AdjustmentSolution
import by.jadjer.shimcalculator.models.Shim
import by.jadjer.shimcalculator.models.ValveMeasurement
import kotlin.math.abs

class ShimOptimizer {

    fun findBestSolutions(
        requiredChanges: Map<ValveMeasurement, Float>,
        allMeasurements: List<ValveMeasurement>,
        availableShims: List<Shim>
    ): List<AdjustmentSolution> {
        val solutions = mutableListOf<AdjustmentSolution>()

        // 1. Попробовать найти решение с перестановкой существующих шайб
        findSwapSolutions(requiredChanges, allMeasurements, solutions)

        // 2. Решения с минимальным количеством новых шайб
        findMinimalNewShimSolutions(requiredChanges, availableShims, solutions)

        // 3. Полные решения с новыми шайбами
        findCompleteNewShimSolutions(requiredChanges, availableShims, solutions)

        return solutions.sortedBy { it.totalCost }
    }

    private fun findSwapSolutions(
        requiredChanges: Map<ValveMeasurement, Float>,
        allMeasurements: List<ValveMeasurement>,
        solutions: MutableList<AdjustmentSolution>
    ) {
        // Реализация алгоритма поиска перестановок
        // ...
    }

    private fun findMinimalNewShimSolutions(
        requiredChanges: Map<ValveMeasurement, Float>,
        availableShims: List<Shim>,
        solutions: MutableList<AdjustmentSolution>
    ) {
        // Реализация алгоритма с минимальным количеством новых шайб
        // ...
    }

    private fun findCompleteNewShimSolutions(
        requiredChanges: Map<ValveMeasurement, Float>,
        availableShims: List<Shim>,
        solutions: MutableList<AdjustmentSolution>
    ) {
        requiredChanges.forEach { (valve, requiredSize) ->
            val closestShim = findClosestShim(requiredSize, availableShims)
            if (closestShim != null) {
                solutions.add(
                    AdjustmentSolution(
                        id = "new-shim-${valve.valveNumber}",
                        requiredShims = listOf(closestShim),
                        swaps = emptyList(),
                        totalCost = calculateShimCost(closestShim)
                    )
                )
            }
        }
    }

    private fun findClosestShim(
        requiredSize: Float,
        shims: List<Shim>
    ): Shim? {
        return shims.minByOrNull { abs(it.size - requiredSize) }
    }

    private fun calculateShimCost(shim: Shim): Float {
        return if (shim.isInstalled) 0f else shim.size * 10f // Примерная формула стоимости
    }
}
