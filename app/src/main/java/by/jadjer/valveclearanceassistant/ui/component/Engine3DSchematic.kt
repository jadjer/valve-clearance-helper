package by.jadjer.valveclearanceassistant.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.*

@Composable
fun Engine3DSchematic(
    cylinderCount: Int,
    valvesPerCylinder: Int,
    highlightedValveIndex: Int = -1,
    modifier: Modifier = Modifier,
    cylinderSpacing: Dp = 50.dp,
    cylinderDiameter: Dp = 60.dp,
    valveDiameter: Dp = 12.dp,
    pistonStroke: Dp = 30.dp,
    angleDegrees: Float = 30f // Угол обзора для 3D эффекта
) {
    require(cylinderCount > 0) { "Cylinder count must be positive" }
    require(valvesPerCylinder > 0) { "Valves per cylinder must be positive" }
    require(highlightedValveIndex < cylinderCount * valvesPerCylinder) {
        "Highlighted valve index out of bounds"
    }

    Canvas(
        modifier = modifier
            .background(Color(0xFF222222))
    ) {
        val spacingPx = cylinderSpacing.toPx()
        val cylinderRadius = cylinderDiameter.toPx() / 2
        val valveRadius = valveDiameter.toPx() / 2
        val pistonStrokePx = pistonStroke.toPx()

        // Общие параметры для 3D эффекта
        val perspectiveFactor = 0.7f
        val angleRad = angleDegrees * PI.toFloat() / 180f
        val cosAngle = cos(angleRad)
        val sinAngle = sin(angleRad)

        // Рассчитываем общую ширину для центрирования
        val totalWidth = cylinderCount * (cylinderDiameter.toPx() * perspectiveFactor) + (cylinderCount - 1) * spacingPx
        val startX = (size.width - totalWidth) / 2
        val baseY = size.height * 0.6f

//        // Рисуем блок цилиндров
//        drawBlock(startX, baseY, cylinderCount, cylinderRadius, spacingPx, perspectiveFactor)

        // Рисуем каждый цилиндр
        for (cylinderIndex in 0 until cylinderCount) {
            val cylinderX = startX + cylinderIndex * (cylinderRadius * 2 * perspectiveFactor + spacingPx)
            val cylinderCenter = Offset(cylinderX + cylinderRadius * perspectiveFactor, baseY - cylinderRadius * 1.5f)

            // Цилиндр (3D труба)
            draw3DCylinder(
                center = cylinderCenter,
                radius = cylinderRadius,
                length = pistonStrokePx * 2,
                angleRad = angleRad,
                color = Color(0xFF555555),
            )

            // Головка блока
//            draw3DHead(
//                center = Offset(cylinderCenter.x, cylinderCenter.y - cylinderRadius * 0.7f),
//                width = cylinderRadius * 2 * perspectiveFactor,
//                height = cylinderRadius * 0.8f,
//                depth = cylinderRadius * 1.2f,
//                angleRad = angleRad,
//                color = Color(0xFF444444),
//            )

            // Клапаны
            for (valveIndex in 0 until valvesPerCylinder) {
                val angle = 2f * PI.toFloat() * valveIndex / valvesPerCylinder
                val valveX = cylinderCenter.x + cos(angle) * cylinderRadius * 0.7f * perspectiveFactor
                val valveY = cylinderCenter.y - cylinderRadius * 0.7f + sin(angle) * cylinderRadius * 0.3f

                val globalValveIndex = cylinderIndex * valvesPerCylinder + valveIndex
                val isHighlighted = globalValveIndex == highlightedValveIndex

                // Ножка клапана
                draw3DValveStem(
                    start = Offset(valveX, valveY),
                    end = Offset(valveX, valveY - valveRadius * 3f),
                    radius = valveRadius * 0.3f,
                    angleRad = angleRad,
                    color = if (isHighlighted) Color.Red else Color(0xFF3366CC)
                )

                // Головка клапана
                draw3DValve(
                    center = Offset(valveX, valveY - valveRadius * 3f),
                    radius = valveRadius,
                    angleRad = angleRad,
                    color = if (isHighlighted) Color.Red else Color(0xFF4477DD)
                )
            }

            // Поршень
            draw3DPiston(
                center = Offset(cylinderCenter.x, cylinderCenter.y + pistonStrokePx * 0.5f),
                radius = cylinderRadius * 0.9f,
                height = cylinderRadius * 0.6f,
                angleRad = angleRad,
                color = Color(0xFF888888)
            )

            // Шатун
            draw3DConnectingRod(
                start = Offset(cylinderCenter.x, cylinderCenter.y + pistonStrokePx * 0.5f + cylinderRadius * 0.3f),
                end = Offset(cylinderCenter.x, baseY + cylinderRadius * 2f),
                width = cylinderRadius * 0.5f,
                angleRad = angleRad,
                color = Color(0xFF777777)
            )

            // Коленвал
            if (cylinderIndex == 0) {
                draw3DCrankshaft(
                    center = Offset(cylinderCenter.x, baseY + cylinderRadius * 2.5f),
                    radius = cylinderRadius * 0.8f,
                    width = totalWidth + cylinderRadius * 2f,
                    angleRad = angleRad,
                    color = Color(0xFF666666)
                )
            }
        }
    }
}

private fun DrawScope.drawBlock(
    startX: Float,
    baseY: Float,
    cylinderCount: Int,
    cylinderRadius: Float,
    spacingPx: Float,
    perspectiveFactor: Float
) {
    val blockHeight = cylinderRadius * 3f
    val blockWidth = cylinderCount * (cylinderRadius * 2 * perspectiveFactor) +
            (cylinderCount - 1) * spacingPx + cylinderRadius * 2f

    val path = Path().apply {
        moveTo(startX, baseY)
        lineTo(startX + blockWidth, baseY)
        lineTo(startX + blockWidth * 0.95f, baseY - blockHeight * 0.2f)
        lineTo(startX + blockWidth * 0.7f, baseY - blockHeight)
        lineTo(startX + blockWidth * 0.3f, baseY - blockHeight)
        lineTo(startX + blockWidth * 0.05f, baseY - blockHeight * 0.2f)
        close()
    }

    drawPath(
        path = path,
        color = Color(0xFF333333),
        style = Fill
    )

    // Боковая грань
    val sidePath = Path().apply {
        moveTo(startX + blockWidth, baseY)
        lineTo(startX + blockWidth * 0.95f, baseY - blockHeight * 0.2f)
        lineTo(startX + blockWidth * 0.95f - blockHeight * 0.15f, baseY - blockHeight * 0.2f - blockHeight * 0.1f)
        lineTo(startX + blockWidth - blockHeight * 0.15f, baseY - blockHeight * 0.1f)
        close()
    }

    drawPath(
        path = sidePath,
        color = Color(0xFF2A2A2A),
        style = Fill
    )
}

private fun DrawScope.draw3DCylinder(
    center: Offset,
    radius: Float,
    length: Float,
    angleRad: Float,
    color: Color
) {
    val perspectiveFactor = cos(angleRad)

    // Основной цилиндр
    drawOval(
        color = color,
        topLeft = Offset(center.x - radius * perspectiveFactor, center.y - radius),
        size = Size(radius * 2 * perspectiveFactor, radius * 2),
        style = Fill
    )

    // Нижняя часть цилиндра
    drawOval(
        color = color.copy(alpha = 0.7f),
        topLeft = Offset(center.x - radius * perspectiveFactor, center.y + length - radius),
        size = Size(radius * 2 * perspectiveFactor, radius * 2),
        style = Fill
    )

    // Боковая поверхность
    val path = Path().apply {
        moveTo(center.x - radius * perspectiveFactor, center.y - radius)
        lineTo(center.x - radius * perspectiveFactor, center.y + length - radius)
        arcTo(
            rect = androidx.compose.ui.geometry.Rect(
                center.x - radius * perspectiveFactor,
                center.y + length - radius,
                center.x + radius * perspectiveFactor,
                center.y + length + radius
            ),
            startAngleDegrees = 180f,
            sweepAngleDegrees = 180f,
            forceMoveTo = false
        )
        lineTo(center.x + radius * perspectiveFactor, center.y - radius)
        arcTo(
            rect = androidx.compose.ui.geometry.Rect(
                center.x - radius * perspectiveFactor,
                center.y - radius,
                center.x + radius * perspectiveFactor,
                center.y + radius
            ),
            startAngleDegrees = 0f,
            sweepAngleDegrees = -180f,
            forceMoveTo = false
        )
    }

    drawPath(
        path = path,
        color = color.copy(alpha = 0.8f),
        style = Fill
    )
}

private fun DrawScope.draw3DHead(
    center: Offset,
    width: Float,
    height: Float,
    depth: Float,
    angleRad: Float,
    color: Color
) {
    val perspectiveFactor = cos(angleRad)

    // Верхняя часть
    drawRect(
        color = color,
        topLeft = Offset(center.x - width / 2, center.y - height),
        size = Size(width, height),
        style = Fill
    )

    // Передняя грань
    val frontPath = Path().apply {
        moveTo(center.x - width / 2, center.y - height)
        lineTo(center.x - width / 2 + depth * 0.3f, center.y - height - depth * 0.2f)
        lineTo(center.x + width / 2 + depth * 0.3f, center.y - height - depth * 0.2f)
        lineTo(center.x + width / 2, center.y - height)
        close()
    }

    drawPath(
        path = frontPath,
        color = color.copy(alpha = 0.7f),
        style = Fill
    )

    // Боковая грань
    val sidePath = Path().apply {
        moveTo(center.x + width / 2, center.y - height)
        lineTo(center.x + width / 2, center.y)
        lineTo(center.x + width / 2 + depth * 0.3f, center.y - depth * 0.2f)
        lineTo(center.x + width / 2 + depth * 0.3f, center.y - height - depth * 0.2f)
        close()
    }

    drawPath(
        path = sidePath,
        color = color.copy(alpha = 0.6f),
        style = Fill
    )
}

private fun DrawScope.draw3DValve(
    center: Offset,
    radius: Float,
    angleRad: Float,
    color: Color
) {
    val perspectiveFactor = cos(angleRad)

    // Верхняя часть клапана
    drawOval(
        color = color,
        topLeft = Offset(center.x - radius * perspectiveFactor, center.y - radius),
        size = Size(radius * 2 * perspectiveFactor, radius * 2),
        style = Fill
    )

    // Боковая поверхность
    val path = Path().apply {
        arcTo(
            rect = androidx.compose.ui.geometry.Rect(
                center.x - radius * perspectiveFactor,
                center.y - radius,
                center.x + radius * perspectiveFactor,
                center.y + radius
            ),
            startAngleDegrees = 0f,
            sweepAngleDegrees = 180f,
            forceMoveTo = true
        )
        lineTo(center.x + radius * perspectiveFactor * 0.7f, center.y + radius * 1.5f)
        arcTo(
            rect = androidx.compose.ui.geometry.Rect(
                center.x - radius * perspectiveFactor * 0.7f,
                center.y + radius,
                center.x + radius * perspectiveFactor * 0.7f,
                center.y + radius * 2f
            ),
            startAngleDegrees = 0f,
            sweepAngleDegrees = -180f,
            forceMoveTo = false
        )
        lineTo(center.x - radius * perspectiveFactor, center.y)
    }

    drawPath(
        path = path,
        color = color.copy(alpha = 0.8f),
        style = Fill
    )
}

private fun DrawScope.draw3DValveStem(
    start: Offset,
    end: Offset,
    radius: Float,
    angleRad: Float,
    color: Color
) {
    val perspectiveFactor = cos(angleRad)
    val direction = end - start
    val length = direction.getDistance()
    val angle = atan2(direction.y, direction.x)

    rotate(
        degrees = angle * (180f / PI.toFloat()) - 90f,
        pivot = start
    ) {
        // Основной стержень
        drawRect(
            color = color,
            topLeft = Offset(start.x - radius * perspectiveFactor, start.y),
            size = Size(radius * 2 * perspectiveFactor, length),
            style = Fill
        )

        // Боковая грань
        val path = Path().apply {
            moveTo(start.x + radius * perspectiveFactor, start.y)
            lineTo(start.x + radius * perspectiveFactor, start.y + length)
            lineTo(start.x + radius * perspectiveFactor * 1.3f, start.y + length - radius * 0.5f)
            lineTo(start.x + radius * perspectiveFactor * 1.3f, start.y + radius * 0.5f)
            close()
        }

        drawPath(
            path = path,
            color = color.copy(alpha = 0.7f),
            style = Fill
        )
    }
}

private fun DrawScope.draw3DPiston(
    center: Offset,
    radius: Float,
    height: Float,
    angleRad: Float,
    color: Color
) {
    val perspectiveFactor = cos(angleRad)

    // Верхняя часть поршня
    drawOval(
        color = color,
        topLeft = Offset(center.x - radius * perspectiveFactor, center.y - height / 2),
        size = Size(radius * 2 * perspectiveFactor, height),
        style = Fill
    )

    // Боковая поверхность
    val path = Path().apply {
        arcTo(
            rect = androidx.compose.ui.geometry.Rect(
                center.x - radius * perspectiveFactor,
                center.y - height / 2,
                center.x + radius * perspectiveFactor,
                center.y + height / 2
            ),
            startAngleDegrees = 0f,
            sweepAngleDegrees = 180f,
            forceMoveTo = true
        )
        lineTo(center.x + radius * perspectiveFactor * 0.8f, center.y + height)
        arcTo(
            rect = androidx.compose.ui.geometry.Rect(
                center.x - radius * perspectiveFactor * 0.8f,
                center.y + height / 2,
                center.x + radius * perspectiveFactor * 0.8f,
                center.y + height * 1.5f
            ),
            startAngleDegrees = 0f,
            sweepAngleDegrees = -180f,
            forceMoveTo = false
        )
        lineTo(center.x - radius * perspectiveFactor, center.y - height / 2)
    }

    drawPath(
        path = path,
        color = color.copy(alpha = 0.8f),
        style = Fill
    )
}

private fun DrawScope.draw3DConnectingRod(
    start: Offset,
    end: Offset,
    width: Float,
    angleRad: Float,
    color: Color
) {
    val direction = end - start
    val length = direction.getDistance()
    val angle = atan2(direction.y, direction.x)

    rotate(
        degrees = angle * (180f / PI.toFloat()) - 90f,
        pivot = start
    ) {
        // Основной стержень
        drawRect(
            color = color,
            topLeft = Offset(start.x - width / 2, start.y),
            size = Size(width, length),
            style = Fill
        )

        // Боковая грань
        val perspectiveFactor = cos(angleRad)
        val path = Path().apply {
            moveTo(start.x + width / 2, start.y)
            lineTo(start.x + width / 2, start.y + length)
            lineTo(start.x + width / 2 * 1.3f * perspectiveFactor, start.y + length - width * 0.3f)
            lineTo(start.x + width / 2 * 1.3f * perspectiveFactor, start.y + width * 0.3f)
            close()
        }

        drawPath(
            path = path,
            color = color.copy(alpha = 0.7f),
            style = Fill
        )
    }
}

private fun DrawScope.draw3DCrankshaft(
    center: Offset,
    radius: Float,
    width: Float,
    angleRad: Float,
    color: Color
) {
    val perspectiveFactor = cos(angleRad)

    // Основной вал
    drawRect(
        color = color,
        topLeft = Offset(center.x - width / 2, center.y - radius * perspectiveFactor),
        size = Size(width, radius * 2 * perspectiveFactor),
        style = Fill
    )

    // Шейки коленвала
    for (i in 0..4) {
        val offsetX = -width / 2 + i * width / 4f
        drawOval(
            color = color.copy(alpha = 0.9f),
            topLeft = Offset(center.x + offsetX - radius * 0.7f * perspectiveFactor, center.y - radius * 1.5f * perspectiveFactor),
            size = Size(radius * 1.4f * perspectiveFactor, radius * 3f * perspectiveFactor),
            style = Fill
        )
    }

    // Боковая грань
    val path = Path().apply {
        moveTo(center.x + width / 2, center.y - radius * perspectiveFactor)
        lineTo(center.x + width / 2 + radius * 0.3f, center.y - radius * perspectiveFactor * 1.2f)
        lineTo(center.x + width / 2 + radius * 0.3f, center.y + radius * perspectiveFactor * 1.2f)
        lineTo(center.x + width / 2, center.y + radius * perspectiveFactor)
        close()
    }

    drawPath(
        path = path,
        color = color.copy(alpha = 0.7f),
        style = Fill
    )
}

@Preview(showBackground = true)
@Composable
fun Engine3DSchematicPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF222222))
            .padding(16.dp)
    ) {
        Engine3DSchematic(
            cylinderCount = 4,
            valvesPerCylinder = 4,
            highlightedValveIndex = 5,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Engine3DSchematic(
            cylinderCount = 6,
            valvesPerCylinder = 2,
            highlightedValveIndex = 7,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            angleDegrees = 20f
        )
    }
}
