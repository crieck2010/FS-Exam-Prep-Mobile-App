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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.TabularNumStyle
import com.example.ui.theme.calm

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
            animationSpec = tween(durationMillis = 800)
        )
    }

    val trackColor = MaterialTheme.calm.divider
    val activeColor = MaterialTheme.calm.accentBright
    val warnColor = MaterialTheme.calm.warn

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(width = 220.dp, height = 125.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Canvas(modifier = Modifier.size(width = 220.dp, height = 125.dp)) {
                // Thin stroke (4dp) per Calm Pro guidelines
                val strokeWidth = 4.dp.toPx()
                val diameter = size.width - strokeWidth
                val arcSize = Size(diameter, diameter)
                val topLeft = Offset(strokeWidth / 2f, strokeWidth / 2f)

                // Background track (180 degrees from 180° to 360°)
                drawArc(
                    color = trackColor,
                    startAngle = 180f,
                    sweepAngle = 180f,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )

                // Optional subtle trailing incorrect arc
                if (totalCount > 0 && incorrectCount > 0) {
                    val remainingAngle = 180f * (1f - animatedProgress.value)
                    val startAngle = 180f + (180f * animatedProgress.value)
                    drawArc(
                        color = warnColor.copy(alpha = 0.5f),
                        startAngle = startAngle,
                        sweepAngle = remainingAngle,
                        useCenter = false,
                        topLeft = topLeft,
                        size = arcSize,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                }

                // Foreground active arc with solid accent token (no decorative gradients)
                val activeSweep = 180f * animatedProgress.value
                if (activeSweep > 0f) {
                    drawArc(
                        color = activeColor,
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
                modifier = Modifier.padding(bottom = 6.dp)
            ) {
                Text(
                    text = "$correctCount/$totalCount",
                    style = TabularNumStyle.copy(
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
                Text(
                    text = "Correct",
                    fontSize = 14.sp,
                    color = MaterialTheme.calm.steel
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Subtext: "0 skipped • 10 incorrect"
        Text(
            text = "$skippedCount skipped • $incorrectCount incorrect",
            style = TabularNumStyle.copy(
                fontSize = 14.sp,
                color = MaterialTheme.calm.steel
            )
        )
    }
}
