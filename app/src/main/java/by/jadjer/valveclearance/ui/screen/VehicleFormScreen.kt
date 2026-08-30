package by.jadjer.valveclearance.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import by.jadjer.valveclearance.R
import by.jadjer.valveclearance.domain.model.Vehicle
import by.jadjer.valveclearance.domain.repository.VehicleRepository
import by.jadjer.valveclearance.ui.component.AppScaffold
import by.jadjer.valveclearance.ui.component.NumberInput
import by.jadjer.valveclearance.ui.component.SectionHeader
import by.jadjer.valveclearance.ui.viewmodel.VehicleFormViewModel
import by.jadjer.valveclearance.ui.viewmodel.VehicleFormViewModelFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Composable
fun VehicleFormScreen(
    repository: VehicleRepository,
    vehicleId: Long = -1L,
    onVehicleSaved: () -> Unit,
    onBack: () -> Unit
) {
    val viewModel: VehicleFormViewModel = viewModel(
        factory = VehicleFormViewModelFactory(repository, vehicleId)
    )
    val brand by viewModel.brand.collectAsStateWithLifecycle()
    val model by viewModel.model.collectAsStateWithLifecycle()
    val year by viewModel.year.collectAsStateWithLifecycle()
    
    val cylinders by viewModel.cylinders.collectAsStateWithLifecycle()
    val intakeValves by viewModel.intakeValves.collectAsStateWithLifecycle()
    val exhaustValves by viewModel.exhaustValves.collectAsStateWithLifecycle()
    
    val intakeMin by viewModel.intakeMin.collectAsStateWithLifecycle()
    val intakeMax by viewModel.intakeMax.collectAsStateWithLifecycle()
    val exhaustMin by viewModel.exhaustMin.collectAsStateWithLifecycle()
    val exhaustMax by viewModel.exhaustMax.collectAsStateWithLifecycle()

    AppScaffold(
        title = stringResource(if (vehicleId == -1L) R.string.add_vehicle else R.string.edit_vehicle),
        onBack = onBack
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                SectionHeader(stringResource(R.string.general_info))
                
                OutlinedTextField(
                    value = brand,
                    onValueChange = { viewModel.setBrand(it) },
                    label = { Text(stringResource(R.string.brand_label), modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = model,
                    onValueChange = { viewModel.setModel(it) },
                    label = { Text(stringResource(R.string.model_label), modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = year,
                    onValueChange = { viewModel.setYear(it) },
                    label = { Text(stringResource(R.string.year_label), modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
                )
            }
            
            HorizontalDivider()
            
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                SectionHeader(stringResource(R.string.engine_config))
                NumberInput(label = stringResource(R.string.num_cylinders), value = cylinders, onValueChange = { viewModel.setCylinders(it) }, range = 1..12)
                NumberInput(label = stringResource(R.string.intake_valves_per_cyl), value = intakeValves, onValueChange = { viewModel.setIntakeValves(it) }, range = 1..4)
                NumberInput(label = stringResource(R.string.exhaust_valves_per_cyl), value = exhaustValves, onValueChange = { viewModel.setExhaustValves(it) }, range = 1..4)
            }
            
            HorizontalDivider()
            
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                SectionHeader(stringResource(R.string.service_limits))
                
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                        OutlinedTextField(
                            value = intakeMin,
                            onValueChange = { viewModel.setIntakeMin(it) },
                            label = { Text(stringResource(R.string.intake_min), modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
                        )
                    }
                    Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                        OutlinedTextField(
                            value = intakeMax,
                            onValueChange = { viewModel.setIntakeMax(it) },
                            label = { Text(stringResource(R.string.intake_max), modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                        OutlinedTextField(
                            value = exhaustMin,
                            onValueChange = { viewModel.setExhaustMin(it) },
                            label = { Text(stringResource(R.string.exhaust_min), modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
                        )
                    }
                    Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                        OutlinedTextField(
                            value = exhaustMax,
                            onValueChange = { viewModel.setExhaustMax(it) },
                            label = { Text(stringResource(R.string.exhaust_max), modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(
                onClick = { viewModel.saveVehicle(onVehicleSaved) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.save_vehicle))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VehicleFormScreenPreview() {
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
    VehicleFormScreen(repository = mockRepo, onVehicleSaved = {}, onBack = {})
}
