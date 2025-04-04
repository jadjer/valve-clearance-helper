package by.jadjer.valveclearance.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun FloatInput(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    var textValue by remember { mutableStateOf(value.toString()) }

    // Синхронизация при изменении внешнего value
    LaunchedEffect(value) {
        textValue = value.toString()
    }

    Column(modifier = modifier.padding(vertical = 8.dp)) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        OutlinedTextField(
            value = textValue,
            onValueChange = {
                textValue = it
                it.toFloatOrNull()?.let { float ->
                    onValueChange(float)
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
fun FloatInputPreview() {
    FloatInput(label = "Test", value = 123f, onValueChange = {})
}
