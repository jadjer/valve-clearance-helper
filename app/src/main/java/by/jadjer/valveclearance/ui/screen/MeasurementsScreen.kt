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
import by.jadjer.valveclearance.data.repository.ValveClearanceRepositoryImpl
import by.jadjer.valveclearance.ui.component.TwoFloatInputsInRow
import by.jadjer.valveclearance.ui.viewmodel.MeasurementsViewModel
import by.jadjer.valveclearance.ui.viewmodel.MeasurementsViewModelFactory

@Composable
fun MeasurementsScreen(
    repository: ValveClearanceRepositoryImpl = ValveClearanceRepositoryImpl(),
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

@Preview(showBackground = true)
@Composable
fun MeasurementsScreenPreview() {
    MeasurementsScreen {}
}
