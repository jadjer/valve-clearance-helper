package by.jadjer.shimcalculator

import by.jadjer.shimcalculator.models.Shim
import by.jadjer.shimcalculator.models.ValveMeasurement
import by.jadjer.shimcalculator.models.ValveSpecification
import by.jadjer.shimcalculator.models.ValveType
import org.junit.Test

class ClearanceCalculatorTest {
    val measurements = listOf(
        ValveMeasurement(1, ValveType.INTAKE, 0.18f, Shim(1, 1.450f)),
        ValveMeasurement(2, ValveType.INTAKE, 0.13f, Shim(2, 1.500f)),
        ValveMeasurement(3, ValveType.EXHAUST, 0.29f, Shim(3, 2.025f)),
        ValveMeasurement(4, ValveType.EXHAUST, 0.33f, Shim(4, 1.825f)),
        ValveMeasurement(5, ValveType.INTAKE, 0.16f, Shim(5, 1.600f)),
        ValveMeasurement(6, ValveType.INTAKE, 0.14f, Shim(6, 1.450f)),
        ValveMeasurement(7, ValveType.EXHAUST, 0.27f, Shim(7, 1.425f)),
        ValveMeasurement(8, ValveType.EXHAUST, 0.28f, Shim(8, 1.425f)),
    )

    val specifications = ValveSpecification(
        intakeMin = 0.16f - 0.03f,
        intakeMax = 0.16f + 0.03f,
        exhaustMin = 0.31f - 0.03f,
        exhaustMax = 0.31f + 0.03f
    )

    @Test
    fun getInstructions() {
        val calculator = ClearanceCalculator()

        val instructions = calculator.calculateSolutions(measurements, specifications)

        require(instructions.isNotEmpty())

        instructions.forEach { instruction ->
            require(instruction.newClearance in instruction.valve.getMinClearance() .. instruction.valve.getMaxClearance())
        }
    }

}
