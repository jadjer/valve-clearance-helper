package by.jadjer.valveclearanceassistant.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import by.jadjer.valveclearanceassistant.repository.ValveClearanceRepository
import by.jadjer.valveclearanceassistant.ui.viewmodel.MeasuredClearancesViewModel
import by.jadjer.valveclearanceassistant.ui.viewmodel.MeasuredClearancesViewModelFactory

@Composable
fun MeasuredClearancesScreen(
    repository: ValveClearanceRepository = ValveClearanceRepository(),
    onNext: () -> Unit
) {
    val viewModel: MeasuredClearancesViewModel = viewModel(
        factory = MeasuredClearancesViewModelFactory(repository),
    )

    val cylinders = viewModel.getCylinders()
    val intakeValves = viewModel.getIntakeValves()
    val exhaustValves = viewModel.getExhaustValves()

    val intakeClearances = remember {
        mutableStateListOf<Float>().apply {
            repeat(cylinders * intakeValves) { add(0.0f) }
        }
    }
    val exhaustClearances = remember {
        mutableStateListOf<Float>().apply {
            repeat(cylinders * exhaustValves) { add(0.0f) }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Measured Clearances (mm)", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.weight(1f))

        Text("Intake Valves", style = MaterialTheme.typography.headlineMedium)
        repeat(cylinders) { cylinder ->
            repeat(intakeValves) { valve ->
                FloatInput(
                    label = "Cylinder ${cylinder + 1} Intake Valve ${valve + 1}",
                    value = intakeClearances[cylinder * intakeValves + valve],
                    onValueChange = { intakeClearances[cylinder * intakeValves + valve] = it }
                )
            }
        }

        Text("Exhaust Valves", style = MaterialTheme.typography.headlineMedium)
        repeat(cylinders) { cylinder ->
            repeat(exhaustValves) { valve ->
                FloatInput(
                    label = "Cylinder ${cylinder + 1} Exhaust Valve ${valve + 1}",
                    value = exhaustClearances[cylinder * exhaustValves + valve],
                    onValueChange = { exhaustClearances[cylinder * exhaustValves + valve] = it }
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = onNext, modifier = Modifier.fillMaxWidth()) {
            Text("Next")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MeasuredClearancesScreenPreview() {
    MeasuredClearancesScreen {}
}
