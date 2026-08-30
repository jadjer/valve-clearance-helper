package by.jadjer.valveclearance.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import by.jadjer.valveclearance.R
import by.jadjer.valveclearance.domain.repository.ValveClearanceRepository
import by.jadjer.valveclearance.ui.component.AppScaffold
import by.jadjer.valveclearance.ui.component.TwoFloatInputsInRow
import by.jadjer.valveclearance.ui.viewmodel.MeasurementsViewModel
import by.jadjer.valveclearance.ui.viewmodel.MeasurementsViewModelFactory

@Composable
fun MeasurementsScreen(
    repository: ValveClearanceRepository,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    val viewModel: MeasurementsViewModel = viewModel(
        factory = MeasurementsViewModelFactory(repository),
    )

    val measurements by remember { derivedStateOf { viewModel.measurements } }

    AppScaffold(
        title = stringResource(R.string.measured_clearances),
        onBack = onBack
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f, fill = false),
                verticalArrangement = Arrangement.Center
            ) {
                items(measurements) { measurement ->
                    TwoFloatInputsInRow(
                        label1 = stringResource(R.string.valve_number, measurement.valveNumber),
                        value1 = measurement.clearance,
                        label2 = stringResource(R.string.shim),
                        value2 = measurement.shim.size,
                        onValueChange = { clearance, shim ->
                            viewModel.updateMeasuredValue(measurement.valveNumber, clearance, shim)
                        },
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onNext, 
                modifier = Modifier.fillMaxWidth(), 
                enabled = viewModel.isValid()
            ) {
                Text(stringResource(R.string.next))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MeasurementsScreenPreview() {
    val mockRepo = object : ValveClearanceRepository {
        override val cylinders = kotlinx.coroutines.flow.MutableStateFlow(2)
        override val intakeValves = kotlinx.coroutines.flow.MutableStateFlow(2)
        override val exhaustValves = kotlinx.coroutines.flow.MutableStateFlow(2)
        override val measurements = kotlinx.coroutines.flow.MutableStateFlow(List(4) { 
            by.jadjer.shimcalculator.models.ValveMeasurement(
                it + 1, 
                if (it < 2) by.jadjer.shimcalculator.models.ValveType.INTAKE else by.jadjer.shimcalculator.models.ValveType.EXHAUST,
                0.15f,
                by.jadjer.shimcalculator.models.Shim(it + 1, 2.50f)
            )
        })
        override val specification = kotlinx.coroutines.flow.MutableStateFlow(by.jadjer.shimcalculator.models.ValveSpecification(0.1f, 0.2f, 0.2f, 0.3f))
        override fun setEngineData(cylinders: Int, intakeValves: Int, exhaustValves: Int) {}
        override fun setClearanceLimit(intakeMin: Float, intakeMax: Float, exhaustMin: Float, exhaustMax: Float) {}
        override fun updateMeasuredValue(valveNumber: Int, clearance: Float, shim: Float) {}
        override fun getAdjustedValves(): List<by.jadjer.shimcalculator.models.Instruction> = emptyList()
        override fun reset() {}
    }
    MeasurementsScreen(repository = mockRepo, onNext = {}, onBack = {})
}
