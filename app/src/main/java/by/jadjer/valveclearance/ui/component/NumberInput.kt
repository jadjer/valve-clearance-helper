package by.jadjer.valveclearance.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NumberInput(label: String, value: Int, onValueChange: (Int) -> Unit, range: IntRange) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { if (value > range.first) onValueChange(value - 1) }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Decrease")
            }
            Text(text = value.toString(), style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(horizontal = 16.dp))
            IconButton(onClick = { if (value < range.last) onValueChange(value + 1) }) {
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Increase")
            }
        }
    }
}
