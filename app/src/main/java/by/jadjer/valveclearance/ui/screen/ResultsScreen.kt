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
import by.jadjer.valveclearance.data.repository.ValveClearanceRepositoryImpl
import by.jadjer.valveclearance.ui.component.InstructionList
import by.jadjer.valveclearance.ui.viewmodel.ResultsViewModel
import by.jadjer.valveclearance.ui.viewmodel.ResultsViewModelFactory
import by.jadjer.valveclearance.ui.viewmodel.ValveAdjustmentUiState

@Composable
fun ResultsScreen(repository: ValveClearanceRepositoryImpl = ValveClearanceRepositoryImpl(), ) {
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

@Preview(showBackground = true)
@Composable
fun ResultsScreenPreview() {
    ResultsScreen()
}
