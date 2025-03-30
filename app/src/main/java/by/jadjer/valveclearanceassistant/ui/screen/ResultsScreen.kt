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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ResultsScreen(
    intakeShims: List<Float>,
    exhaustShims: List<Float>,
    intakeClearances: List<Float>,
    exhaustClearances: List<Float>,
    intakeMin: Float,
    intakeMax: Float,
    exhaustMin: Float,
    exhaustMax: Float,
    onRestart: () -> Unit
) {
    // Здесь должна быть логика расчета оптимальной перестановки шайб
    val (optimizedIntake, optimizedExhaust, newShimsNeeded) = calculateOptimalShims(
        intakeShims, exhaustShims, intakeClearances, exhaustClearances,
        intakeMin, intakeMax, exhaustMin, exhaustMax
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Optimal Shim Configuration", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        Text("Intake Valves:", style = MaterialTheme.typography.headlineMedium)
        optimizedIntake.forEachIndexed { index, shim ->
            Text("Valve ${index + 1}: ${"%.3f".format(shim)} mm")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Exhaust Valves:", style = MaterialTheme.typography.headlineMedium)
        optimizedExhaust.forEachIndexed { index, shim ->
            Text("Valve ${index + 1}: ${"%.3f".format(shim)} mm")
        }

        if (newShimsNeeded.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("New shims needed:", style = MaterialTheme.typography.headlineMedium, color = Color.Red)
            newShimsNeeded.forEach { shim ->
                Text("${"%.3f".format(shim)} mm", color = Color.Red)
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = onRestart,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Start New Calculation")
        }
    }
}

// Заглушка для функции расчета
fun calculateOptimalShims(
    intakeShims: List<Float>,
    exhaustShims: List<Float>,
    intakeClearances: List<Float>,
    exhaustClearances: List<Float>,
    intakeMin: Float,
    intakeMax: Float,
    exhaustMin: Float,
    exhaustMax: Float
): Triple<List<Float>, List<Float>, List<Float>> {
    // Здесь должна быть реальная логика расчета
    return Triple(intakeShims, exhaustShims, emptyList())
}