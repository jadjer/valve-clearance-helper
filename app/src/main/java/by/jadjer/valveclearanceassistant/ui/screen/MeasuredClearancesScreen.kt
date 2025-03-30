package by.jadjer.valveclearanceassistant.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MeasuredClearancesScreen(
    cylinders: Int,
    intakeValves: Int,
    exhaustValves: Int,
    onNext: (List<Float>, List<Float>) -> Unit
) {
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
            .verticalScroll(rememberScrollState())
    ) {
        Text("Measured Clearances (mm)", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

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

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { onNext(intakeClearances, exhaustClearances) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Next")
        }
    }
}