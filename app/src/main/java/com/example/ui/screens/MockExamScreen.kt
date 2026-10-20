package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import com.example.ui.theme.TabularNumStyle
import com.example.ui.theme.calm
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MockExamScreen(
    questions: List<Question>,
    timeLimitMinutes: Int = 90,
    onSubmitExam: (MockExamResult) -> Unit,
    onCancelExam: () -> Unit
) {
    BackHandler {
        // Trigger quit confirmation instead of immediate back
    }

    var currentIndex by remember { mutableIntStateOf(0) }
    val userAnswers = remember { mutableStateMapOf<String, Int>() }
    val flaggedQuestionIds = remember { mutableStateOf(setOf<String>()) }

    var remainingSeconds by remember { mutableIntStateOf(timeLimitMinutes * 60) }
    var isTimerRunning by remember { mutableStateOf(true) }

    var showPaletteSheet by remember { mutableStateOf(false) }
    val paletteSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var showSubmitDialog by remember { mutableStateOf(false) }
    var showExitDialog by remember { mutableStateOf(false) }

    // Countdown Timer Coroutine
    LaunchedEffect(isTimerRunning) {
        while (isTimerRunning && remainingSeconds > 0) {
            delay(1000)
            remainingSeconds--
            if (remainingSeconds <= 0) {
                isTimerRunning = false
                val timeSpent = (timeLimitMinutes * 60) - remainingSeconds
                submitExamInternal(questions, userAnswers, flaggedQuestionIds.value, timeSpent, onSubmitExam)
            }
        }
    }

    val currentQuestion = questions.getOrElse(currentIndex) { questions.first() }
    val selectedOption = userAnswers[currentQuestion.id]
    val isFlagged = flaggedQuestionIds.value.contains(currentQuestion.id)

    val answeredCount = userAnswers.size
    val flaggedCount = flaggedQuestionIds.value.size
    val unansweredCount = questions.size - answeredCount

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
        containerColor = MaterialTheme.calm.bg,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.calm.surface)
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
                            tint = MaterialTheme.calm.steel
                        )
                    }

                    // Timer Pill (Muted container, tabular figures)
                    val isUrgent = remainingSeconds < 300
                    val timerBg = if (isUrgent) MaterialTheme.calm.warnContainer else MaterialTheme.calm.surface
                    val timerBorder = if (isUrgent) MaterialTheme.calm.warn else MaterialTheme.calm.divider
                    val timerColor = if (isUrgent) MaterialTheme.calm.warn else MaterialTheme.calm.ink

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(timerBg)
                            .border(1.dp, timerBorder, RoundedCornerShape(6.dp))
                            .padding(horizontal = 12.dp, vertical = 5.dp)
                            .testTag("exam_timer_display")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = null,
                                tint = timerColor,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = timeFormatted,
                                style = TabularNumStyle.copy(
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = timerColor
                                )
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
                                tint = if (isFlagged) MaterialTheme.calm.warn else MaterialTheme.calm.steel
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
                                tint = MaterialTheme.calm.steel
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
                        color = MaterialTheme.calm.steel
                    )
                    Text(
                        text = "Question ${currentIndex + 1} of ${questions.size} • Answered: ${userAnswers.size}/${questions.size}",
                        style = TabularNumStyle.copy(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.calm.accentBright
                        )
                    )
                }
            }
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.calm.surface)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Previous
                OutlinedButton(
                    onClick = { if (currentIndex > 0) currentIndex-- },
                    enabled = currentIndex > 0,
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, MaterialTheme.calm.divider),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.calm.ink,
                        disabledContentColor = MaterialTheme.calm.steel.copy(alpha = 0.4f)
                    ),
                    modifier = Modifier.testTag("mock_prev_btn")
                ) {
                    Text("Previous", fontSize = 14.sp)
                }

                // Submit Button
                Button(
                    onClick = { showSubmitDialog = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.calm.accent,
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp),
                    shape = RoundedCornerShape(8.dp),
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
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.calm.accent,
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("mock_next_btn")
                ) {
                    Text(
                        if (currentIndex < questions.size - 1) "Next" else "Review",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            // Flagged banner if active
            if (isFlagged) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(MaterialTheme.calm.warnContainer)
                        .border(1.dp, MaterialTheme.calm.warn.copy(alpha = 0.3f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Flag,
                            contentDescription = null,
                            tint = MaterialTheme.calm.warn,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Flagged for review — return to this question before submitting",
                            color = MaterialTheme.calm.warn,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
            }

            // Question Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Question ${currentIndex + 1} of ${questions.size}",
                    style = TabularNumStyle.copy(
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.calm.steel
                    )
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(MaterialTheme.calm.surface)
                        .border(1.dp, MaterialTheme.calm.divider, RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = currentQuestion.domain.title,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.calm.steel
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Question Prompt (min 16sp)
            Text(
                text = currentQuestion.questionText,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 25.sp,
                color = MaterialTheme.calm.ink,
                modifier = Modifier.testTag("mock_question_text")
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Options List
            val optionLetters = listOf("A", "B", "C", "D")
            currentQuestion.options.forEachIndexed { index, optionText ->
                val letter = optionLetters.getOrElse(index) { "${index + 1}" }
                val isSelected = (selectedOption == index)

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            if (isSelected) MaterialTheme.calm.accentContainer
                            else MaterialTheme.calm.surface
                        )
                        .border(
                            1.dp,
                            if (isSelected) MaterialTheme.calm.accent else MaterialTheme.calm.divider,
                            RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            userAnswers[currentQuestion.id] = index
                        }
                        .padding(14.dp)
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
                            color = MaterialTheme.calm.ink,
                            modifier = Modifier.padding(end = 10.dp)
                        )
                        Text(
                            text = optionText,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            lineHeight = 22.sp,
                            color = MaterialTheme.calm.ink,
                            modifier = Modifier.weight(1f)
                        )
                        if (isSelected) {
                            Text(
                                text = "Selected",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.calm.accentBright
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
            containerColor = MaterialTheme.calm.surface,
            shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Question Palette",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.calm.ink
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Tap any question to jump directly to it",
                    fontSize = 13.sp,
                    color = MaterialTheme.calm.steel
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Palette Legend
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    LegendItem(color = MaterialTheme.calm.accent, label = "Answered")
                    LegendItem(color = MaterialTheme.calm.divider, label = "Unanswered")
                    LegendItem(color = MaterialTheme.calm.warn, label = "Flagged")
                }

                Spacer(modifier = Modifier.height(16.dp))

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
                            isCurrent -> MaterialTheme.calm.accent
                            isAnswered -> MaterialTheme.calm.accentContainer
                            else -> MaterialTheme.calm.surface
                        }
                        val border = when {
                            isQFlagged -> MaterialTheme.calm.warn
                            isCurrent -> MaterialTheme.calm.accent
                            isAnswered -> MaterialTheme.calm.accent.copy(alpha = 0.5f)
                            else -> MaterialTheme.calm.divider
                        }
                        val textCol = when {
                            isCurrent -> Color.White
                            isAnswered -> MaterialTheme.calm.accentBright
                            else -> MaterialTheme.calm.steel
                        }

                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(bg)
                                .border(
                                    1.dp,
                                    border,
                                    RoundedCornerShape(6.dp)
                                )
                                .clickable {
                                    currentIndex = idx
                                    showPaletteSheet = false
                                }
                                .testTag("palette_q_${idx + 1}"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${idx + 1}",
                                style = TabularNumStyle.copy(
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = textCol
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))
            }
        }
    }

    // Submit Early Confirmation Dialog
    if (showSubmitDialog) {
        AlertDialog(
            onDismissRequest = { showSubmitDialog = false },
            title = {
                Text("Submit Mock Exam?", fontWeight = FontWeight.Bold, color = MaterialTheme.calm.ink)
            },
            text = {
                Column {
                    Text(
                        "You have completed $answeredCount of ${questions.size} questions.",
                        color = MaterialTheme.calm.ink,
                        fontSize = 14.sp
                    )
                    if (unansweredCount > 0) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "⚠️ $unansweredCount questions are unanswered.",
                            color = MaterialTheme.calm.warn,
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp
                        )
                    }
                    if (flaggedCount > 0) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "🚩 $flaggedCount questions are flagged for review.",
                            color = MaterialTheme.calm.steel,
                            fontSize = 13.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Are you ready to submit and view your domain diagnostic score report?",
                        color = MaterialTheme.calm.steel,
                        fontSize = 13.sp
                    )
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
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.calm.accent,
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("confirm_submit_btn")
                ) {
                    Text("Submit", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showSubmitDialog = false }) {
                    Text("Keep Testing", color = MaterialTheme.calm.steel)
                }
            },
            containerColor = MaterialTheme.calm.surface,
            shape = RoundedCornerShape(8.dp)
        )
    }

    // Exit Early Confirmation Dialog
    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("Quit Mock Exam?", fontWeight = FontWeight.Bold, color = MaterialTheme.calm.ink) },
            text = { Text("Your exam progress will be lost if you exit without submitting.", color = MaterialTheme.calm.steel, fontSize = 14.sp) },
            confirmButton = {
                Button(
                    onClick = {
                        showExitDialog = false
                        onCancelExam()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.calm.warn,
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Quit Exam", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) {
                    Text("Continue Exam", color = MaterialTheme.calm.steel)
                }
            },
            containerColor = MaterialTheme.calm.surface,
            shape = RoundedCornerShape(8.dp)
        )
    }
}

private fun submitExamInternal(
    questions: List<Question>,
    answers: Map<String, Int>,
    flags: Set<String>,
    timeSpentSeconds: Int,
    onSubmitExam: (MockExamResult) -> Unit
) {
    val totalCount = questions.size
    val correctCount = questions.count { q ->
        answers[q.id] == q.correctIndex
    }
    val scorePct = if (totalCount > 0) (correctCount * 100) / totalCount else 0

    // Compute domain breakdown
    val domainScores = mutableMapOf<FsDomain, Pair<Int, Int>>()
    questions.forEach { q ->
        val current = domainScores.getOrDefault(q.domain, Pair(0, 0))
        val isCorrect = (answers[q.id] == q.correctIndex)
        domainScores[q.domain] = Pair(
            current.first + (if (isCorrect) 1 else 0),
            current.second + 1
        )
    }

    val result = MockExamResult(
        id = "mock_${System.currentTimeMillis()}",
        timestamp = System.currentTimeMillis(),
        totalQuestions = totalCount,
        correctCount = correctCount,
        scorePercentage = scorePct,
        timeSpentSeconds = timeSpentSeconds,
        isPassing = scorePct >= 70,
        domainScores = domainScores,
        userAnswers = answers,
        flaggedQuestionIds = flags
    )
    onSubmitExam(result)
}

@Composable
private fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(color)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            color = MaterialTheme.calm.steel
        )
    }
}
