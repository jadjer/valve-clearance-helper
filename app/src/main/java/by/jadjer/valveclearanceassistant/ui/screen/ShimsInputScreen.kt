package by.jadjer.valveclearanceassistant.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun ShimsInputScreen(
    cylinders: Int,
    intakeValves: Int,
    exhaustValves: Int,
    onNext: (List<Float>, List<Float>) -> Unit
) {
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
            onClick = { onNext(intakeShims, exhaustShims) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Calculate Optimal Configuration")
        }
    }
}
