package by.jadjer.valveclearance.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    var textValue1 by remember { mutableStateOf(value1.toString()) }
    var textValue2 by remember { mutableStateOf(value2.toString()) }

    val floatValue1 = textValue1.toFloatOrNull()
    val floatValue2 = textValue2.toFloatOrNull()
    val bothValid = floatValue1 != null && floatValue2 != null

    LaunchedEffect(floatValue1, floatValue2) {
        if (bothValid) {
            onValueChange(floatValue1, floatValue2)
        }
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FloatInput(
            label = label1,
            value = textValue1.toFloat(),
            onValueChange = { textValue1 = it.toString() },
            modifier = Modifier.weight(1f),
            isError = textValue1.isNotEmpty() && floatValue1 == null
        )

        FloatInput(
            label = label2,
            value = textValue2.toFloat(),
            onValueChange = { textValue2 = it.toString() },
            modifier = Modifier.weight(1f),
            isError = textValue2.isNotEmpty() && floatValue2 == null
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TwoFloatInputsInRowPreview() {
    TwoFloatInputsInRow(
        label1 = "Test1",
        value1 = 0.2f,
        label2 = "Test2",
        value2 = 0.3f,
        onValueChange = { _, _ -> },
        modifier = Modifier
    )
}
