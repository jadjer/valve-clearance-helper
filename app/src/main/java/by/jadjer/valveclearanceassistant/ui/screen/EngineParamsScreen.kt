package by.jadjer.valveclearanceassistant.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun EngineParamsScreen(
    onNext: (cylinders: Int, intakeValves: Int, exhaustValves: Int) -> Unit
) {
    var cylinders by remember { mutableIntStateOf(4) }
    var intakeValves by remember { mutableIntStateOf(2) }
    var exhaustValves by remember { mutableIntStateOf(2) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Engine Parameters",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))
        NumberInput(
            label = "Number of cylinders",
            value = cylinders,
            onValueChange = { cylinders = it },
            range = 1..12
        )
        NumberInput(
            label = "Intake valves per cylinder",
            value = intakeValves,
            onValueChange = { intakeValves = it },
            range = 1..4
        )
        NumberInput(
            label = "Exhaust valves per cylinder",
            value = exhaustValves,
            onValueChange = { exhaustValves = it },
            range = 1..4
        )
        Spacer(modifier = Modifier.height(48.dp))
        Button(
            onClick = { onNext(cylinders, intakeValves, exhaustValves) },
            modifier = Modifier.width(200.dp)
        ) {
            Text("Next")
        }
    }
}

@Composable
fun NumberInput(label: String, value: Int, onValueChange: (Int) -> Unit, range: IntRange) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { if (value > range.first) onValueChange(value - 1) }) {
                Icon(Icons.Default.Delete, contentDescription = "Decrease")
            }
            Text(
                text = value.toString(),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            IconButton(onClick = { if (value < range.last) onValueChange(value + 1) }) {
                Icon(Icons.Default.Add, contentDescription = "Increase")
            }
        }
    }
}