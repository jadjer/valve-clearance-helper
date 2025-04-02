package by.jadjer.valveclearanceassistant.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import by.jadjer.valveclearanceassistant.repository.ValveClearanceRepository
import by.jadjer.valveclearanceassistant.ui.component.FloatInput
import by.jadjer.valveclearanceassistant.ui.viewmodel.ServiceLimitsViewModel
import by.jadjer.valveclearanceassistant.ui.viewmodel.ServiceLimitsViewModelFactory

@Composable
fun ServiceLimitsScreen(
    repository: ValveClearanceRepository = ValveClearanceRepository(),
    onNext: () -> Unit,
) {
    val viewModel: ServiceLimitsViewModel = viewModel(factory = ServiceLimitsViewModelFactory(repository))

    val intakeClearanceMin by viewModel.intakeClearanceMin.collectAsState()
    val intakeClearanceMax by viewModel.intakeClearanceMax.collectAsState()
    val exhaustClearanceMin by viewModel.exhaustClearanceMin.collectAsState()
    val exhaustClearanceMax by viewModel.exhaustClearanceMax.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.setIntakeClearanceMin(intakeClearanceMin)
        viewModel.setIntakeClearanceMax(intakeClearanceMax)
        viewModel.setExhaustClearanceMin(exhaustClearanceMin)
        viewModel.setExhaustClearanceMax(exhaustClearanceMax)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Service Limits (mm)", style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.weight(1f))

        FloatInput(label = "Intake valve min", value = intakeClearanceMin, onValueChange = { viewModel.setIntakeClearanceMin(it) })
        FloatInput(label = "Intake valve max", value = intakeClearanceMax, onValueChange = { viewModel.setIntakeClearanceMax(it) })
        FloatInput(label = "Exhaust valve min", value = exhaustClearanceMin, onValueChange = { viewModel.setExhaustClearanceMin(it) })
        FloatInput(label = "Exhaust valve max", value = exhaustClearanceMax, onValueChange = { viewModel.setExhaustClearanceMax(it) })

        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = onNext, modifier = Modifier.fillMaxWidth()) {
            Text("Next")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ServiceLimitsScreenPreview() {
    ServiceLimitsScreen {}
}
