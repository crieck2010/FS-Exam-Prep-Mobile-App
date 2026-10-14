package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.PrimaryBlueLight

@Composable
fun ArcScoreGauge(
    correctCount: Int,
    totalCount: Int,
    skippedCount: Int = 0,
    incorrectCount: Int = (totalCount - correctCount).coerceAtLeast(0),
    modifier: Modifier = Modifier
) {
    val progressFraction = if (totalCount > 0) {
        (correctCount.toFloat() / totalCount).coerceIn(0f, 1f)
    } else 0f

    val animatedProgress = remember { Animatable(0f) }
    LaunchedEffect(progressFraction) {
        animatedProgress.animateTo(
            targetValue = progressFraction,
            animationSpec = tween(durationMillis = 900)
        )
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(width = 240.dp, height = 135.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Canvas(modifier = Modifier.size(width = 240.dp, height = 135.dp)) {
                val strokeWidth = 14.dp.toPx()
                val diameter = size.width - strokeWidth
                val arcSize = Size(diameter, diameter)
                val topLeft = Offset(strokeWidth / 2f, strokeWidth / 2f)

                // Background track (180 degrees from 180° to 360°)
                drawArc(
                    color = Color(0xFF282B33),
                    startAngle = 180f,
                    sweepAngle = 180f,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )

                // If there are incorrect answers, draw trailing red accent on the remaining portion
                if (totalCount > 0 && incorrectCount > 0) {
                    val remainingAngle = 180f * (1f - animatedProgress.value)
                    val startAngle = 180f + (180f * animatedProgress.value)
                    drawArc(
                        color = Color(0xFFE28B93),
                        startAngle = startAngle,
                        sweepAngle = remainingAngle,
                        useCenter = false,
                        topLeft = topLeft,
                        size = arcSize,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                }

                // Foreground active arc with gradient (Cyan to Blue)
                val activeSweep = 180f * animatedProgress.value
                if (activeSweep > 0f) {
                    val gradient = Brush.horizontalGradient(
                        colors = listOf(AccentCyan, PrimaryBlueLight)
                    )
                    drawArc(
                        brush = gradient,
                        startAngle = 180f,
                        sweepAngle = activeSweep,
                        useCenter = false,
                        topLeft = topLeft,
                        size = arcSize,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                }
            }

            // Center Numbers & Label
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Text(
                    text = "$correctCount/$totalCount",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Correct",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Subtext: "0 skipped • 10 incorrect"
        Text(
            text = "$skippedCount skipped • $incorrectCount incorrect",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
