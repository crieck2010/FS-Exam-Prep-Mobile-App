package com.example.model

data class Question(
    val id: String,
    val domain: FsDomain,
    val difficulty: QuestionDifficulty = QuestionDifficulty.ABOUT_THE_SAME,
    val questionNumberLabel: String = "",
    val questionText: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String,
    val formulaHighlight: String? = null,
    val topicTag: String = ""
)

data class UserAnswer(
    val selectedIndex: Int,
    val isCorrect: Boolean,
    val answeredAtMillis: Long = System.currentTimeMillis()
)

data class CompletedModule(
    val id: String,
    val timestamp: Long,
    val title: String,
    val domain: FsDomain?, // null = All Domains / Comprehensive Mock
    val totalQuestions: Int,
    val correctCount: Int,
    val incorrectCount: Int,
    val skippedCount: Int,
    val userAnswers: Map<String, Int> = emptyMap(), // questionId -> selectedIndex
    val durationSeconds: Int = 0
) {
    val percentage: Int
        get() = if (totalQuestions > 0) ((correctCount.toFloat() / totalQuestions) * 100).toInt() else 0
}

data class DomainStat(
    val domain: FsDomain,
    val attempted: Int = 0,
    val correct: Int = 0
) {
    val accuracyPercentage: Int
        get() = if (attempted > 0) ((correct.toFloat() / attempted) * 100).toInt() else 0

    val isStrength: Boolean
        get() = attempted >= 3 && accuracyPercentage >= 70

    val isNeedsWork: Boolean
        get() = attempted >= 2 && accuracyPercentage < 70
}

data class UserProgress(
    val streakDays: Int = 5,
    val lastActiveDate: String = "",
    val totalQuestionsAnswered: Int = 40,
    val totalCorrect: Int = 30,
    val dailyGoalTarget: Int = 20,
    val questionsAnsweredToday: Int = 12
) {
    val overallAccuracy: Int
        get() = if (totalQuestionsAnswered > 0) ((totalCorrect.toFloat() / totalQuestionsAnswered) * 100).toInt() else 0

    val dailyProgressFraction: Float
        get() = (questionsAnsweredToday.toFloat() / dailyGoalTarget.coerceAtLeast(1)).coerceIn(0f, 1f)
}
