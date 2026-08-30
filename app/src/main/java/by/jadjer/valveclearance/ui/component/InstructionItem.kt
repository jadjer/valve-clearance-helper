package by.jadjer.valveclearance.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import by.jadjer.shimcalculator.models.*
import by.jadjer.valveclearance.R

@Composable
fun InstructionItem(instruction: Instruction) {
    val color = when (instruction.action) {
        ActionType.KEEP -> Color(0xFF4CAF50)
        ActionType.MOVE -> Color(0xFFFF9800)
        ActionType.REPLACE -> Color(0xFFF44336)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f)),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(color))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.valve_number, instruction.valve.measurement.valveNumber),
                    style = MaterialTheme.typography.titleLarge
                )
                
                Badge(containerColor = color) {
                    Text(text = instruction.action.name, color = Color.White)
                }
            }

            Text(
                text = instruction.valve.measurement.valveType.name,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(R.string.current_clearance, instruction.valve.measurement.clearance),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = stringResource(R.string.target_clearance, instruction.valve.targetClearance),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (instruction.action != ActionType.KEEP) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (instruction.action == ActionType.REPLACE) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = color, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                    Text(
                        text = stringResource(R.string.new_shim_size, instruction.newShim.size),
                        style = MaterialTheme.typography.titleMedium,
                        color = color,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InstructionItemPreview() {
    val mockInstruction = Instruction(
        valve = ValveForAdjustment(
            measurement = ValveMeasurement(1, ValveType.INTAKE, 0.15f, Shim(1, 2.50f)),
            specification = ValveSpecification(0.10f, 0.20f, 0.20f, 0.30f),
            targetClearance = 0.15f
        ),
        action = ActionType.REPLACE,
        newShim = Shim(1, 2.55f),
        newClearance = 0.15f
    )
    InstructionItem(instruction = mockInstruction)
}
