package by.jadjer.valveclearance.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import by.jadjer.shimcalculator.models.ActionType
import by.jadjer.shimcalculator.models.Instruction
import by.jadjer.shimcalculator.models.ValveType
import by.jadjer.valveclearance.repository.ValveClearanceRepository
import by.jadjer.valveclearance.ui.viewmodel.ResultsViewModel
import by.jadjer.valveclearance.ui.viewmodel.ResultsViewModelFactory
import by.jadjer.valveclearance.ui.viewmodel.ValveAdjustmentUiState

@Composable
fun ResultsScreen(repository: ValveClearanceRepository = ValveClearanceRepository(), ) {
    val viewModel: ResultsViewModel = viewModel(factory = ResultsViewModelFactory(repository))
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.calculateAdjustments()
    }

    when (val state = uiState) {
        is ValveAdjustmentUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is ValveAdjustmentUiState.Error -> {
            ErrorScreen(message = state.message)
        }

        is ValveAdjustmentUiState.Success -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Service Limits (mm)", style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.weight(1f))

                InstructionList(instructions = state.instructions)
            }
        }
    }
}

@Composable
fun InstructionList(instructions: List<Instruction>) {
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(instructions) { instruction ->
            InstructionItem(instruction = instruction)
        }
    }
}

@Composable
fun InstructionItem(instruction: Instruction) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Клапан ${instruction.valve.measurement.valveNumber}",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = when (instruction.valve.measurement.valveType) {
                    ValveType.INTAKE -> "Тип: Впуск"
                    ValveType.EXHAUST -> "Тип: Выпуск"
                }
            )
            Text("Текущий зазор: ${instruction.valve.measurement.clearance} мм")

            Text("Целевой зазор: ${instruction.valve.targetClearance} мм")
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = when (instruction.action) {
                    ActionType.KEEP -> "Оставить текущую шайбу"
                    ActionType.MOVE -> "Переставить шайбу с клапана ${instruction.newShim.valveNumber}"
                    ActionType.REPLACE -> "Установить новую шайбу"
                },
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Text("Размер шайбы: ${instruction.newShim.size} мм")
        }
    }
}

@Composable
fun ErrorScreen(message: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Error",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = message)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ResultsScreenPreview() {
    ResultsScreen {}
}
