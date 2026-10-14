package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.FsDomain
import com.example.model.MockExamResult
import com.example.ui.components.ArcScoreGauge
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.CorrectBadgeBg
import com.example.ui.theme.CorrectBadgeText
import com.example.ui.theme.IncorrectBadgeBg
import com.example.ui.theme.IncorrectBadgeText
import com.example.ui.theme.PrimaryBlue

@Composable
fun MockExamResultScreen(
    result: MockExamResult,
    onReviewAll: () -> Unit,
    onReviewIncorrect: () -> Unit,
    onReturnHome: () -> Unit
) {
    BackHandler { onReturnHome() }

    val durationFormatted = remember(result.timeSpentSeconds) {
        val mins = result.timeSpentSeconds / 60
        val secs = result.timeSpentSeconds % 60
        "${mins}m ${secs}s"
    }

    val avgPaceFormatted = remember(result.timeSpentSeconds, result.totalQuestions) {
        if (result.totalQuestions > 0) {
            val paceSecs = result.timeSpentSeconds / result.totalQuestions
            "${paceSecs}s / question"
        } else "0s"
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onReturnHome, modifier = Modifier.testTag("result_close_btn")) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                Text(
                    text = "Mock Exam Diagnostic Report",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.width(48.dp))
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            // Pass / Fail Prediction Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (result.isPassing) Color(0xFF132B1E) else Color(0xFF33161A))
                    .border(
                        1.5.dp,
                        if (result.isPassing) Color(0xFF22C55E) else Color(0xFFEF4444),
                        RoundedCornerShape(16.dp)
                    )
                    .padding(20.dp)
                    .testTag("mock_status_banner")
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (result.isPassing) Icons.Default.CheckCircle else Icons.Default.Error,
                        contentDescription = null,
                        tint = if (result.isPassing) Color(0xFF4ADE80) else Color(0xFFF87171),
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = if (result.isPassing) "SIMULATED EXAM PASSED" else "NEEDS REVIEW (BELOW 70%)",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (result.isPassing) Color(0xFF4ADE80) else Color(0xFFF87171)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (result.isPassing)
                                "Score: ${result.scorePercentage}% • You exceeded the NCEES cut score threshold."
                            else
                                "Score: ${result.scorePercentage}% • Focus on targeted domain drills to reach the 70% threshold.",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Arc Score Gauge
            ArcScoreGauge(
                correctCount = result.correctCount,
                totalCount = result.totalQuestions,
                skippedCount = 0,
                incorrectCount = result.totalQuestions - result.correctCount
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Time & Pacing Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
                        .padding(14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Total Time", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = durationFormatted, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    }
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
                        .padding(14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Average Pace", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = avgPaceFormatted, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Domain-by-Domain Diagnostic Breakdown
            Text(
                text = "Score Breakdown by Domain",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Performance across all NCEES exam specifications",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(14.dp))

            result.domainScores.forEach { (domain, score) ->
                val correct = score.first
                val total = score.second
                val pct = if (total > 0) ((correct.toFloat() / total) * 100).toInt() else 0

                val statusLabel = when {
                    pct >= 70 -> "Proficient"
                    pct >= 50 -> "Borderline"
                    else -> "Needs Work"
                }

                val statusColor = when {
                    pct >= 70 -> Color(0xFF22C55E)
                    pct >= 50 -> Color(0xFFF59E0B)
                    else -> Color(0xFFEF4444)
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp))
                        .padding(14.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = domain.title,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "$correct / $total ($pct%)",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = statusColor
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        LinearProgressIndicator(
                            progress = { (pct.toFloat() / 100f).coerceIn(0f, 1f) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = statusColor,
                            trackColor = Color(0xFF282B33)
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Status: $statusLabel",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = statusColor
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Action Buttons
            Button(
                onClick = onReviewAll,
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("btn_review_all_mock")
            ) {
                Text("Review All Questions & Explanations", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (result.correctCount < result.totalQuestions) {
                OutlinedButton(
                    onClick = onReviewIncorrect,
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("btn_review_incorrect_mock")
                ) {
                    Text("Review Incorrect Questions Only (${result.totalQuestions - result.correctCount})", fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            Button(
                onClick = onReturnHome,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("btn_return_home_mock")
            ) {
                Text("Return to Dashboard", fontSize = 15.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
