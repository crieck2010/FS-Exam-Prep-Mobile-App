package com.example.model

enum class AchievementCategory {
    STREAK,
    ACCURACY,
    VOLUME,
    DOMAIN_MASTERY,
    MOCK_EXAM
}

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val category: AchievementCategory,
    val iconEmoji: String,
    val currentProgress: Int,
    val targetProgress: Int,
    val isUnlocked: Boolean,
    val unlockedDate: String? = null
) {
    val progressFraction: Float
        get() = (currentProgress.toFloat() / targetProgress.coerceAtLeast(1)).coerceIn(0f, 1f)
}

data class LeaderboardUser(
    val rank: Int,
    val handle: String,
    val scorePoints: Int,
    val streakDays: Int,
    val questionsAnswered: Int,
    val accuracy: Int,
    val isCurrentUser: Boolean = false,
    val badgeLabel: String? = null
)

data class MockExamConfig(
    val title: String,
    val questionCount: Int,
    val timeLimitMinutes: Int,
    val isFullSimulation: Boolean
)

data class MockExamResult(
    val id: String,
    val timestamp: Long,
    val totalQuestions: Int,
    val correctCount: Int,
    val scorePercentage: Int,
    val timeSpentSeconds: Int,
    val isPassing: Boolean, // typically >= 70% (practice benchmark)
    val domainScores: Map<FsDomain, Pair<Int, Int>>, // domain -> (correct, total)
    val userAnswers: Map<String, Int>,
    val flaggedQuestionIds: Set<String>
)
