package by.jadjer.valveclearance.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import by.jadjer.valveclearance.ui.component.VehicleList
import by.jadjer.valveclearance.ui.viewmodel.VehicleListViewModel
import by.jadjer.valveclearance.ui.viewmodel.VehicleListViewModelFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Composable
fun VehicleListScreen(
    repository: VehicleRepository,
    onAddVehicle: () -> Unit,
    onVehicleClick: (Long) -> Unit,
    onQuickCalculate: () -> Unit
) {
    val viewModel: VehicleListViewModel = viewModel(
        factory = VehicleListViewModelFactory(repository)
    )
    val vehicles by viewModel.vehicles.collectAsStateWithLifecycle()

    AppScaffold(
        title = stringResource(R.string.my_vehicles),
        actions = {
            TextButton(onClick = onQuickCalculate) {
                Text(stringResource(R.string.start))
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddVehicle) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_vehicle))
            }
        }
    ) { padding ->
        if (vehicles.isEmpty()) {
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.no_vehicles),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            VehicleList(
                vehicles = vehicles,
                onVehicleClick = { vehicle -> onVehicleClick(vehicle.id) },
                modifier = Modifier.padding(padding)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VehicleListScreenPreview() {
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
    VehicleListScreen(repository = mockRepo, onAddVehicle = {}, onVehicleClick = {}, onQuickCalculate = {})
}
