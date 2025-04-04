package by.jadjer.valveclearance.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import by.jadjer.valveclearance.repository.ValveClearanceRepository
import by.jadjer.valveclearance.ui.viewmodel.MeasurementsViewModel
import by.jadjer.valveclearance.ui.viewmodel.MeasurementsViewModelFactory

@Composable
fun MeasurementsScreen(
    repository: ValveClearanceRepository = ValveClearanceRepository(),
    onNext: () -> Unit
) {
    val viewModel: MeasurementsViewModel = viewModel(
        factory = MeasurementsViewModelFactory(repository),
    )

    val measurements by remember { derivedStateOf { viewModel.measurements } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Measured Clearances (mm)", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.weight(1f))

        LazyColumn {
            items(measurements) { measurement ->
                TwoFloatInputsInRow(
                    label1 = "Valve ${measurement.valveNumber}",
                    value1 = measurement.clearance,
                    label2 = "Shim",
                    value2 = measurement.shim.size,
                    onValueChange = { clearance, shim ->
                        viewModel.updateMeasuredValue(measurement.valveNumber, clearance, shim)
                    },
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = onNext, modifier = Modifier.fillMaxWidth(), enabled = viewModel.isValid()) {
            Text("Next")
        }
    }
}

@Composable
fun TwoFloatInputsInRow(
    label1: String,
    value1: Float,
    label2: String,
    value2: Float,
    onValueChange: (Float, Float) -> Unit,
    modifier: Modifier = Modifier
) {
    var textValue1 by remember { mutableStateOf(value1.toString()) }
    var textValue2 by remember { mutableStateOf(value2.toString()) }

    val floatValue1 = textValue1.toFloatOrNull()
    val floatValue2 = textValue2.toFloatOrNull()
    val bothValid = floatValue1 != null && floatValue2 != null

    LaunchedEffect(floatValue1, floatValue2) {
        if (bothValid) {
            onValueChange(floatValue1, floatValue2)
        }
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FloatInput(
            label = label1,
            value = textValue1,
            onValueChange = { textValue1 = it },
            modifier = Modifier.weight(1f),
            isError = textValue1.isNotEmpty() && floatValue1 == null
        )

        FloatInput(
            label = label2,
            value = textValue2,
            onValueChange = { textValue2 = it },
            modifier = Modifier.weight(1f),
            isError = textValue2.isNotEmpty() && floatValue2 == null
        )
    }
}

@Composable
fun FloatInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false
) {
    Column(modifier = modifier.padding(vertical = 8.dp)) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            isError = isError
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MeasurementsScreenPreview() {
    MeasurementsScreen {}
}
