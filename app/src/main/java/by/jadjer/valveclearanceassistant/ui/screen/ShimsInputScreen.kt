package by.jadjer.valveclearanceassistant.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import by.jadjer.valveclearanceassistant.App
import by.jadjer.valveclearanceassistant.ui.viewmodel.ShimsInputViewModel
import by.jadjer.valveclearanceassistant.ui.viewmodel.ShimsInputViewModelFactory

@Composable
fun ShimsInputScreen(
    app: App,
    onNext: () -> Unit
) {
    val viewModel: ShimsInputViewModel = viewModel(factory = ShimsInputViewModelFactory(app.valveClearanceRepository))

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
            .verticalScroll(rememberScrollState())
    ) {
        Text("Current Shim Sizes (mm)", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

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

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { onNext() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Calculate Optimal Configuration")
        }
    }
}
