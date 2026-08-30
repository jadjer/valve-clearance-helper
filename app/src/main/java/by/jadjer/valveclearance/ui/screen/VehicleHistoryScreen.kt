package by.jadjer.valveclearance.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
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
import by.jadjer.valveclearance.domain.model.Vehicle
import by.jadjer.valveclearance.domain.repository.ValveClearanceRepository
import by.jadjer.valveclearance.domain.repository.VehicleRepository
import by.jadjer.valveclearance.ui.component.AppScaffold
import by.jadjer.valveclearance.ui.viewmodel.VehicleHistoryViewModel
import by.jadjer.valveclearance.ui.viewmodel.VehicleHistoryViewModelFactory
import by.jadjer.valveclearance.utils.formatTimestamp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Composable
fun VehicleHistoryScreen(
    vehicleId: Long,
    repository: VehicleRepository,
    sessionRepository: ValveClearanceRepository,
    onSessionClick: (Long) -> Unit,
    onNewMeasurement: () -> Unit,
    onEditVehicle: (Long) -> Unit,
    onBack: () -> Unit
) {
    val viewModel: VehicleHistoryViewModel = viewModel(
        factory = VehicleHistoryViewModelFactory(repository, vehicleId)
    )
    val sessions by viewModel.sessions.collectAsStateWithLifecycle()
    val vehicle by viewModel.vehicle.collectAsStateWithLifecycle()

    AppScaffold(
        title = vehicle?.let { "${it.brand} ${it.model}" } ?: stringResource(R.string.measurement_history),
        onBack = onBack,
        actions = {
            IconButton(onClick = { onEditVehicle(vehicleId) }) {
                Icon(Icons.Default.Edit, contentDescription = stringResource(R.string.edit_vehicle))
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                vehicle?.let { v ->
                    sessionRepository.reset()
                    sessionRepository.setEngineData(
                        cylinders = v.engine.cylinders.size,
                        intakeValves = v.engine.cylinders.firstOrNull()?.intakePerCylinder ?: 1,
                        exhaustValves = v.engine.cylinders.firstOrNull()?.exhaustPerCylinder ?: 1
                    )
                    sessionRepository.setClearanceLimit(
                        v.specification.intakeMin,
                        v.specification.intakeMax,
                        v.specification.exhaustMin,
                        v.specification.exhaustMax
                    )
                    onNewMeasurement()
                }
            }) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.new_measurement))
            }
        }
    ) { padding ->
        if (sessions.isEmpty()) {
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(stringResource(R.string.no_measurements))
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(sessions) { session ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onSessionClick(session.id) }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = formatTimestamp(session.timestamp),
                                style = MaterialTheme.typography.titleMedium
                            )
                            if (session.notes.isNotEmpty()) {
                                Text(text = session.notes, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VehicleHistoryScreenPreview() {
    val mockRepo = object : VehicleRepository {
        override fun getAllVehicles(): Flow<List<Vehicle>> = flowOf(emptyList())
        override suspend fun getVehicleById(id: Long): Vehicle? = null
        override suspend fun addVehicle(vehicle: Vehicle) {}
        override suspend fun updateVehicle(vehicle: Vehicle) {}
        override suspend fun deleteVehicle(vehicle: Vehicle) {}
        override suspend fun saveMeasurementSession(vehicle: Vehicle, measurements: List<by.jadjer.shimcalculator.models.ValveMeasurement>) {}
        override fun getSessionsForVehicle(vehicleId: Long): Flow<List<by.jadjer.valveclearance.domain.repository.MeasurementSession>> = flowOf(emptyList())
        override suspend fun getMeasurementsForSession(sessionId: Long): List<by.jadjer.shimcalculator.models.ValveMeasurement> = emptyList()
        override suspend fun deleteSession(sessionId: Long) {}
    }
    val mockSessionRepo = object : ValveClearanceRepository {
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
    VehicleHistoryScreen(
        vehicleId = 1L,
        repository = mockRepo,
        sessionRepository = mockSessionRepo,
        onSessionClick = {},
        onNewMeasurement = {},
        onEditVehicle = {},
        onBack = {}
    )
}
