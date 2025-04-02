package by.jadjer.valveclearanceassistant.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import by.jadjer.valveclearanceassistant.repository.ValveClearanceRepository
import by.jadjer.valveclearanceassistant.ui.component.NumberInput
import by.jadjer.valveclearanceassistant.ui.viewmodel.EngineParamsViewModel
import by.jadjer.valveclearanceassistant.ui.viewmodel.EngineParamsViewModelFactory

@Composable
fun EngineParamsScreen(
    repository: ValveClearanceRepository = ValveClearanceRepository(),
    onNext: () -> Unit,
) {
    val viewModel: EngineParamsViewModel = viewModel(
        factory = EngineParamsViewModelFactory(repository),
    )

    val cylinders by viewModel.cylinders.collectAsState()
    val intakeValves by viewModel.intakeValves.collectAsState()
    val exhaustValves by viewModel.exhaustValves.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.setCylinders(cylinders)
        viewModel.setIntakeValves(intakeValves)
        viewModel.setExhaustValves(exhaustValves)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Engine Parameters", style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.weight(1f))

        Column {
            NumberInput(label = "Number of cylinders", value = cylinders, onValueChange = { viewModel.setCylinders(it) }, range = 1..12)
            NumberInput(label = "Intake valves per cylinder", value = intakeValves, onValueChange = { viewModel.setIntakeValves(it) }, range = 1..4)
            NumberInput(label = "Exhaust valves per cylinder", value = exhaustValves, onValueChange = { viewModel.setExhaustValves(it) }, range = 1..4)
        }

        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = onNext, modifier = Modifier.fillMaxWidth()) {
            Text("Next")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EngineParamsScreenPreview() {
    EngineParamsScreen {}
}
