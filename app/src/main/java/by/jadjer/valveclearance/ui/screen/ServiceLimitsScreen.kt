package by.jadjer.valveclearance.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import by.jadjer.valveclearance.R
import by.jadjer.valveclearance.domain.repository.ValveClearanceRepository
import by.jadjer.valveclearance.ui.component.AppScaffold
import by.jadjer.valveclearance.ui.component.TwoFloatInputsInRow
import by.jadjer.valveclearance.ui.viewmodel.ServiceLimitsViewModel
import by.jadjer.valveclearance.ui.viewmodel.ServiceLimitsViewModelFactory

@Composable
fun ServiceLimitsScreen(
    repository: ValveClearanceRepository,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    val viewModel: ServiceLimitsViewModel = viewModel(
        factory = ServiceLimitsViewModelFactory(repository),
    )

    val intakeMin by viewModel.intakeClearanceMin.collectAsStateWithLifecycle()
    val intakeMax by viewModel.intakeClearanceMax.collectAsStateWithLifecycle()
    val exhaustMin by viewModel.exhaustClearanceMin.collectAsStateWithLifecycle()
    val exhaustMax by viewModel.exhaustClearanceMax.collectAsStateWithLifecycle()

    AppScaffold(
        title = stringResource(R.string.service_limits),
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
            Column {
                TwoFloatInputsInRow(
                    label1 = stringResource(R.string.intake_min),
                    value1 = intakeMin,
                    label2 = stringResource(R.string.intake_max),
                    value2 = intakeMax,
                    onValueChange = { min, max ->
                        viewModel.setIntakeClearanceMin(min)
                        viewModel.setIntakeClearanceMax(max)
                    },
                )
                TwoFloatInputsInRow(
                    label1 = stringResource(R.string.exhaust_min),
                    value1 = exhaustMin,
                    label2 = stringResource(R.string.exhaust_max),
                    value2 = exhaustMax,
                    onValueChange = { min, max ->
                        viewModel.setExhaustClearanceMin(min)
                        viewModel.setExhaustClearanceMax(max)
                    },
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
            
            Button(
                onClick = {
                    if (viewModel.saveData()) {
                        onNext()
                    }
                },
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
fun ServiceLimitsScreenPreview() {
    val mockRepo = object : ValveClearanceRepository {
        override val cylinders = kotlinx.coroutines.flow.MutableStateFlow(4)
        override val intakeValves = kotlinx.coroutines.flow.MutableStateFlow(2)
        override val exhaustValves = kotlinx.coroutines.flow.MutableStateFlow(2)
        override val measurements = kotlinx.coroutines.flow.MutableStateFlow(emptyList<by.jadjer.shimcalculator.models.ValveMeasurement>())
        override val specification = kotlinx.coroutines.flow.MutableStateFlow(by.jadjer.shimcalculator.models.ValveSpecification(0.1f, 0.2f, 0.2f, 0.3f))
        override fun setEngineData(cylinders: Int, intakeValves: Int, exhaustValves: Int) {}
        override fun setClearanceLimit(intakeMin: Float, intakeMax: Float, exhaustMin: Float, exhaustMax: Float) {}
        override fun updateMeasuredValue(valveNumber: Int, clearance: Float, shim: Float) {}
        override fun getAdjustedValves(): List<by.jadjer.shimcalculator.models.Instruction> = emptyList()
        override fun reset() {}
    }
    ServiceLimitsScreen(repository = mockRepo, onNext = {}, onBack = {})
}
