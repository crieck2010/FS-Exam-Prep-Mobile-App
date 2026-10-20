package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
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
import com.example.model.Question
import com.example.ui.theme.TabularNumStyle
import com.example.ui.theme.calm

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    questions: List<Question>,
    initialAnswers: Map<String, Int> = emptyMap(),
    isReviewMode: Boolean = false,
    onFinishQuiz: (userAnswers: Map<String, Int>) -> Unit,
    onClose: () -> Unit
) {
    if (questions.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No questions available for this selection.",
                color = MaterialTheme.calm.steel
            )
        }
        return
    }

    var currentIndex by remember { mutableIntStateOf(0) }
    val userAnswers = remember { mutableStateMapOf<String, Int>().apply { putAll(initialAnswers) } }

    val currentQuestion = questions.getOrElse(currentIndex) { questions.first() }
    val selectedOption = userAnswers[currentQuestion.id]

    // Calculate score tallies so far
    val answeredCount = userAnswers.size
    val correctCount = questions.count { q ->
        userAnswers[q.id] == q.correctIndex
    }
    val incorrectCount = questions.count { q ->
        val ans = userAnswers[q.id]
        ans != null && ans != q.correctIndex
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
                    IconButton(
                        onClick = onClose,
                        modifier = Modifier.testTag("back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.calm.steel
                        )
                    }
                },
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Progress Track
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 12.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            LinearProgressIndicator(
                                progress = { ((currentIndex + 1).toFloat() / questions.size).coerceIn(0f, 1f) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(3.dp)
                                    .clip(RoundedCornerShape(2.dp)),
                                color = MaterialTheme.calm.accentBright,
                                trackColor = MaterialTheme.calm.divider
                            )
                        }

                        // Position counter "1 / 48"
                        Text(
                            text = "${currentIndex + 1} / ${questions.size}",
                            style = TabularNumStyle.copy(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.calm.steel
                            ),
                            modifier = Modifier.padding(end = 10.dp)
                        )

                        // Badges: Muted Incorrect and Correct counts
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Incorrect Pill Badge
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(MaterialTheme.calm.warnContainer)
                                    .border(1.dp, MaterialTheme.calm.warn.copy(alpha = 0.3f), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 8.dp, vertical = 3.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "✕",
                                        color = MaterialTheme.calm.warn,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(
                                        text = "$incorrectCount",
                                        style = TabularNumStyle.copy(
                                            color = MaterialTheme.calm.warn,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    )
                                }
                            }

                            // Correct Pill Badge
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(MaterialTheme.calm.goContainer)
                                    .border(1.dp, MaterialTheme.calm.go.copy(alpha = 0.3f), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 8.dp, vertical = 3.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "✓",
                                        color = MaterialTheme.calm.go,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(
                                        text = "$correctCount",
                                        style = TabularNumStyle.copy(
                                            color = MaterialTheme.calm.go,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    )
                                }
                            }
                        }
                    }
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
            // Question Label e.g. "Question 1"
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Question ${currentIndex + 1}",
                    style = TabularNumStyle.copy(
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.calm.steel
                    ),
                    modifier = Modifier.testTag("question_index_label")
                )

                // Domain Tag Pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(MaterialTheme.calm.surface)
                        .border(1.dp, MaterialTheme.calm.divider, RoundedCornerShape(6.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
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

            // Main Question Text Prompt (min 16sp)
            Text(
                text = currentQuestion.questionText,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 25.sp,
                color = MaterialTheme.calm.ink,
                modifier = Modifier.testTag("question_prompt")
            )

            // Optional Sub-diagram or Formula Highlight Box
            if (currentQuestion.formulaHighlight != null && selectedOption == null) {
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(MaterialTheme.calm.surface)
                        .border(1.dp, MaterialTheme.calm.divider, RoundedCornerShape(6.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        text = "Formula: ${currentQuestion.formulaHighlight}",
                        fontSize = 14.sp,
                        color = MaterialTheme.calm.steel,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Options List
            val optionLetters = listOf("A", "B", "C", "D")
            currentQuestion.options.forEachIndexed { index, optionText ->
                val letter = optionLetters.getOrElse(index) { "${index + 1}" }
                val isSelected = (selectedOption == index)
                val isAnswered = (selectedOption != null)
                val isCorrect = (index == currentQuestion.correctIndex)
                val showAsCorrect = isAnswered && isCorrect
                val showAsIncorrect = isAnswered && isSelected && !isCorrect

                val cardBg = when {
                    showAsCorrect -> MaterialTheme.calm.goContainer
                    showAsIncorrect -> MaterialTheme.calm.warnContainer
                    isSelected -> MaterialTheme.calm.accentContainer
                    else -> MaterialTheme.calm.surface
                }
                val borderColor = when {
                    showAsCorrect -> MaterialTheme.calm.go
                    showAsIncorrect -> MaterialTheme.calm.warn
                    isSelected -> MaterialTheme.calm.accent
                    else -> MaterialTheme.calm.divider
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(cardBg)
                        .border(1.dp, borderColor, RoundedCornerShape(8.dp))
                        .clickable(enabled = !isAnswered || isReviewMode) {
                            if (!isAnswered) {
                                userAnswers[currentQuestion.id] = index
                            }
                        }
                        .padding(14.dp)
                        .testTag("option_${letter.lowercase()}")
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Letter prefix e.g. "A."
                            Text(
                                text = "$letter.",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.calm.ink,
                                modifier = Modifier.padding(end = 10.dp)
                            )
                            // Option text (16sp per Brand Guide)
                            Text(
                                text = optionText,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal,
                                lineHeight = 22.sp,
                                color = MaterialTheme.calm.ink,
                                modifier = Modifier.weight(1f)
                            )

                            // Status badge on the right
                            if (isSelected && !showAsCorrect && !showAsIncorrect) {
                                Text(
                                    text = "(Your answer)",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.calm.steel,
                                    modifier = Modifier.padding(end = 6.dp)
                                )
                            }

                            if (showAsCorrect) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (isSelected) {
                                        Text(
                                            text = "(Your answer)",
                                            fontSize = 12.sp,
                                            color = MaterialTheme.calm.steel,
                                            modifier = Modifier.padding(end = 6.dp)
                                        )
                                    }
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(MaterialTheme.calm.goContainer)
                                            .border(1.dp, MaterialTheme.calm.go.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                                            .padding(horizontal = 8.dp, vertical = 3.dp)
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = null,
                                                tint = MaterialTheme.calm.go,
                                                modifier = Modifier.size(12.dp)
                                            )
                                            Spacer(modifier = Modifier.width(3.dp))
                                            Text(
                                                text = "Correct answer",
                                                color = MaterialTheme.calm.go,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }
                                    }
                                }
                            } else if (showAsIncorrect) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "(Your answer)",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.calm.steel,
                                        modifier = Modifier.padding(end = 6.dp)
                                    )
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(MaterialTheme.calm.warnContainer)
                                            .border(1.dp, MaterialTheme.calm.warn.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                                            .padding(horizontal = 8.dp, vertical = 3.dp)
                                    ) {
                                        Text(
                                            text = "✕ Incorrect",
                                            color = MaterialTheme.calm.warn,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                            }
                        }

                        // Detailed explanation embedded directly beneath chosen/correct option
                        AnimatedVisibility(
                            visible = isAnswered && (isSelected || isCorrect),
                            enter = fadeIn()
                        ) {
                            Column(modifier = Modifier.padding(top = 10.dp, start = 2.dp)) {
                                Text(
                                    text = currentQuestion.explanation,
                                    fontSize = 14.sp,
                                    lineHeight = 20.sp,
                                    color = MaterialTheme.calm.steel
                                )
                                if (currentQuestion.formulaHighlight != null && isCorrect) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(MaterialTheme.calm.accentContainer)
                                            .padding(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Text(
                                            text = currentQuestion.formulaHighlight,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = MaterialTheme.calm.accentBright
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Bottom Navigation Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back Button (Secondary: Ink text, Divider outline, 8dp radius)
                OutlinedButton(
                    onClick = {
                        if (currentIndex > 0) currentIndex--
                    },
                    enabled = currentIndex > 0,
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, MaterialTheme.calm.divider),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.calm.ink,
                        disabledContentColor = MaterialTheme.calm.steel.copy(alpha = 0.4f)
                    ),
                    modifier = Modifier
                        .size(width = 110.dp, height = 46.dp)
                        .testTag("quiz_back_button")
                ) {
                    Text("Back", fontSize = 15.sp, fontWeight = FontWeight.Medium)
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Next Button (Primary: Accent fill, white text, 8dp radius, no elevation)
                Button(
                    onClick = {
                        if (currentIndex < questions.size - 1) {
                            currentIndex++
                        } else {
                            onFinishQuiz(userAnswers)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.calm.accent,
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .size(width = 110.dp, height = 46.dp)
                        .testTag("quiz_next_button")
                ) {
                    Text(
                        text = if (currentIndex < questions.size - 1) "Next" else "Finish",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // "Back to score summary" link
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                TextButton(
                    onClick = { onFinishQuiz(userAnswers) },
                    modifier = Modifier.testTag("back_to_summary_link")
                ) {
                    Text(
                        text = "Back to score summary",
                        color = MaterialTheme.calm.steel,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
