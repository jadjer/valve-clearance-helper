package by.jadjer.valveclearance.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import by.jadjer.valveclearance.ui.component.InstructionList
import by.jadjer.valveclearance.ui.viewmodel.ResultsViewModel
import by.jadjer.valveclearance.ui.viewmodel.ResultsViewModelFactory
import by.jadjer.valveclearance.ui.viewmodel.ValveAdjustmentUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Composable
fun ResultsScreen(
    repository: ValveClearanceRepository,
    vehicleRepository: VehicleRepository? = null,
    vehicleId: Long = -1L,
    onFinish: () -> Unit = {},
    onBack: () -> Unit
) {
    val viewModel: ResultsViewModel = viewModel(
        factory = ResultsViewModelFactory(repository, vehicleRepository, vehicleId)
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.calculateAdjustments()
    }

    AppScaffold(
        title = stringResource(R.string.adjustment_instructions),
        onBack = onBack,
        bottomBar = {
            if (uiState is ValveAdjustmentUiState.Success) {
                Button(
                    onClick = { viewModel.saveSession(onFinish) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(stringResource(R.string.save_and_finish))
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (val state = uiState) {
                is ValveAdjustmentUiState.Loading -> {
                    CircularProgressIndicator()
                }

                is ValveAdjustmentUiState.Error -> {
                    ErrorScreen(message = state.message)
                }

                is ValveAdjustmentUiState.Success -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        InstructionList(
                            instructions = state.instructions,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ResultsScreenPreview() {
    val mockRepo = object : ValveClearanceRepository {
        override val cylinders = kotlinx.coroutines.flow.MutableStateFlow(1)
        override val intakeValves = kotlinx.coroutines.flow.MutableStateFlow(1)
        override val exhaustValves = kotlinx.coroutines.flow.MutableStateFlow(1)
        override val measurements = kotlinx.coroutines.flow.MutableStateFlow(emptyList<by.jadjer.shimcalculator.models.ValveMeasurement>())
        override val specification = kotlinx.coroutines.flow.MutableStateFlow(by.jadjer.shimcalculator.models.ValveSpecification(0f, 0f, 0f, 0f))
        override fun setEngineData(cylinders: Int, intakeValves: Int, exhaustValves: Int) {}
        override fun setClearanceLimit(intakeMin: Float, intakeMax: Float, exhaustMin: Float, exhaustMax: Float) {}
        override fun updateMeasuredValue(valveNumber: Int, clearance: Float, shim: Float) {}
        override fun getAdjustedValves(): List<by.jadjer.shimcalculator.models.Instruction> = emptyList()
        override fun reset() {}
    }
    val mockVehicleRepo = object : VehicleRepository {
        override fun getAllVehicles(): Flow<List<by.jadjer.valveclearance.domain.model.Vehicle>> = flowOf(emptyList())
        override suspend fun getVehicleById(id: Long): by.jadjer.valveclearance.domain.model.Vehicle? = null
        override suspend fun addVehicle(vehicle: by.jadjer.valveclearance.domain.model.Vehicle) {}
        override suspend fun updateVehicle(vehicle: by.jadjer.valveclearance.domain.model.Vehicle) {}
        override suspend fun deleteVehicle(vehicle: by.jadjer.valveclearance.domain.model.Vehicle) {}
        override suspend fun saveMeasurementSession(vehicle: by.jadjer.valveclearance.domain.model.Vehicle, measurements: List<by.jadjer.shimcalculator.models.ValveMeasurement>) {}
        override fun getSessionsForVehicle(vehicleId: Long): Flow<List<by.jadjer.valveclearance.domain.repository.MeasurementSession>> = flowOf(emptyList())
        override suspend fun getMeasurementsForSession(sessionId: Long): List<by.jadjer.shimcalculator.models.ValveMeasurement> = emptyList()
        override suspend fun deleteSession(sessionId: Long) {}
    }
    ResultsScreen(repository = mockRepo, vehicleRepository = mockVehicleRepo, onFinish = {}, onBack = {})
}
