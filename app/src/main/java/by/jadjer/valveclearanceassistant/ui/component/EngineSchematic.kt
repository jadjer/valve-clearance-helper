package by.jadjer.valveclearanceassistant.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun EngineSchematic(
    cylinderCount: Int,
    valvesPerCylinder: Int,
    highlightedValveIndex: Int = -1,
    modifier: Modifier = Modifier,
    cylinderSpacing: Dp = 40.dp,
    cylinderSize: Dp = 60.dp,
    valveSize: Dp = 10.dp
) {
    require(cylinderCount > 0) { "Cylinder count must be positive" }
    require(valvesPerCylinder > 0) { "Valves per cylinder must be positive" }
    require(highlightedValveIndex < cylinderCount * valvesPerCylinder) {
        "Highlighted valve index out of bounds"
    }

    Canvas(modifier = modifier) {
        val spacingPx = cylinderSpacing.toPx()
        val cylinderSizePx = cylinderSize.toPx()
        val valveSizePx = valveSize.toPx()
        val halfValve = valveSizePx / 2

        // Рассчитываем общую ширину для центрирования
        val totalWidth = cylinderCount * cylinderSizePx +
                (cylinderCount - 1) * spacingPx
        val startX = (size.width - totalWidth) / 2

        // Рисуем каждый цилиндр
        for (cylinderIndex in 0 until cylinderCount) {
            val cylinderX = startX + cylinderIndex * (cylinderSizePx + spacingPx)
            val cylinderY = size.height / 2 - cylinderSizePx / 2

            // Корпус цилиндра
            drawRect(
                color = Color.Gray,
                topLeft = Offset(cylinderX, cylinderY),
                size = Size(cylinderSizePx, cylinderSizePx),
                style = Stroke(width = 2f)
            )

            // Поршень (внутри цилиндра)
            drawRect(
                color = Color.DarkGray,
                topLeft = Offset(cylinderX + 5f, cylinderY + cylinderSizePx / 3),
                size = Size(cylinderSizePx - 10f, cylinderSizePx / 3)
            )

            // Клапаны (над цилиндром)
            val valveRowY = cylinderY - valveSizePx - 5f
            for (valveIndex in 0 until valvesPerCylinder) {
                val valveX = cylinderX +
                        (cylinderSizePx / (valvesPerCylinder + 1)) * (valveIndex + 1) -
                        halfValve

                val globalValveIndex = cylinderIndex * valvesPerCylinder + valveIndex
                val isHighlighted = globalValveIndex == highlightedValveIndex

                drawCircle(
                    color = if (isHighlighted) Color.Red else Color.Blue,
                    center = Offset(valveX, valveRowY),
                    radius = valveSizePx / 2,
                    style = if (isHighlighted) Stroke(width = 2f) else Stroke(width = 1f)
                )

                // Ножка клапана
                drawLine(
                    color = if (isHighlighted) Color.Red else Color.Blue,
                    start = Offset(valveX, valveRowY + halfValve),
                    end = Offset(valveX, cylinderY),
                    strokeWidth = if (isHighlighted) 2f else 1f
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EngineSchematicPreview() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        EngineSchematic(
            cylinderCount = 4,
            valvesPerCylinder = 4,
            highlightedValveIndex = 5,
            modifier = Modifier.fillMaxWidth().height(200.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        EngineSchematic(
            cylinderCount = 6,
            valvesPerCylinder = 2,
            highlightedValveIndex = 7,
            modifier = Modifier.fillMaxWidth().height(200.dp)
        )
    }
}