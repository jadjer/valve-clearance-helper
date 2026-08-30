package by.jadjer.valveclearance.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import by.jadjer.shimcalculator.models.*

@Composable
fun InstructionList(instructions: List<Instruction>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.padding(16.dp)) {
        items(instructions) { instruction ->
            InstructionItem(instruction = instruction)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InstructionListPreview() {
    val mockInstructions = List(3) {
        Instruction(
            valve = ValveForAdjustment(
                measurement = ValveMeasurement(it + 1, ValveType.INTAKE, 0.15f, Shim(it + 1, 2.50f)),
                specification = ValveSpecification(0.10f, 0.20f, 0.20f, 0.30f),
                targetClearance = 0.15f
            ),
            action = ActionType.KEEP,
            newShim = Shim(it + 1, 2.50f),
            newClearance = 0.15f
        )
    }
    InstructionList(instructions = mockInstructions)
}
