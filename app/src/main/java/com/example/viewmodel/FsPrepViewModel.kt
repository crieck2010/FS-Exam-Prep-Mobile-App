package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.data.QuestionBank
import com.example.data.QuizRepository
import com.example.model.Achievement
import com.example.model.AppThemeMode
import com.example.model.CompletedModule
import com.example.model.DomainStat
import com.example.model.FsDomain
import com.example.model.LeaderboardUser
import com.example.model.MockExamResult
import com.example.model.Question
import com.example.model.QuestionDifficulty
import com.example.model.UserProgress
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class ScreenState {
    HOME,
    QUIZ,
    SCORE_SUMMARY,
    MOCK_EXAM,
    MOCK_EXAM_RESULT,
    ACHIEVEMENTS,
    LEADERBOARD,
    FLASHCARDS,
    STUDY_GUIDE
}

class FsPrepViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = QuizRepository(application.applicationContext)

    val userProgress: StateFlow<UserProgress> = repository.userProgress
    val completedModules: StateFlow<List<CompletedModule>> = repository.completedModules
    val domainStats: StateFlow<Map<FsDomain, DomainStat>> = repository.domainStats
    val achievements: StateFlow<List<Achievement>> = repository.achievements
    val leaderboard: StateFlow<List<LeaderboardUser>> = repository.leaderboard
    val userHandle: StateFlow<String> = repository.userHandle
    val themeMode: StateFlow<AppThemeMode> = repository.themeMode

    private val _currentScreen = MutableStateFlow(ScreenState.HOME)
    val currentScreen: StateFlow<ScreenState> = _currentScreen.asStateFlow()

    private val _activeQuestions = MutableStateFlow<List<Question>>(emptyList())
    val activeQuestions: StateFlow<List<Question>> = _activeQuestions.asStateFlow()

    private val _activeAnswers = MutableStateFlow<Map<String, Int>>(emptyMap())
    val activeAnswers: StateFlow<Map<String, Int>> = _activeAnswers.asStateFlow()

    private val _isReviewMode = MutableStateFlow(false)
    val isReviewMode: StateFlow<Boolean> = _isReviewMode.asStateFlow()

    private val _lastCompletedModule = MutableStateFlow<CompletedModule?>(null)
    val lastCompletedModule: StateFlow<CompletedModule?> = _lastCompletedModule.asStateFlow()

    private val _lastMockResult = MutableStateFlow<MockExamResult?>(null)
    val lastMockResult: StateFlow<MockExamResult?> = _lastMockResult.asStateFlow()

    private val _mockQuestions = MutableStateFlow<List<Question>>(emptyList())
    val mockQuestions: StateFlow<List<Question>> = _mockQuestions.asStateFlow()

    private val _showConfigSheet = MutableStateFlow(false)
    val showConfigSheet: StateFlow<Boolean> = _showConfigSheet.asStateFlow()

    private val _selectedConfigDomain = MutableStateFlow<FsDomain?>(null)
    val selectedConfigDomain: StateFlow<FsDomain?> = _selectedConfigDomain.asStateFlow()

    private var quizStartTimeMillis: Long = 0L

    fun openQuizConfig(domain: FsDomain? = null) {
        _selectedConfigDomain.value = domain
        _showConfigSheet.value = true
    }

    fun dismissQuizConfig() {
        _showConfigSheet.value = false
        _selectedConfigDomain.value = null
    }

    fun startConfiguredQuiz(
        domain: FsDomain?,
        count: Int,
        focusGrowth: Boolean,
        difficulty: QuestionDifficulty
    ) {
        dismissQuizConfig()

        val weakDomains = domainStats.value.filter { it.value.isNeedsWork }.keys
        val questions = QuestionBank.getQuestionsForQuiz(
            domain = domain,
            count = count,
            focusOnGrowth = focusGrowth,
            weakDomains = weakDomains,
            difficulty = difficulty
        )

        _activeQuestions.value = questions
        _activeAnswers.value = emptyMap()
        _isReviewMode.value = false
        quizStartTimeMillis = System.currentTimeMillis()
        _currentScreen.value = ScreenState.QUIZ
    }

    fun startDomainQuiz(domain: FsDomain) {
        openQuizConfig(domain)
    }

    fun startAdaptiveQuiz() {
        val masteryMap = domainStats.value.mapValues { it.value.accuracyPercentage }
        val adaptiveQuestions = QuestionBank.getAdaptiveQuestions(masteryMap, count = 10)

        _activeQuestions.value = adaptiveQuestions
        _activeAnswers.value = emptyMap()
        _isReviewMode.value = false
        quizStartTimeMillis = System.currentTimeMillis()
        _currentScreen.value = ScreenState.QUIZ
    }

    fun startMockExam(timeLimitMinutes: Int = 320) {
        val questions = QuestionBank.getMockExamQuestions()
        _mockQuestions.value = questions
        _currentScreen.value = ScreenState.MOCK_EXAM
    }

    fun submitMockExam(result: MockExamResult) {
        repository.saveMockExamResult(result)
        _lastMockResult.value = result
        _currentScreen.value = ScreenState.MOCK_EXAM_RESULT
    }

    fun reviewMockExam(onlyIncorrect: Boolean = false) {
        val result = _lastMockResult.value ?: return
        val allQuestions = _mockQuestions.value

        val questionsToReview = if (onlyIncorrect) {
            allQuestions.filter { q ->
                val ans = result.userAnswers[q.id]
                ans == null || ans != q.correctIndex
            }
        } else {
            allQuestions
        }

        _activeQuestions.value = questionsToReview
        _activeAnswers.value = result.userAnswers
        _isReviewMode.value = true
        _currentScreen.value = ScreenState.QUIZ
    }

    fun finishQuiz(answers: Map<String, Int>) {
        val durationSeconds = ((System.currentTimeMillis() - quizStartTimeMillis) / 1000).toInt().coerceAtLeast(1)
        val domain = _selectedConfigDomain.value
        val questions = _activeQuestions.value

        val module = repository.saveCompletedQuiz(
            domain = domain,
            questions = questions,
            userAnswers = answers,
            durationSeconds = durationSeconds
        )

        _activeAnswers.value = answers
        _lastCompletedModule.value = module
        _currentScreen.value = ScreenState.SCORE_SUMMARY
    }

    fun reviewCurrentQuiz() {
        _isReviewMode.value = true
        _currentScreen.value = ScreenState.QUIZ
    }

    fun reviewCompletedModule(module: CompletedModule) {
        val questions = if (module.domain != null) {
            QuestionBank.questions.filter { it.domain == module.domain }
        } else {
            val answeredQuestions = QuestionBank.questions.filter { module.userAnswers.containsKey(it.id) }
            if (answeredQuestions.isNotEmpty()) answeredQuestions else QuestionBank.questions.take(module.totalQuestions)
        }

        _activeQuestions.value = questions
        _activeAnswers.value = module.userAnswers
        _isReviewMode.value = true
        _lastCompletedModule.value = module
        _currentScreen.value = ScreenState.QUIZ
    }

    fun updateUserHandle(handle: String) {
        repository.updateHandle(handle)
    }

    fun navigateToHome() {
        _currentScreen.value = ScreenState.HOME
        _isReviewMode.value = false
    }

    fun openAchievements() {
        _currentScreen.value = ScreenState.ACHIEVEMENTS
    }

    fun openLeaderboard() {
        _currentScreen.value = ScreenState.LEADERBOARD
    }

    fun openFlashcards() {
        _currentScreen.value = ScreenState.FLASHCARDS
    }

    fun openStudyGuide() {
        _currentScreen.value = ScreenState.STUDY_GUIDE
    }

    fun setThemeMode(mode: AppThemeMode) {
        repository.setThemeMode(mode)
    }
}
