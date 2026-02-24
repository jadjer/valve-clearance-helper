package by.jadjer.valveclearance.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import by.jadjer.shimcalculator.models.ActionType
import by.jadjer.shimcalculator.models.Instruction
import by.jadjer.shimcalculator.models.Shim
import by.jadjer.shimcalculator.models.ValveForAdjustment
import by.jadjer.shimcalculator.models.ValveMeasurement
import by.jadjer.shimcalculator.models.ValveSpecification
import by.jadjer.shimcalculator.models.ValveType

@Composable
fun InstructionList(instructions: List<Instruction>) {
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(instructions) { instruction ->
            InstructionItem(instruction = instruction)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InstructionListPreview() {
    InstructionList(
        listOf(
            Instruction(
                valve = ValveForAdjustment(
                    measurement = ValveMeasurement(
                        valveNumber = 1,
                        valveType = ValveType.INTAKE,
                        0.01f,
                        shim = Shim(1, size = 0.12f)
                    ),
                    specification = ValveSpecification(
                        0.12f,
                        0.15f,
                        0.22f,
                        0.25f
                    ),
                    targetClearance = 0.12f
                ),
                action = ActionType.KEEP,
                newShim = Shim(valveNumber = 2, size = 0.19f),
                newClearance = 0.24f,
            ),
            Instruction(
                valve = ValveForAdjustment(
                    measurement = ValveMeasurement(
                        valveNumber = 1,
                        valveType = ValveType.INTAKE,
                        0.01f,
                        shim = Shim(1, size = 0.12f)
                    ),
                    specification = ValveSpecification(
                        0.12f,
                        0.15f,
                        0.22f,
                        0.25f
                    ),
                    targetClearance = 0.12f
                ),
                action = ActionType.MOVE,
                newShim = Shim(valveNumber = 2, size = 0.19f),
                newClearance = 0.24f,
            ),
            Instruction(
                valve = ValveForAdjustment(
                    measurement = ValveMeasurement(
                        valveNumber = 1,
                        valveType = ValveType.INTAKE,
                        0.01f,
                        shim = Shim(1, size = 0.12f)
                    ),
                    specification = ValveSpecification(
                        0.12f,
                        0.15f,
                        0.22f,
                        0.25f
                    ),
                    targetClearance = 0.12f
                ),
                action = ActionType.REPLACE,
                newShim = Shim(valveNumber = 2, size = 0.19f),
                newClearance = 0.24f,
            ),
        )
    )
}
