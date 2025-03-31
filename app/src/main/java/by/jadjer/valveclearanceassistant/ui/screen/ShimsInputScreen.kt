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
import by.jadjer.valveclearanceassistant.ui.viewmodel.ShimsInputViewModel
import by.jadjer.valveclearanceassistant.ui.viewmodel.ShimsInputViewModelFactory

@Composable
fun ShimsInputScreen(
    repository: ValveClearanceRepository = ValveClearanceRepository(),
    onNext: () -> Unit
) {
    val viewModel: ShimsInputViewModel = viewModel(factory = ShimsInputViewModelFactory(repository))

    val cylinders = viewModel.getCylinders()
    val intakeValves = viewModel.getIntakeValves()
    val exhaustValves = viewModel.getExhaustValves()

    val intakeShims = remember {
        mutableStateListOf<Float>().apply {
            repeat(cylinders * intakeValves) { add(0.0f) }
        }
    }
    val exhaustShims = remember {
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
        Text(text = "Current Shim Sizes (mm)", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.weight(1f))

        Text("Intake Valves Shims", style = MaterialTheme.typography.headlineMedium)
        repeat(cylinders) { cylinder ->
            repeat(intakeValves) { valve ->
                FloatInput(
                    label = "Cylinder ${cylinder + 1} Intake Valve ${valve + 1}",
                    value = intakeShims[cylinder * intakeValves + valve],
                    onValueChange = { intakeShims[cylinder * intakeValves + valve] = it }
                )
            }
        }

        Text("Exhaust Valves Shims", style = MaterialTheme.typography.headlineMedium)
        repeat(cylinders) { cylinder ->
            repeat(exhaustValves) { valve ->
                FloatInput(
                    label = "Cylinder ${cylinder + 1} Exhaust Valve ${valve + 1}",
                    value = exhaustShims[cylinder * exhaustValves + valve],
                    onValueChange = { exhaustShims[cylinder * exhaustValves + valve] = it }
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = onNext, modifier = Modifier.fillMaxWidth()) {
            Text("Calculate Optimal Configuration")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShimsInputScreenPreview() {
    ShimsInputScreen {}
}
