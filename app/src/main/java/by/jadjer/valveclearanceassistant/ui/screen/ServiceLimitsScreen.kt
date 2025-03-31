package by.jadjer.valveclearanceassistant.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import by.jadjer.valveclearanceassistant.App
import by.jadjer.valveclearanceassistant.ui.viewmodel.ServiceLimitsViewModel
import by.jadjer.valveclearanceassistant.ui.viewmodel.ServiceLimitsViewModelFactory

@Composable
fun ServiceLimitsScreen(
    app: App,
    onNext: () -> Unit,
) {
    val viewModel: ServiceLimitsViewModel = viewModel(factory = ServiceLimitsViewModelFactory(app.valveClearanceRepository))

    val intakeClearanceMin by viewModel.intakeClearanceMin.collectAsState()
    val intakeClearanceMax by viewModel.intakeClearanceMax.collectAsState()
    val exhaustClearanceMin by viewModel.exhaustClearanceMin.collectAsState()
    val exhaustClearanceMax by viewModel.exhaustClearanceMax.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Service Limits (mm)", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        FloatInput(label = "Intake valve min", value = intakeClearanceMin, onValueChange = { viewModel.setIntakeClearanceMin(it) })
        FloatInput(label = "Intake valve max", value = intakeClearanceMax, onValueChange = { viewModel.setIntakeClearanceMax(it) })
        FloatInput(label = "Exhaust valve min", value = exhaustClearanceMin, onValueChange = { viewModel.setExhaustClearanceMin(it) })
        FloatInput(label = "Exhaust valve max", value = exhaustClearanceMax, onValueChange = { viewModel.setExhaustClearanceMax(it) })

        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = { onNext() },
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