package by.jadjer.valveclearance.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import by.jadjer.valveclearance.R
import by.jadjer.valveclearance.domain.model.Vehicle
import by.jadjer.valveclearance.domain.repository.VehicleRepository
import by.jadjer.valveclearance.ui.component.AppScaffold
import by.jadjer.valveclearance.ui.viewmodel.SessionDetailsViewModel
import by.jadjer.valveclearance.ui.viewmodel.SessionDetailsViewModelFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Composable
fun SessionDetailsScreen(
    sessionId: Long,
    repository: VehicleRepository,
    onDelete: () -> Unit,
    onBack: () -> Unit
) {
    val viewModel: SessionDetailsViewModel = viewModel(
        factory = SessionDetailsViewModelFactory(repository, sessionId)
    )
    val measurements by viewModel.measurements.collectAsStateWithLifecycle()

    AppScaffold(
        title = stringResource(R.string.session_details),
        onBack = onBack,
        actions = {
            IconButton(onClick = { 
                viewModel.deleteSession { onDelete() }
            }) {
                Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.delete))
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(measurements) { measurement ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.valve_number, measurement.valveNumber), 
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = measurement.valveType.name, 
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), thickness = 0.5.dp)
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = stringResource(R.string.measured_clearances), style = MaterialTheme.typography.labelSmall)
                                Text(text = "${measurement.clearance} mm", style = MaterialTheme.typography.bodyLarge)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = stringResource(R.string.shim), style = MaterialTheme.typography.labelSmall)
                                Text(text = "${measurement.shim.size} mm", style = MaterialTheme.typography.bodyLarge)
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
fun SessionDetailsScreenPreview() {
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
    SessionDetailsScreen(sessionId = 1L, repository = mockRepo, onDelete = {}, onBack = {})
}
