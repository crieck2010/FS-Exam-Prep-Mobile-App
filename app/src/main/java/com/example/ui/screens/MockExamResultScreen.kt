package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import com.example.model.MockExamResult
import com.example.ui.components.ArcScoreGauge
import com.example.ui.theme.TabularNumStyle
import com.example.ui.theme.calm

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MockExamResultScreen(
    result: MockExamResult,
    onReviewAll: () -> Unit,
    onReviewIncorrect: () -> Unit,
    onReturnHome: () -> Unit
) {
    val durationFormatted = remember(result.timeSpentSeconds) {
        val mins = result.timeSpentSeconds / 60
        val secs = result.timeSpentSeconds % 60
        String.format("%dm %02ds", mins, secs)
    }

    val avgPaceFormatted = remember(result.timeSpentSeconds, result.totalQuestions) {
        if (result.totalQuestions > 0) {
            val pace = result.timeSpentSeconds / result.totalQuestions
            String.format("%dm %02ds/q", pace / 60, pace % 60)
        } else "0s"
    }

    Scaffold(
        containerColor = MaterialTheme.calm.bg,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.calm.surface,
                    titleContentColor = MaterialTheme.calm.ink
                ),
                navigationIcon = {
                    IconButton(onClick = onReturnHome) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Return",
                            tint = MaterialTheme.calm.steel
                        )
                    }
                },
                title = {
                    Text(
                        text = "Mock Exam Diagnostic Report",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.calm.ink
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            // Pass / Fail Prediction Banner (Muted Go & Warn)
            val bannerBg = if (result.isPassing) MaterialTheme.calm.goContainer else MaterialTheme.calm.warnContainer
            val bannerBorder = if (result.isPassing) MaterialTheme.calm.go.copy(alpha = 0.4f) else MaterialTheme.calm.warn.copy(alpha = 0.4f)
            val bannerColor = if (result.isPassing) MaterialTheme.calm.go else MaterialTheme.calm.warn

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(bannerBg)
                    .border(1.dp, bannerBorder, RoundedCornerShape(8.dp))
                    .padding(16.dp)
                    .testTag("mock_status_banner")
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (result.isPassing) Icons.Default.CheckCircle else Icons.Default.Error,
                        contentDescription = null,
                        tint = bannerColor,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = if (result.isPassing) "SIMULATED EXAM PASSED" else "NEEDS REVIEW (BELOW 70%)",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = bannerColor
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (result.isPassing)
                                "Score: ${result.scorePercentage}% • A passing-level performance."
                            else
                                "Score: ${result.scorePercentage}% • Focus on targeted domain drills to reach the 70% threshold.",
                            style = TabularNumStyle.copy(
                                fontSize = 13.sp,
                                color = MaterialTheme.calm.steel
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Arc Score Gauge
            ArcScoreGauge(
                correctCount = result.correctCount,
                totalCount = result.totalQuestions,
                skippedCount = 0,
                incorrectCount = result.totalQuestions - result.correctCount
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Time & Pacing Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.calm.surface)
                        .border(1.dp, MaterialTheme.calm.divider, RoundedCornerShape(8.dp))
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Total Time", fontSize = 12.sp, color = MaterialTheme.calm.steel)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = durationFormatted,
                            style = TabularNumStyle.copy(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.calm.ink
                            )
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.calm.surface)
                        .border(1.dp, MaterialTheme.calm.divider, RoundedCornerShape(8.dp))
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Average Pace", fontSize = 12.sp, color = MaterialTheme.calm.steel)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = avgPaceFormatted,
                            style = TabularNumStyle.copy(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.calm.ink
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Domain-by-Domain Diagnostic Breakdown
            Text(
                text = "Score Breakdown by Domain",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.calm.ink
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Performance across all FS exam domains",
                fontSize = 13.sp,
                color = MaterialTheme.calm.steel
            )

            Spacer(modifier = Modifier.height(12.dp))

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
                    pct >= 70 -> MaterialTheme.calm.go
                    pct >= 50 -> MaterialTheme.calm.accentBright
                    else -> MaterialTheme.calm.warn
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.calm.surface)
                        .border(1.dp, MaterialTheme.calm.divider, RoundedCornerShape(8.dp))
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
                                color = MaterialTheme.calm.ink,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "$correct / $total ($pct%)",
                                style = TabularNumStyle.copy(
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = statusColor
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        LinearProgressIndicator(
                            progress = { (pct.toFloat() / 100f).coerceIn(0f, 1f) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(3.dp)
                                .clip(RoundedCornerShape(2.dp)),
                            color = statusColor,
                            trackColor = MaterialTheme.calm.divider
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

            Spacer(modifier = Modifier.height(28.dp))

            // Action Buttons
            Button(
                onClick = onReviewAll,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.calm.accent,
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("btn_review_all_mock")
            ) {
                Text("Review All Questions & Explanations", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (result.correctCount < result.totalQuestions) {
                OutlinedButton(
                    onClick = onReviewIncorrect,
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, MaterialTheme.calm.divider),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.calm.ink
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("btn_review_incorrect_mock")
                ) {
                    Text(
                        "Review Incorrect Questions Only (${result.totalQuestions - result.correctCount})",
                        style = TabularNumStyle.copy(fontSize = 14.sp)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            OutlinedButton(
                onClick = onReturnHome,
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, MaterialTheme.calm.divider),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.calm.ink
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("btn_return_home_mock")
            ) {
                Text("Return to Dashboard", fontSize = 15.sp)
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}
