package by.jadjer.valveclearance.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import by.jadjer.valveclearance.R
import by.jadjer.valveclearance.domain.repository.ValveClearanceRepository
import by.jadjer.valveclearance.domain.repository.VehicleRepository
import by.jadjer.valveclearance.ui.component.AppScaffold
import by.jadjer.valveclearance.ui.component.NumberInput
import by.jadjer.valveclearance.ui.viewmodel.EngineParamsViewModel
import by.jadjer.valveclearance.ui.viewmodel.EngineParamsViewModelFactory

@Composable
fun EngineParamsScreen(
    repository: ValveClearanceRepository,
    vehicleRepository: VehicleRepository? = null,
    vehicleId: Long = -1L,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    val viewModel: EngineParamsViewModel = viewModel(
        factory = EngineParamsViewModelFactory(repository, vehicleRepository, vehicleId),
    )

    val cylinders by viewModel.cylinders.collectAsStateWithLifecycle()
    val intakeValves by viewModel.intakeValves.collectAsStateWithLifecycle()
    val exhaustValves by viewModel.exhaustValves.collectAsStateWithLifecycle()

    LaunchedEffect(vehicleId) {
        if (vehicleId != -1L) {
            onNext()
        }
    }

    AppScaffold(
        title = stringResource(R.string.engine_params),
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
                NumberInput(label = stringResource(R.string.num_cylinders), value = cylinders, onValueChange = { viewModel.setCylinders(it) }, range = 1..12)
                NumberInput(label = stringResource(R.string.intake_valves_per_cyl), value = intakeValves, onValueChange = { viewModel.setIntakeValves(it) }, range = 1..4)
                NumberInput(label = stringResource(R.string.exhaust_valves_per_cyl), value = exhaustValves, onValueChange = { viewModel.setExhaustValves(it) }, range = 1..4)
            }

            Spacer(modifier = Modifier.height(32.dp))
            
            Button(onClick = {
                if (viewModel.saveData()) {
                    onNext()
                }
            }, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.next))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EngineParamsScreenPreview() {
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
    EngineParamsScreen(repository = mockRepo, onNext = {}, onBack = {})
}
