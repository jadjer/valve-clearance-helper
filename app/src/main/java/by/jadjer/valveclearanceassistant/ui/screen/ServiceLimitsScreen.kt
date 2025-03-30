package by.jadjer.valveclearanceassistant.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun ServiceLimitsScreen(
    onNext: (intakeMin: Float, intakeMax: Float, exhaustMin: Float, exhaustMax: Float) -> Unit
) {
    var intakeMin by remember { mutableFloatStateOf(0.15f) }
    var intakeMax by remember { mutableFloatStateOf(0.25f) }
    var exhaustMin by remember { mutableFloatStateOf(0.25f) }
    var exhaustMax by remember { mutableFloatStateOf(0.35f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Service Limits (mm)", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        FloatInput(label = "Intake valve min", value = intakeMin, onValueChange = { intakeMin = it })
        FloatInput(label = "Intake valve max", value = intakeMax, onValueChange = { intakeMax = it })
        FloatInput(label = "Exhaust valve min", value = exhaustMin, onValueChange = { exhaustMin = it })
        FloatInput(label = "Exhaust valve max", value = exhaustMax, onValueChange = { exhaustMax = it })

        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = { onNext(intakeMin, intakeMax, exhaustMin, exhaustMax) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Next")
        }
    }
}

@Composable
fun FloatInput(label: String, value: Float, onValueChange: (Float) -> Unit) {
    var textValue by remember { mutableStateOf(value.toString()) }

    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        OutlinedTextField(
            value = textValue,
            onValueChange = {
                textValue = it
                it.toFloatOrNull()?.let { float -> onValueChange(float) }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
    }
}