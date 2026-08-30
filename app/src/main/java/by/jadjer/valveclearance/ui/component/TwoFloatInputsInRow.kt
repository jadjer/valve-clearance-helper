package by.jadjer.valveclearance.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TwoFloatInputsInRow(
    label1: String,
    value1: Float,
    label2: String,
    value2: Float,
    onValueChange: (Float, Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(modifier = Modifier.weight(1f)) {
            FloatInput(
                label = label1,
                value = value1,
                onValueChange = { onValueChange(it, value2) }
            )
        }
        Box(modifier = Modifier.weight(1f)) {
            FloatInput(
                label = label2,
                value = value2,
                onValueChange = { onValueChange(value1, it) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TwoFloatInputsInRowPreview() {
    TwoFloatInputsInRow(
        label1 = "Min", value1 = 0.10f,
        label2 = "Max", value2 = 0.20f,
        onValueChange = { _, _ -> }
    )
}
