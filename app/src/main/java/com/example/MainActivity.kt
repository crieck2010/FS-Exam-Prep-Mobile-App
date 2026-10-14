package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
            MyApplicationTheme(darkTheme = true) {
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

                    when (currentScreen) {
                        ScreenState.HOME -> {
                            HomeScreen(
                                userProgress = userProgress,
                                completedModules = completedModules,
                                domainStats = domainStats,
                                achievements = achievements,
                                leaderboard = leaderboard,
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
                                timeLimitMinutes = 90,
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
