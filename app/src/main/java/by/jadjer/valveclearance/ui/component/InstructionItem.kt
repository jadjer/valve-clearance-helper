package by.jadjer.valveclearance.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
fun InstructionItem(instruction: Instruction) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Клапан ${instruction.valve.measurement.valveNumber}",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = when (instruction.valve.measurement.valveType) {
                        ValveType.INTAKE -> "Впуск"
                        ValveType.EXHAUST -> "Выпуск"
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Text("Текущий зазор: ${instruction.valve.measurement.clearance} мм")
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                Text("Целевой зазор: ${instruction.valve.targetClearance} мм")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Text("Размер шайбы: ${instruction.newShim.size} мм")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = when (instruction.action) {
                        ActionType.KEEP -> "Оставить текущую шайбу"
                        ActionType.MOVE -> "Переставить шайбу с клапана ${instruction.newShim.valveNumber}"
                        ActionType.REPLACE -> "Установить новую шайбу"
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InstructionItemPreview() {
    InstructionItem(
        instruction = Instruction(
            valve = ValveForAdjustment(
                measurement = ValveMeasurement(
                    valveNumber = 1,
                    valveType = ValveType.INTAKE,
                    0.01f,
                    shim = Shim(valveNumber = 1, size = 0.12f),
                ),
                specification = ValveSpecification(
                    intakeMin = 0.12f,
                    intakeMax = 0.15f,
                    exhaustMin = 0.22f,
                    exhaustMax = 0.25f
                ),
                targetClearance = 0.12f
            ),
            action = ActionType.KEEP,
            newShim = Shim(valveNumber = 2, size = 0.19f),
            newClearance = 0.24f,
        )
    )
}
