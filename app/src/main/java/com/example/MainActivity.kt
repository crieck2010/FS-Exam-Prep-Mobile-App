package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.TabularNumStyle
import com.example.ui.theme.calm
import com.example.model.CompletedModule
import com.example.ui.components.QuizConfigSheet
import com.example.ui.screens.AchievementsScreen
import com.example.ui.screens.FlashcardsScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LeaderboardScreen
import com.example.ui.screens.MockExamResultScreen
import com.example.ui.screens.MockExamScreen
import com.example.ui.screens.QuizScreen
import com.example.ui.screens.ScoreSummaryScreen
import com.example.ui.screens.StudyGuideScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.FsPrepViewModel
import com.example.viewmodel.ScreenState

class MainActivity : ComponentActivity() {

    private val viewModel: FsPrepViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val themeMode by viewModel.themeMode.collectAsState()
            val isSystemDark = androidx.compose.foundation.isSystemInDarkTheme()
            val isDarkTheme = when (themeMode) {
                com.example.model.AppThemeMode.SYSTEM -> isSystemDark
                com.example.model.AppThemeMode.LIGHT -> false
                com.example.model.AppThemeMode.DARK -> true
            }

            MyApplicationTheme(darkTheme = isDarkTheme) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val currentScreen by viewModel.currentScreen.collectAsState()
                    val userProgress by viewModel.userProgress.collectAsState()
                    val completedModules by viewModel.completedModules.collectAsState()
                    val domainStats by viewModel.domainStats.collectAsState()
                    val achievements by viewModel.achievements.collectAsState()
                    val leaderboard by viewModel.leaderboard.collectAsState()
                    val userHandle by viewModel.userHandle.collectAsState()
                    val activeQuestions by viewModel.activeQuestions.collectAsState()
                    val activeAnswers by viewModel.activeAnswers.collectAsState()
                    val isReviewMode by viewModel.isReviewMode.collectAsState()
                    val lastCompletedModule by viewModel.lastCompletedModule.collectAsState()
                    val lastMockResult by viewModel.lastMockResult.collectAsState()
                    val mockQuestions by viewModel.mockQuestions.collectAsState()
                    val showConfigSheet by viewModel.showConfigSheet.collectAsState()
                    val selectedConfigDomain by viewModel.selectedConfigDomain.collectAsState()

                    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                    val bankError = com.example.data.QuestionBank.loadError

                    if (bankError != null) {
                        BankErrorScreen(errorMessage = bankError)
                    } else {
                        when (currentScreen) {
                            ScreenState.HOME -> {
                            HomeScreen(
                                userProgress = userProgress,
                                completedModules = completedModules,
                                domainStats = domainStats,
                                achievements = achievements,
                                leaderboard = leaderboard,
                                currentThemeMode = themeMode,
                                onSelectThemeMode = { newMode -> viewModel.setThemeMode(newMode) },
                                onStartDomainQuiz = { domain -> viewModel.startDomainQuiz(domain) },
                                onStartAdaptiveQuiz = { viewModel.startAdaptiveQuiz() },
                                onStartMockExam = { viewModel.startMockExam() },
                                onOpenQuizConfig = { viewModel.openQuizConfig(null) },
                                onReviewModule = { module -> viewModel.reviewCompletedModule(module) },
                                onOpenFlashcards = { viewModel.openFlashcards() },
                                onOpenStudyGuide = { viewModel.openStudyGuide() },
                                onOpenAchievements = { viewModel.openAchievements() },
                                onOpenLeaderboard = { viewModel.openLeaderboard() }
                            )
                        }

                        ScreenState.QUIZ -> {
                            QuizScreen(
                                questions = activeQuestions,
                                initialAnswers = activeAnswers,
                                isReviewMode = isReviewMode,
                                onFinishQuiz = { answers ->
                                    if (isReviewMode) {
                                        viewModel.navigateToHome()
                                    } else {
                                        viewModel.finishQuiz(answers)
                                    }
                                },
                                onClose = { viewModel.navigateToHome() }
                            )
                        }

                        ScreenState.SCORE_SUMMARY -> {
                            val moduleToDisplay = lastCompletedModule ?: completedModules.firstOrNull() ?: CompletedModule(
                                id = "fallback",
                                timestamp = System.currentTimeMillis(),
                                title = "Practice Quiz",
                                domain = null,
                                totalQuestions = 40,
                                correctCount = 30,
                                incorrectCount = 10,
                                skippedCount = 0
                            )

                            ScoreSummaryScreen(
                                module = moduleToDisplay,
                                domainStats = domainStats,
                                onReviewQuestions = { viewModel.reviewCurrentQuiz() },
                                onTakeAnotherQuiz = { viewModel.openQuizConfig(null) },
                                onOpenFlashcards = { viewModel.openFlashcards() },
                                onOpenStudyGuide = { viewModel.openStudyGuide() },
                                onNavigateHome = { viewModel.navigateToHome() }
                            )
                        }

                        ScreenState.MOCK_EXAM -> {
                            MockExamScreen(
                                questions = mockQuestions,
                                timeLimitMinutes = 320,
                                onSubmitExam = { result -> viewModel.submitMockExam(result) },
                                onCancelExam = { viewModel.navigateToHome() }
                            )
                        }

                        ScreenState.MOCK_EXAM_RESULT -> {
                            if (lastMockResult != null) {
                                MockExamResultScreen(
                                    result = lastMockResult!!,
                                    onReviewAll = { viewModel.reviewMockExam(onlyIncorrect = false) },
                                    onReviewIncorrect = { viewModel.reviewMockExam(onlyIncorrect = true) },
                                    onReturnHome = { viewModel.navigateToHome() }
                                )
                            } else {
                                viewModel.navigateToHome()
                            }
                        }

                        ScreenState.ACHIEVEMENTS -> {
                            AchievementsScreen(
                                achievements = achievements,
                                onBack = { viewModel.navigateToHome() }
                            )
                        }

                        ScreenState.LEADERBOARD -> {
                            LeaderboardScreen(
                                leaderboard = leaderboard,
                                currentUserHandle = userHandle,
                                onUpdateHandle = { newHandle -> viewModel.updateUserHandle(newHandle) },
                                onBack = { viewModel.navigateToHome() }
                            )
                        }

                        ScreenState.FLASHCARDS -> {
                            FlashcardsScreen(
                                onBack = { viewModel.navigateToHome() }
                            )
                        }

                        ScreenState.STUDY_GUIDE -> {
                            StudyGuideScreen(
                                onBack = { viewModel.navigateToHome() }
                            )
                        }
                    }

                    if (showConfigSheet) {
                        QuizConfigSheet(
                            sheetState = sheetState,
                            initialDomain = selectedConfigDomain,
                            onDismiss = { viewModel.dismissQuizConfig() },
                            onStartQuiz = { domain, count, focusGrowth, difficulty ->
                                viewModel.startConfiguredQuiz(domain, count, focusGrowth, difficulty)
                            }
                        )
                    }
                }
            }
        }
    }
}
}

@Composable
private fun BankErrorScreen(errorMessage: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.calm.bg)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = "Bank Error",
            tint = MaterialTheme.calm.warn,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Question Bank Error",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.calm.ink
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "The question engine failed validation and cannot be loaded:",
            fontSize = 14.sp,
            color = MaterialTheme.calm.steel,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.calm.surface,
                    shape = RoundedCornerShape(8.dp)
                )
                .border(
                    width = 1.dp,
                    color = MaterialTheme.calm.warn,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(16.dp)
        ) {
            Text(
                text = errorMessage,
                fontSize = 13.sp,
                color = MaterialTheme.calm.warn,
                style = TabularNumStyle
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Per engine specification, no questions will be served until the bank is corrected.",
            fontSize = 12.sp,
            color = MaterialTheme.calm.steel,
            textAlign = TextAlign.Center
        )
    }
}
