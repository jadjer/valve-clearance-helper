package by.jadjer.valveclearanceassistant.repository

import by.jadjer.shimcalculator.ClearanceCalculator
import by.jadjer.shimcalculator.models.ValveMeasurement
import by.jadjer.shimcalculator.models.ValveSpecification
import by.jadjer.shimcalculator.models.ValveType
import by.jadjer.valveclearanceassistant.model.domain.ClearanceRange
import by.jadjer.valveclearanceassistant.model.domain.EngineData

class ValveClearanceRepository {

    private val engineData = EngineData(1, 1, 1)
    private val intakeClearance = ClearanceRange(0f, 1f)
    private val exhaustClearance = ClearanceRange(0f, 1f)

    fun getEngineData(): EngineData {
        return engineData
    }

    fun setEngineData(cylinders: Int, intakeValves: Int, exhaustValves: Int) {
        engineData.cylinders = cylinders
        engineData.intakeValves = intakeValves
        engineData.exhaustValves = exhaustValves
    }

    fun setIntakeClearance(intakeClearanceMin: Float, intakeClearanceMax: Float) {
        intakeClearance.clearanceMin = intakeClearanceMin
        intakeClearance.clearanceMax = intakeClearanceMax
    }

    fun setExhaustClearance(exhaustClearanceMin: Float, exhaustClearanceMax: Float) {
        exhaustClearance.clearanceMin = exhaustClearanceMin
        exhaustClearance.clearanceMax = exhaustClearanceMax
    }

    fun addMeasuredValveClearance(clearance: Float) {

    }

    fun addMeasuredShimThickness(thickness: Float) {

    }

    fun solution() {
        // 1. Подготовка данных
        val measurements = listOf(
            ValveMeasurement(1, ValveType.INTAKE, 0.15f, 2.50f),
            ValveMeasurement(2, ValveType.EXHAUST, 0.30f, 2.75f)
        )

        val specs = ValveSpecification(
            intakeMin = 0.20f,
            intakeMax = 0.25f,
            exhaustMin = 0.25f,
            exhaustMax = 0.30f
        )

        val availableShims = listOf(
            Shim(2.45f, 7.0f, isInstalled = true),
            Shim(2.50f, 7.0f),
            Shim(2.55f, 7.0f)
        )

        // 2. Расчет решений
        val calculator = ClearanceCalculator()
        val solutions = calculator.calculateSolutions(measurements, specs, availableShims)

        // 3. Вывод результатов
        solutions.forEachIndexed { index, solution ->
            println("Решение #${index + 1}:")
            println("Новых шайб: ${solution.newShimsCount}")
            println("Перестановок: ${solution.swapsCount}")
            println("Общая стоимость: ${solution.totalCost}")
            println()
        }
    }
}