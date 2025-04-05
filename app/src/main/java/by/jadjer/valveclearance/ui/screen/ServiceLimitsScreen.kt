package by.jadjer.valveclearance.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource

import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import by.jadjer.valveclearance.R
import by.jadjer.valveclearance.repository.ValveClearanceRepository
import by.jadjer.valveclearance.ui.component.FloatInput
import by.jadjer.valveclearance.ui.viewmodel.ServiceLimitsViewModel
import by.jadjer.valveclearance.ui.viewmodel.ServiceLimitsViewModelFactory

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

    val (focus1, focus2, focus3, focus4) = remember { FocusRequester.createRefs() }

    LaunchedEffect(Unit) {
        focus1.requestFocus()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.screen_service_limit),
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.weight(1f))

        Text("Intake valves:")
        FloatInput(
            label = stringResource(R.string.label_min),
            value = intakeClearanceMin,
            onValueChange = { viewModel.setIntakeClearanceMin(it) },
            focusRequester = focus1,
            nextFocusRequester = focus2
        )
        FloatInput(
            label = stringResource(R.string.label_max),
            value = intakeClearanceMax,
            onValueChange = { viewModel.setIntakeClearanceMax(it) },
            focusRequester = focus2,
            nextFocusRequester = focus3
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text("Exhaust valves:")
        FloatInput(
            label = stringResource(R.string.label_min),
            value = exhaustClearanceMin,
            onValueChange = { viewModel.setExhaustClearanceMin(it) },
            focusRequester = focus3,
            nextFocusRequester = focus4
        )
        FloatInput(
            label = stringResource(R.string.label_max),
            value = exhaustClearanceMax,
            onValueChange = { viewModel.setExhaustClearanceMax(it) },
            focusRequester = focus4,
        )

        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = {
            if (viewModel.saveData()) {
                onNext()
            }
        }, modifier = Modifier.fillMaxWidth(), enabled = viewModel.isValid()) {
            Text(text = stringResource(R.string.button_next))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ServiceLimitsScreenPreview() {
    ServiceLimitsScreen {}
}
