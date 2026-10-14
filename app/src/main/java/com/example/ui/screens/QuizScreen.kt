package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.CorrectBadgeBg
import com.example.ui.theme.CorrectBadgeText
import com.example.ui.theme.CorrectOptionBorder
import com.example.ui.theme.IncorrectBadgeBg
import com.example.ui.theme.IncorrectBadgeText
import com.example.ui.theme.IncorrectOptionBorder
import com.example.ui.theme.PrimaryBlue

@Composable
fun QuizScreen(
    questions: List<Question>,
    initialAnswers: Map<String, Int> = emptyMap(),
    isReviewMode: Boolean = false,
    onFinishQuiz: (answers: Map<String, Int>) -> Unit,
    onClose: () -> Unit
) {
    if (questions.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No questions available", color = MaterialTheme.colorScheme.onBackground)
        }
        return
    }

    var currentIndex by remember { mutableIntStateOf(0) }
    val userAnswers = remember {
        mutableStateMapOf<String, Int>().apply {
            putAll(initialAnswers)
        }
    }

    val currentQuestion = questions.getOrElse(currentIndex) { questions.first() }
    val selectedOption = userAnswers[currentQuestion.id]

    // Calculate live tally of correct and incorrect so far
    var correctCount = 0
    var incorrectCount = 0
    questions.forEach { q ->
        val ans = userAnswers[q.id]
        if (ans != null) {
            if (ans == q.correctIndex) correctCount++ else incorrectCount++
        }
    }

    BackHandler {
        if (isReviewMode) onClose() else onFinishQuiz(userAnswers)
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                // Header Row with Close, Progress Bar, Position, and Tally Badges
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = onClose,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("quiz_close_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    // Progress Track with dot/indicator
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
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp)),
                            color = PrimaryBlue,
                            trackColor = Color(0xFF282A30)
                        )
                    }

                    // Position counter "1 / 48"
                    Text(
                        text = "${currentIndex + 1} / ${questions.size}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(end = 10.dp)
                    )

                    // Badges: Red Incorrect and Green Correct
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Incorrect Pill Badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(IncorrectBadgeBg)
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "✕",
                                    color = IncorrectBadgeText,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "$incorrectCount",
                                    color = IncorrectBadgeText,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        // Correct Pill Badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(CorrectBadgeBg)
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "✓",
                                    color = CorrectBadgeText,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "$correctCount",
                                    color = CorrectBadgeText,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
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
            // Question Label e.g. "Question 1"
            Text(
                text = "Question ${currentIndex + 1}",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Domain chip badge
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

            Spacer(modifier = Modifier.height(14.dp))

            // Question statement text
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
                val isAnswered = (selectedOption != null)
                val isCorrect = (index == currentQuestion.correctIndex)

                val showAsCorrect = isAnswered && isCorrect
                val showAsIncorrect = isAnswered && isSelected && !isCorrect

                val cardBg = when {
                    showAsCorrect -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
                    showAsIncorrect -> IncorrectBadgeBg.copy(alpha = 0.5f)
                    isSelected -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)
                    else -> MaterialTheme.colorScheme.surface
                }

                val borderColor = when {
                    showAsCorrect -> CorrectOptionBorder
                    showAsIncorrect -> IncorrectOptionBorder
                    isSelected -> PrimaryBlue
                    else -> MaterialTheme.colorScheme.outline
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(cardBg)
                        .border(1.dp, borderColor, RoundedCornerShape(14.dp))
                        .clickable(enabled = !isAnswered || isReviewMode) {
                            if (!isAnswered) {
                                userAnswers[currentQuestion.id] = index
                            }
                        }
                        .padding(16.dp)
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
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(end = 12.dp)
                            )

                            // Option text
                            Text(
                                text = optionText,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.weight(1f)
                            )

                            // Status badge on the right
                            if (isSelected && !showAsCorrect && !showAsIncorrect) {
                                Text(
                                    text = "(Your answer)",
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                            }

                            if (showAsCorrect) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (isSelected) {
                                        Text(
                                            text = "(Your answer)",
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.padding(end = 6.dp)
                                        )
                                    }
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(CorrectBadgeBg)
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = null,
                                                tint = CorrectBadgeText,
                                                modifier = Modifier.size(12.dp)
                                            )
                                            Spacer(modifier = Modifier.width(3.dp))
                                            Text(
                                                text = "Correct answer",
                                                color = CorrectBadgeText,
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
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(end = 6.dp)
                                    )
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(IncorrectBadgeBg)
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = "✕ Incorrect",
                                            color = IncorrectBadgeText,
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
                            Column(modifier = Modifier.padding(top = 12.dp, start = 2.dp)) {
                                Text(
                                    text = currentQuestion.explanation,
                                    fontSize = 14.sp,
                                    lineHeight = 20.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                if (currentQuestion.formulaHighlight != null && isCorrect) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(MaterialTheme.colorScheme.secondaryContainer)
                                            .padding(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Text(
                                            text = currentQuestion.formulaHighlight,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = MaterialTheme.colorScheme.onSecondaryContainer
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Bottom Navigation Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back Button
                Button(
                    onClick = {
                        if (currentIndex > 0) currentIndex--
                    },
                    enabled = currentIndex > 0,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                    ),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .size(width = 110.dp, height = 48.dp)
                        .testTag("quiz_back_button")
                ) {
                    Text("Back", fontSize = 15.sp, fontWeight = FontWeight.Medium)
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Next Button
                Button(
                    onClick = {
                        if (currentIndex < questions.size - 1) {
                            currentIndex++
                        } else {
                            onFinishQuiz(userAnswers)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryBlue,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .size(width = 110.dp, height = 48.dp)
                        .testTag("quiz_next_button")
                ) {
                    Text(
                        text = if (currentIndex < questions.size - 1) "Next" else "Finish",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
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
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
