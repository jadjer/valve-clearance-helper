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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CylinderHeadDetailedView(
    intakeValvesCount: Int = 2,
    exhaustValvesCount: Int = 2,
    highlightedValveIndex: Int = -1,
    modifier: Modifier = Modifier,
    headWidth: Dp = 250.dp,
    headHeight: Dp = 100.dp,
    headDepth: Dp = 50.dp,
    valveDiameter: Dp = 18.dp,
    shimThickness: Dp = 5.dp,
    angleDegrees: Float = 20f
) {
    require(intakeValvesCount > 0) { "Intake valves count must be positive" }
    require(exhaustValvesCount > 0) { "Exhaust valves count must be positive" }

    Canvas(
        modifier = modifier
            .background(Color(0xFFF0F0F0))
    ) {
        val width = headWidth.toPx()
        val height = headHeight.toPx()
        val depth = headDepth.toPx()
        val valveRadius = valveDiameter.toPx() / 2
        val shimThicknessPx = shimThickness.toPx()

        // Параметры для 3D эффекта
        val angleRad = angleDegrees * PI.toFloat() / 180f
        val perspectiveFactor = 0.7f + 0.3f * cos(angleRad)

        val centerX = size.width / 2
        val centerY = size.height / 2

        // Рисуем головку блока
        draw3DHeadBlockDetailed(
            center = Offset(centerX, centerY),
            width = width,
            height = height,
            depth = depth,
            angleRad = angleRad
        )

        // Впускные клапаны (слева)
        drawValveSet(
            center = Offset(centerX - width * 0.2f, centerY),
            count = intakeValvesCount,
            isIntake = true,
            valveRadius = valveRadius,
            shimThicknessPx = shimThicknessPx,
            angleRad = angleRad,
            highlightedIndex = if (highlightedValveIndex < intakeValvesCount) highlightedValveIndex else -1,
            startIndex = 0
        )

        // Выпускные клапаны (справа)
        drawValveSet(
            center = Offset(centerX + width * 0.2f, centerY),
            count = exhaustValvesCount,
            isIntake = false,
            valveRadius = valveRadius,
            shimThicknessPx = shimThicknessPx,
            angleRad = angleRad,
            highlightedIndex = if (highlightedValveIndex >= intakeValvesCount) highlightedValveIndex - intakeValvesCount else -1,
            startIndex = intakeValvesCount
        )

        // Камера сгорания (вид сверху)
        drawCombustionChamberTopView(
            center = Offset(centerX, centerY + height * 0.1f),
            width = width * 0.6f,
            height = height * 0.4f,
            angleRad = angleRad,
            intakeCount = intakeValvesCount,
            exhaustCount = exhaustValvesCount
        )
    }
}

private fun DrawScope.draw3DHeadBlockDetailed(
    center: Offset,
    width: Float,
    height: Float,
    depth: Float,
    angleRad: Float
) {
    val perspectiveFactor = 0.7f + 0.3f * cos(angleRad)

    // Основная часть головки
    val mainPath = Path().apply {
        // Верхняя грань
        moveTo(center.x - width/2, center.y - height/2)
        lineTo(center.x + width/2, center.y - height/2)
        lineTo(center.x + width/2, center.y + height/2)
        lineTo(center.x - width/2, center.y + height/2)
        close()

        // Передняя грань
        moveTo(center.x - width/2, center.y - height/2)
        lineTo(center.x - width/2 * perspectiveFactor, center.y - height/2 - depth * 0.4f)
        lineTo(center.x + width/2 * perspectiveFactor, center.y - height/2 - depth * 0.4f)
        lineTo(center.x + width/2, center.y - height/2)
        close()

        // Боковая грань (правая)
        moveTo(center.x + width/2, center.y - height/2)
        lineTo(center.x + width/2, center.y + height/2)
        lineTo(center.x + width/2 * perspectiveFactor, center.y + height/2 - depth * 0.3f)
        lineTo(center.x + width/2 * perspectiveFactor, center.y - height/2 - depth * 0.4f)
        close()
    }

    drawPath(
        path = mainPath,
        color = Color(0xFF606060),
        style = Fill
    )

    // Впускные каналы (левая сторона)
    val intakePath = Path().apply {
        val intakeWidth = width * 0.15f
        val intakeHeight = height * 0.6f

        moveTo(center.x - width/2 * 0.8f, center.y - intakeHeight/2)
        lineTo(center.x - width/2 * 0.8f - depth * 0.5f, center.y - intakeHeight/2 - depth * 0.3f)
        lineTo(center.x - width/2 * 0.8f - depth * 0.5f, center.y + intakeHeight/2 - depth * 0.3f)
        lineTo(center.x - width/2 * 0.8f, center.y + intakeHeight/2)
        close()
    }

    drawPath(
        path = intakePath,
        color = Color(0xFF7BA4D1),
        style = Fill
    )

    // Выпускные каналы (правая сторона)
    val exhaustPath = Path().apply {
        val exhaustWidth = width * 0.15f
        val exhaustHeight = height * 0.6f

        moveTo(center.x + width/2 * 0.8f, center.y - exhaustHeight/2)
        lineTo(center.x + width/2 * 0.8f + depth * 0.5f, center.y - exhaustHeight/2 - depth * 0.3f)
        lineTo(center.x + width/2 * 0.8f + depth * 0.5f, center.y + exhaustHeight/2 - depth * 0.3f)
        lineTo(center.x + width/2 * 0.8f, center.y + exhaustHeight/2)
        close()
    }

    drawPath(
        path = exhaustPath,
        color = Color(0xFFD17B7B),
        style = Fill
    )
}

private fun DrawScope.drawValveSet(
    center: Offset,
    count: Int,
    isIntake: Boolean,
    valveRadius: Float,
    shimThicknessPx: Float,
    angleRad: Float,
    highlightedIndex: Int,
    startIndex: Int
) {
    val spacing = valveRadius * 4f
    val startY = center.y - (count - 1) * spacing / 2

    for (i in 0 until count) {
        val valveY = startY + i * spacing
        val globalIndex = startIndex + i
        val isHighlighted = globalIndex == highlightedIndex

        val valveColor = if (isIntake) {
            if (isHighlighted) Color(0xFF0066CC) else Color(0xFF3388EE)
        } else {
            if (isHighlighted) Color(0xFFCC3300) else Color(0xFFEE5533)
        }

        val shimColor = if (isHighlighted) Color(0xFFAAAAAA) else Color(0xFF888888)

        // Клапан (стержень)
//        draw3DValveStemDetailed(
//            top = Offset(center.x, valveY - valveRadius * 2f),
//            bottom = Offset(center.x, valveY + valveRadius * 3f),
//            radius = valveRadius * 0.6f,
//            angleRad = angleRad,
//            color = valveColor
//        )

        // Регулировочная шайба
        draw3DShimDetailed(
            center = Offset(center.x, valveY - valveRadius * 1.5f),
            radius = valveRadius * 0.9f,
            thickness = shimThicknessPx,
            angleRad = angleRad,
            color = shimColor
        )

        // Тарелка клапана (в камере сгорания)
        draw3DValvePlateDetailed(
            center = Offset(center.x, valveY + valveRadius * 2f),
            radius = if (isIntake) valveRadius * 1.6f else valveRadius * 1.4f,
            thickness = valveRadius * 0.4f,
            angleRad = angleRad,
            color = valveColor
        )

        // Пружина (упрощенная)
//        drawValveSpringDetailed(
//            top = Offset(center.x, valveY - valveRadius * 3f),
//            bottom = Offset(center.x, valveY - valveRadius * 0.5f),
//            radius = valveRadius * 1.3f,
//            angleRad = angleRad,
//            color = Color(0xFFBBBBBB)
//        )
    }
}

//private fun DrawScope.draw3DValveStemDetailed(
//    top: Offset,
//    bottom: Offset,
//    radius: Float,
//    angleRad: Float,
//    color: Color
//) {
//    val direction = bottom - top
//    val length = direction.getDistance()
//    val angle = atan2(direction.y, direction.x)
//    val perspectiveFactor = cos(angleRad)
//
//    rotate(
//        degrees = angle * (180f / PI.toFloat()) - 90f,
//        pivot = top
//    ) {
//        // Основной стержень
//        drawRect(
//            brush = Brush.verticalGradient(
//                colors = listOf(color, color.copy(alpha = 0.8f))
//            ),
//            topLeft = Offset(top.x - radius * perspectiveFactor, top.y),
//            size = Size(radius * 2 * perspectiveFactor, length)
//        )
//
//        // Боковая грань
//        val path = Path().apply {
//            moveTo(top.x + radius * perspectiveFactor, top.y)
//            lineTo(top.x + radius * perspectiveFactor, top.y + length)
//            lineTo(top.x + radius * perspectiveFactor * 1.4f, top.y + length - radius * 0.4f)
//            lineTo(top.x + radius * perspectiveFactor * 1.4f, top.y + radius * 0.4f)
//            close()
//        }
//
//        drawPath(
//            path = path,
//            color = color.copy(alpha = 0.6f),
//            style = Fill
//        )
//    }
//}

private fun DrawScope.draw3DShimDetailed(
    center: Offset,
    radius: Float,
    thickness: Float,
    angleRad: Float,
    color: Color
) {
    val perspectiveFactor = cos(angleRad)

    // Верхняя часть шайбы
    drawOval(
        color = color,
        topLeft = Offset(center.x - radius * perspectiveFactor, center.y - thickness/2),
        size = Size(radius * 2 * perspectiveFactor, thickness),
        style = Fill
    )

    // Боковая поверхность
    val path = Path().apply {
        arcTo(
            rect = androidx.compose.ui.geometry.Rect(
                center.x - radius * perspectiveFactor,
                center.y - thickness/2,
                center.x + radius * perspectiveFactor,
                center.y + thickness/2
            ),
            startAngleDegrees = 0f,
            sweepAngleDegrees = 180f,
            forceMoveTo = true
        )
        lineTo(center.x + radius * perspectiveFactor * 0.9f, center.y + thickness)
        arcTo(
            rect = androidx.compose.ui.geometry.Rect(
                center.x - radius * perspectiveFactor * 0.9f,
                center.y + thickness/2,
                center.x + radius * perspectiveFactor * 0.9f,
                center.y + thickness * 1.5f
            ),
            startAngleDegrees = 0f,
            sweepAngleDegrees = -180f,
            forceMoveTo = false
        )
        lineTo(center.x - radius * perspectiveFactor, center.y - thickness/2)
    }

    drawPath(
        path = path,
        color = color.copy(alpha = 0.7f),
        style = Fill
    )

    // Маркировка толщины
    drawOval(
        color = Color(0xFF666666),
        topLeft = Offset(center.x - radius * 0.5f * perspectiveFactor, center.y - thickness/4),
        size = Size(radius * perspectiveFactor, thickness/2),
        style = Fill
    )
}

private fun DrawScope.draw3DValvePlateDetailed(
    center: Offset,
    radius: Float,
    thickness: Float,
    angleRad: Float,
    color: Color
) {
    val perspectiveFactor = cos(angleRad)

    // Верхняя часть тарелки
    drawOval(
        color = color,
        topLeft = Offset(center.x - radius * perspectiveFactor, center.y - thickness),
        size = Size(radius * 2 * perspectiveFactor, thickness * 2),
        style = Fill
    )

    // Боковая поверхность
    val path = Path().apply {
        arcTo(
            rect = androidx.compose.ui.geometry.Rect(
                center.x - radius * perspectiveFactor,
                center.y - thickness,
                center.x + radius * perspectiveFactor,
                center.y + thickness
            ),
            startAngleDegrees = 0f,
            sweepAngleDegrees = 180f,
            forceMoveTo = true
        )
        lineTo(center.x + radius * perspectiveFactor * 0.8f, center.y + thickness * 1.5f)
        arcTo(
            rect = androidx.compose.ui.geometry.Rect(
                center.x - radius * perspectiveFactor * 0.8f,
                center.y + thickness,
                center.x + radius * perspectiveFactor * 0.8f,
                center.y + thickness * 2f
            ),
            startAngleDegrees = 0f,
            sweepAngleDegrees = -180f,
            forceMoveTo = false
        )
        lineTo(center.x - radius * perspectiveFactor, center.y - thickness)
    }

    drawPath(
        path = path,
        color = color.copy(alpha = 0.8f),
        style = Fill
    )

    // Фаска на тарелке
    drawOval(
        color = color.copy(alpha = 0.6f),
        topLeft = Offset(center.x - radius * 0.7f * perspectiveFactor, center.y - thickness * 0.7f),
        size = Size(radius * 1.4f * perspectiveFactor, thickness * 1.4f),
        style = Stroke(width = 1.5f)
    )
}

//private fun DrawScope.drawValveSpringDetailed(
//    top: Offset,
//    bottom: Offset,
//    radius: Float,
//    angleRad: Float,
//    color: Color
//) {
//    val direction = bottom - top
//    val length = direction.getDistance()
//    val angle = atan2(direction.y, direction.x)
//    val perspectiveFactor = cos(angleRad)
//    val coils = 8
//
//    rotate(
//        degrees = angle * (180f / PI.toFloat()) - 90f,
//        pivot = top
//    ) {
//        val path = Path()
//        val coilHeight = length / coils
//
//        path.moveTo(top.x, top.y)
//
//        for (i in 0..coils) {
//            val y = top.y + i * coilHeight
//            val x = top.x + if (i % 2 == 0) radius * perspectiveFactor else -radius * perspectiveFactor
//            path.lineTo(x, y)
//        }
//
//        drawPath(
//            path = path,
//            color = color,
//            style = Stroke(width = 2f)
//        )
//    }
//}

private fun DrawScope.drawCombustionChamberTopView(
    center: Offset,
    width: Float,
    height: Float,
    angleRad: Float,
    intakeCount: Int,
    exhaustCount: Int
) {
    val perspectiveFactor = cos(angleRad)
    val chamberWidth = width * perspectiveFactor
    val chamberHeight = height

    // Основная камера
    drawOval(
        color = Color(0xFF333333),
        topLeft = Offset(center.x - chamberWidth/2, center.y - chamberHeight/2),
        size = Size(chamberWidth, chamberHeight),
        style = Fill
    )

    // Впускные клапаны (левая сторона)
    val intakeSpacing = chamberHeight / (intakeCount + 1)
    for (i in 1..intakeCount) {
        val y = center.y - chamberHeight/2 + i * intakeSpacing
        drawOval(
            color = Color(0xFF3388EE).copy(alpha = 0.7f),
            topLeft = Offset(center.x - chamberWidth * 0.4f - 10f, y - 15f),
            size = Size(30f, 30f),
            style = Fill
        )
    }

    // Выпускные клапаны (правая сторона)
    val exhaustSpacing = chamberHeight / (exhaustCount + 1)
    for (i in 1..exhaustCount) {
        val y = center.y - chamberHeight/2 + i * exhaustSpacing
        drawOval(
            color = Color(0xFFEE5533).copy(alpha = 0.7f),
            topLeft = Offset(center.x + chamberWidth * 0.4f - 20f, y - 15f),
            size = Size(30f, 30f),
            style = Fill
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CylinderHeadDetailedViewPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F0F0))
            .padding(16.dp)
    ) {
        CylinderHeadDetailedView(
            intakeValvesCount = 2,
            exhaustValvesCount = 2,
            highlightedValveIndex = 1,
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp),
            angleDegrees = 20f
        )

        Spacer(modifier = Modifier.height(16.dp))

        CylinderHeadDetailedView(
            intakeValvesCount = 3,
            exhaustValvesCount = 2,
            highlightedValveIndex = 4,
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp),
            angleDegrees = 15f
        )
    }
}