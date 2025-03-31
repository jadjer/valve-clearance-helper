package by.jadjer.valveclearanceassistant.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun WelcomeScreen(
    onNext: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Valve Clearance Calculator", style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(32.dp))

        Text(text = "This app helps you calculate optimal shim configuration for valve adjustment", style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)

        Spacer(modifier = Modifier.height(48.dp))
        Button(onClick = onNext, modifier = Modifier.width(200.dp)) {
            Text(text = "Start")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    WelcomeScreen {}
}
