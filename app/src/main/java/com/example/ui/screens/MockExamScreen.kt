package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.OutlinedFlag
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.model.Question
import com.example.ui.theme.AccentAmber
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.PrimaryBlue
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MockExamScreen(
    questions: List<Question>,
    timeLimitMinutes: Int = 90,
    onSubmitExam: (result: MockExamResult) -> Unit,
    onCancelExam: () -> Unit
) {
    if (questions.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No exam questions loaded", color = MaterialTheme.colorScheme.onBackground)
        }
        return
    }

    var currentIndex by remember { mutableIntStateOf(0) }
    val userAnswers = remember { mutableStateMapOf<String, Int>() }
    val flaggedQuestionIds = remember { mutableStateOf(setOf<String>()) }
    var remainingSeconds by remember { mutableIntStateOf(timeLimitMinutes * 60) }
    var isTimerRunning by remember { mutableStateOf(true) }

    var showSubmitDialog by remember { mutableStateOf(false) }
    var showPaletteSheet by remember { mutableStateOf(false) }
    var showExitDialog by remember { mutableStateOf(false) }

    val paletteSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Countdown Timer Loop
    LaunchedEffect(isTimerRunning) {
        while (isTimerRunning && remainingSeconds > 0) {
            delay(1000L)
            remainingSeconds--
            if (remainingSeconds <= 0) {
                // Auto-submit on time expiration
                isTimerRunning = false
                showSubmitDialog = false
                submitExamInternal(questions, userAnswers, flaggedQuestionIds.value, timeLimitMinutes * 60, onSubmitExam)
            }
        }
    }

    BackHandler {
        showExitDialog = true
    }

    val currentQuestion = questions.getOrElse(currentIndex) { questions.first() }
    val isFlagged = flaggedQuestionIds.value.contains(currentQuestion.id)
    val selectedOption = userAnswers[currentQuestion.id]

    // Formatted Time String HH:MM:SS
    val timeFormatted = remember(remainingSeconds) {
        val hours = remainingSeconds / 3600
        val mins = (remainingSeconds % 3600) / 60
        val secs = remainingSeconds % 60
        if (hours > 0) {
            String.format("%d:%02d:%02d", hours, mins, secs)
        } else {
            String.format("%02d:%02d", mins, secs)
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF16181D))
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Close / Exit
                    IconButton(
                        onClick = { showExitDialog = true },
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("mock_exit_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Exit Exam",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Timer Pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (remainingSeconds < 300) Color(0xFF3B151A) else Color(0xFF1E2638))
                            .border(
                                1.dp,
                                if (remainingSeconds < 300) Color(0xFFEF4444) else Color(0xFF3B82F6),
                                RoundedCornerShape(16.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                            .testTag("exam_timer_display")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = null,
                                tint = if (remainingSeconds < 300) Color(0xFFF87171) else AccentCyan,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = timeFormatted,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (remainingSeconds < 300) Color(0xFFF87171) else Color.White
                            )
                        }
                    }

                    // Question Palette & Flag Buttons
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Flag button
                        IconButton(
                            onClick = {
                                val current = flaggedQuestionIds.value
                                flaggedQuestionIds.value = if (isFlagged) {
                                    current - currentQuestion.id
                                } else {
                                    current + currentQuestion.id
                                }
                            },
                            modifier = Modifier
                                .size(36.dp)
                                .testTag("mock_flag_btn")
                        ) {
                            Icon(
                                imageVector = if (isFlagged) Icons.Default.Flag else Icons.Default.OutlinedFlag,
                                contentDescription = "Flag for review",
                                tint = if (isFlagged) AccentAmber else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.width(4.dp))

                        // Grid Palette Button
                        IconButton(
                            onClick = { showPaletteSheet = true },
                            modifier = Modifier
                                .size(36.dp)
                                .testTag("mock_palette_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.GridView,
                                contentDescription = "Question Palette",
                                tint = AccentCyan
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // CBT Exam Indicator Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "NCEES FS CBT Simulation",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Question ${currentIndex + 1} of ${questions.size} • Answered: ${userAnswers.size}/${questions.size}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = AccentCyan
                    )
                }
            }
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF16181D))
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Previous
                OutlinedButton(
                    onClick = { if (currentIndex > 0) currentIndex-- },
                    enabled = currentIndex > 0,
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.testTag("mock_prev_btn")
                ) {
                    Text("Previous", fontSize = 14.sp)
                }

                // Submit Button
                Button(
                    onClick = { showSubmitDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669)),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.testTag("mock_submit_btn")
                ) {
                    Text("Submit Exam", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                }

                // Next
                Button(
                    onClick = {
                        if (currentIndex < questions.size - 1) {
                            currentIndex++
                        } else {
                            showSubmitDialog = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.testTag("mock_next_btn")
                ) {
                    Text(if (currentIndex < questions.size - 1) "Next" else "Review", color = Color.White, fontSize = 14.sp)
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            // Flagged banner if active
            if (isFlagged) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF332310))
                        .border(1.dp, AccentAmber, RoundedCornerShape(10.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Flag,
                            contentDescription = null,
                            tint = AccentAmber,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Flagged for review — return to this question before submitting",
                            color = AccentAmber,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
            }

            // Domain Badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = currentQuestion.domain.title,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Question Statement
            Text(
                text = currentQuestion.questionText,
                fontSize = 18.sp,
                lineHeight = 26.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Options List
            val optionLetters = listOf("A", "B", "C", "D")
            currentQuestion.options.forEachIndexed { index, optionText ->
                val letter = optionLetters.getOrElse(index) { "${index + 1}" }
                val isSelected = (selectedOption == index)

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f)
                            else MaterialTheme.colorScheme.surface
                        )
                        .border(
                            1.dp,
                            if (isSelected) PrimaryBlue else MaterialTheme.colorScheme.outline,
                            RoundedCornerShape(14.dp)
                        )
                        .clickable {
                            userAnswers[currentQuestion.id] = index
                        }
                        .padding(16.dp)
                        .testTag("mock_opt_${letter.lowercase()}")
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "$letter.",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isSelected) PrimaryBlue else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(end = 12.dp)
                        )
                        Text(
                            text = optionText,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f)
                        )
                        if (isSelected) {
                            Text(
                                text = "Selected",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = PrimaryBlue
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }

    // Question Palette Modal BottomSheet
    if (showPaletteSheet) {
        ModalBottomSheet(
            onDismissRequest = { showPaletteSheet = false },
            sheetState = paletteSheetState,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .testTag("palette_sheet")
            ) {
                Text(
                    text = "Question Palette",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Tap any question to jump directly to it",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Palette Legend
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    LegendItem(color = PrimaryBlue, label = "Answered")
                    LegendItem(color = Color(0xFF2E323B), label = "Unanswered")
                    LegendItem(color = AccentAmber, label = "Flagged")
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Grid of Questions
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    questions.forEachIndexed { idx, q ->
                        val isAnswered = userAnswers.containsKey(q.id)
                        val isQFlagged = flaggedQuestionIds.value.contains(q.id)
                        val isCurrent = idx == currentIndex

                        val bg = when {
                            isCurrent -> Color(0xFF1D4ED8)
                            isAnswered -> PrimaryBlue
                            else -> Color(0xFF282B33)
                        }

                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(bg)
                                .border(
                                    if (isQFlagged) 2.dp else if (isCurrent) 2.dp else 1.dp,
                                    if (isQFlagged) AccentAmber else if (isCurrent) Color.White else MaterialTheme.colorScheme.outline,
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable {
                                    currentIndex = idx
                                    showPaletteSheet = false
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${idx + 1}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    // Submit Confirmation Dialog
    if (showSubmitDialog) {
        val answeredCount = userAnswers.size
        val unansweredCount = questions.size - answeredCount
        val flaggedCount = flaggedQuestionIds.value.size

        AlertDialog(
            onDismissRequest = { showSubmitDialog = false },
            title = {
                Text("Submit Mock Exam?", fontWeight = FontWeight.Bold)
            },
            text = {
                Column {
                    Text("You have completed $answeredCount of ${questions.size} questions.")
                    if (unansweredCount > 0) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "⚠️ $unansweredCount questions are unanswered.",
                            color = Color(0xFFF87171),
                            fontWeight = FontWeight.Medium
                        )
                    }
                    if (flaggedCount > 0) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "🚩 $flaggedCount questions are flagged for review.",
                            color = AccentAmber
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Are you ready to submit and view your domain diagnostic score report?")
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSubmitDialog = false
                        isTimerRunning = false
                        val timeSpent = (timeLimitMinutes * 60) - remainingSeconds
                        submitExamInternal(questions, userAnswers, flaggedQuestionIds.value, timeSpent, onSubmitExam)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669)),
                    modifier = Modifier.testTag("confirm_submit_btn")
                ) {
                    Text("Submit", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showSubmitDialog = false }) {
                    Text("Keep Testing")
                }
            },
            containerColor = MaterialTheme.colorScheme.surface
        )
    }

    // Exit Early Confirmation Dialog
    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("Quit Mock Exam?") },
            text = { Text("Your exam progress will be lost if you exit without submitting.") },
            confirmButton = {
                Button(
                    onClick = {
                        showExitDialog = false
                        onCancelExam()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444))
                ) {
                    Text("Quit Exam", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) {
                    Text("Continue Exam")
                }
            },
            containerColor = MaterialTheme.colorScheme.surface
        )
    }
}

private fun submitExamInternal(
    questions: List<Question>,
    answers: Map<String, Int>,
    flags: Set<String>,
    timeSpentSeconds: Int,
    onResult: (MockExamResult) -> Unit
) {
    var correct = 0
    val domainCounts = mutableMapOf<FsDomain, Pair<Int, Int>>() // domain -> (correct, total)

    questions.forEach { q ->
        val chosen = answers[q.id]
        val isCorrect = (chosen != null && chosen == q.correctIndex)
        if (isCorrect) correct++

        val current = domainCounts[q.domain] ?: Pair(0, 0)
        domainCounts[q.domain] = Pair(
            current.first + (if (isCorrect) 1 else 0),
            current.second + 1
        )
    }

    val percentage = if (questions.isNotEmpty()) ((correct.toFloat() / questions.size) * 100).toInt() else 0
    val isPassing = percentage >= 70

    val result = MockExamResult(
        id = "mock_${System.currentTimeMillis()}",
        timestamp = System.currentTimeMillis(),
        totalQuestions = questions.size,
        correctCount = correct,
        scorePercentage = percentage,
        timeSpentSeconds = timeSpentSeconds.coerceAtLeast(1),
        isPassing = isPassing,
        domainScores = domainCounts,
        userAnswers = answers,
        flaggedQuestionIds = flags
    )
    onResult(result)
}

@Composable
private fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
