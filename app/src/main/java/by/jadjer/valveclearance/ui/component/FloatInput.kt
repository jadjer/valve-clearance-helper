package by.jadjer.valveclearance.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun FloatInput(
    label: String,
    value: Float,
    modifier: Modifier = Modifier,
    onValueChange: (Float) -> Unit,  // Принимаем Float? для поддержки очистки поля
    validator: (Float) -> Boolean = { true },
    focusRequester: FocusRequester = remember { FocusRequester() },
    nextFocusRequester: FocusRequester? = null,
    onFocusChanged: (Boolean) -> Unit = {}
) {
    var textValue by remember { mutableStateOf(value.toString()) }
    var isError by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(modifier = modifier.padding(vertical = 8.dp)) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        OutlinedTextField(
            value = textValue,
            onValueChange = { newValue ->
                textValue = newValue

                if (newValue.isEmpty()) {
                    isError = true
                    return@OutlinedTextField
                }

                val floatValue = newValue.toFloatOrNull()
                if (floatValue == null) {
                    isError = true  // Ошибка, если не число
                    return@OutlinedTextField
                }

                isError = !validator(floatValue)
                if (!isError) {
                    onValueChange(floatValue)
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = if (nextFocusRequester != null) ImeAction.Next else ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onNext = { nextFocusRequester?.requestFocus() },
                onDone = { keyboardController?.hide() }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .onFocusChanged { onFocusChanged(it.isFocused) },
            isError = isError,
            trailingIcon = {
                if (isError) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Error",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            },
            supportingText = {
                if (isError) {
                    Text(
                        text = if (textValue.isEmpty()) {
                            "Field cannot be empty"
                        } else {
                            "Enter a valid number"
                        },
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        )
    }
}

@Preview
@Composable
fun FloatInputPreview() {
    var temperature by remember { mutableStateOf<Float>(0f) }

    FloatInput(
        label = "Temperature (°C)",
        value = 36.6f,
        onValueChange = { temperature = it },
        validator = { it in -50f..100f },
        focusRequester = remember { FocusRequester() }
    )
}
