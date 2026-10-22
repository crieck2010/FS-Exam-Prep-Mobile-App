package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.model.Achievement
import com.example.model.AchievementCategory
import com.example.model.AppThemeMode
import com.example.model.CompletedModule
import com.example.model.DomainStat
import com.example.model.FsDomain
import com.example.model.LeaderboardUser
import com.example.model.MockExamResult
import com.example.model.Question
import com.example.model.UserProgress
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class QuizRepository(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("fs_prep_prefs", Context.MODE_PRIVATE)

    private val _completedModules = MutableStateFlow<List<CompletedModule>>(emptyList())
    val completedModules: StateFlow<List<CompletedModule>> = _completedModules.asStateFlow()

    private val _userProgress = MutableStateFlow(UserProgress())
    val userProgress: StateFlow<UserProgress> = _userProgress.asStateFlow()

    private val _domainStats = MutableStateFlow<Map<FsDomain, DomainStat>>(emptyMap())
    val domainStats: StateFlow<Map<FsDomain, DomainStat>> = _domainStats.asStateFlow()

    private val _achievements = MutableStateFlow<List<Achievement>>(emptyList())
    val achievements: StateFlow<List<Achievement>> = _achievements.asStateFlow()

    private val _mockExamResults = MutableStateFlow<List<MockExamResult>>(emptyList())
    val mockExamResults: StateFlow<List<MockExamResult>> = _mockExamResults.asStateFlow()

    private val _leaderboard = MutableStateFlow<List<LeaderboardUser>>(emptyList())
    val leaderboard: StateFlow<List<LeaderboardUser>> = _leaderboard.asStateFlow()

    private val _userHandle = MutableStateFlow("Surveyor#402")
    val userHandle: StateFlow<String> = _userHandle.asStateFlow()

    private val _themeMode = MutableStateFlow(AppThemeMode.SYSTEM)
    val themeMode: StateFlow<AppThemeMode> = _themeMode.asStateFlow()

    init {
        QuestionBank.initialize(context)
        loadData()
    }

    private fun loadData() {
        val savedHandle = prefs.getString("user_handle", "Surveyor#402") ?: "Surveyor#402"
        _userHandle.value = savedHandle

        val themeStr = prefs.getString("app_theme_mode", AppThemeMode.SYSTEM.name) ?: AppThemeMode.SYSTEM.name
        _themeMode.value = try {
            AppThemeMode.valueOf(themeStr)
        } catch (e: Exception) {
            AppThemeMode.SYSTEM
        }

        val modulesJson = prefs.getString("modules_json", null)
        val modules = if (modulesJson != null) {
            parseModules(modulesJson)
        } else {
            seedInitialModules()
        }
        _completedModules.value = modules

        val streak = prefs.getInt("streak_days", 5)
        val target = prefs.getInt("daily_target", 20)
        val todayDate = getTodayDateString()
        val savedDate = prefs.getString("last_active_date", todayDate) ?: todayDate
        val todayCount = if (savedDate == todayDate) prefs.getInt("today_count", 12) else 0

        var totalAnswered = 0
        var totalCorrect = 0
        modules.forEach {
            totalAnswered += it.totalQuestions
            totalCorrect += it.correctCount
        }

        val progress = UserProgress(
            streakDays = streak,
            lastActiveDate = savedDate,
            totalQuestionsAnswered = totalAnswered.coerceAtLeast(40),
            totalCorrect = totalCorrect.coerceAtLeast(30),
            dailyGoalTarget = target,
            questionsAnsweredToday = todayCount
        )
        _userProgress.value = progress

        computeDomainStats(modules)
        loadMockExams()
        updateAchievements(modules, progress, _domainStats.value, _mockExamResults.value)
        refreshLeaderboard(progress)
    }

    fun updateHandle(newHandle: String) {
        val trimmed = newHandle.trim().take(20)
        if (trimmed.isNotBlank()) {
            _userHandle.value = trimmed
            prefs.edit().putString("user_handle", trimmed).apply()
            refreshLeaderboard(_userProgress.value)
        }
    }

    fun setThemeMode(mode: AppThemeMode) {
        _themeMode.value = mode
        prefs.edit().putString("app_theme_mode", mode.name).apply()
    }

    private fun seedInitialModules(): List<CompletedModule> {
        val now = System.currentTimeMillis()
        val seed = listOf(
            CompletedModule(
                id = "mod_seed_1",
                timestamp = now - (3600 * 1000 * 4), // 4 hours ago
                title = "Comprehensive FS Mock Exam",
                domain = null,
                totalQuestions = 40,
                correctCount = 30,
                incorrectCount = 10,
                skippedCount = 0,
                userAnswers = mapOf(
                    "q_curv_01" to 0,
                    "q_plss_01" to 2,
                    "q_equip_01" to 2,
                    "q_law_01" to 1,
                    "q_law_02" to 2,
                    "q_geod_01" to 0
                ),
                durationSeconds = 1840
            ),
            CompletedModule(
                id = "mod_seed_2",
                timestamp = now - (3600 * 1000 * 26),
                title = "Boundary Law & Real Property Drill",
                domain = FsDomain.BOUNDARY_LAW_REAL_PROPERTY,
                totalQuestions = 10,
                correctCount = 9,
                incorrectCount = 1,
                skippedCount = 0,
                userAnswers = mapOf(
                    "q_law_01" to 1,
                    "q_law_02" to 2,
                    "q_law_03" to 2
                ),
                durationSeconds = 480
            ),
            CompletedModule(
                id = "mod_seed_3",
                timestamp = now - (3600 * 1000 * 52),
                title = "Surveying Processes & Methods",
                domain = FsDomain.SURVEYING_PROCESSES_METHODS,
                totalQuestions = 10,
                correctCount = 8,
                incorrectCount = 2,
                skippedCount = 0,
                userAnswers = mapOf(
                    "q_equip_01" to 2,
                    "q_equip_02" to 0
                ),
                durationSeconds = 510
            )
        )
        saveModules(seed)
        return seed
    }

    private fun computeDomainStats(modules: List<CompletedModule>) {
        val statsMap = mutableMapOf<FsDomain, DomainStat>()
        FsDomain.entries.forEach { domain ->
            statsMap[domain] = DomainStat(domain = domain, attempted = 0, correct = 0)
        }

        modules.forEach { module ->
            module.userAnswers.forEach { (qId, answerIdx) ->
                val q = QuestionBank.questions.find { it.id == qId }
                if (q != null) {
                    val current = statsMap[q.domain] ?: DomainStat(domain = q.domain)
                    val isCorrect = (answerIdx == q.correctIndex)
                    statsMap[q.domain] = current.copy(
                        attempted = current.attempted + 1,
                        correct = current.correct + (if (isCorrect) 1 else 0)
                    )
                }
            }
        }

        val firstDomain = FsDomain.entries.firstOrNull()
        if (firstDomain != null && statsMap[firstDomain]?.attempted == 0) {
            statsMap[FsDomain.BOUNDARY_LAW_REAL_PROPERTY] = DomainStat(FsDomain.BOUNDARY_LAW_REAL_PROPERTY, attempted = 12, correct = 11)
            statsMap[FsDomain.SURVEYING_PROCESSES_METHODS] = DomainStat(FsDomain.SURVEYING_PROCESSES_METHODS, attempted = 10, correct = 9)
            statsMap[FsDomain.SURVEYING_PRINCIPLES_GEODESY] = DomainStat(FsDomain.SURVEYING_PRINCIPLES_GEODESY, attempted = 8, correct = 4)
            statsMap[FsDomain.SURVEY_COMPUTATIONS] = DomainStat(FsDomain.SURVEY_COMPUTATIONS, attempted = 10, correct = 6)
            statsMap[FsDomain.BUSINESS_CONCEPTS_ETHICS] = DomainStat(FsDomain.BUSINESS_CONCEPTS_ETHICS, attempted = 6, correct = 5)
            statsMap[FsDomain.APPLIED_MATH_STATISTICS] = DomainStat(FsDomain.APPLIED_MATH_STATISTICS, attempted = 6, correct = 4)
            statsMap[FsDomain.MAPPING_PROCESSES_METHODS] = DomainStat(FsDomain.MAPPING_PROCESSES_METHODS, attempted = 4, correct = 3)
        }

        _domainStats.value = statsMap
    }

    private fun updateAchievements(
        modules: List<CompletedModule>,
        progress: UserProgress,
        stats: Map<FsDomain, DomainStat>,
        mockExams: List<MockExamResult>
    ) {
        val list = mutableListOf<Achievement>()

        // 1. First Step
        list.add(
            Achievement(
                id = "ach_first_module",
                title = "First Step",
                description = "Complete your first practice module",
                category = AchievementCategory.VOLUME,
                iconEmoji = "🌟",
                currentProgress = modules.size.coerceAtMost(1),
                targetProgress = 1,
                isUnlocked = modules.isNotEmpty(),
                unlockedDate = if (modules.isNotEmpty()) "Unlocked" else null
            )
        )

        // 2. Consistent Surveyor (3-day streak)
        list.add(
            Achievement(
                id = "ach_streak_3",
                title = "Consistent Surveyor",
                description = "Maintain a 3-day study streak",
                category = AchievementCategory.STREAK,
                iconEmoji = "🔥",
                currentProgress = progress.streakDays.coerceAtMost(3),
                targetProgress = 3,
                isUnlocked = progress.streakDays >= 3,
                unlockedDate = if (progress.streakDays >= 3) "Unlocked" else null
            )
        )

        // 3. Iron Chainman (7-day streak)
        list.add(
            Achievement(
                id = "ach_streak_7",
                title = "Iron Chainman",
                description = "Maintain a 7-day study streak",
                category = AchievementCategory.STREAK,
                iconEmoji = "⚡",
                currentProgress = progress.streakDays.coerceAtMost(7),
                targetProgress = 7,
                isUnlocked = progress.streakDays >= 7,
                unlockedDate = if (progress.streakDays >= 7) "Unlocked" else null
            )
        )

        // 4. Chief of Party (14-day streak)
        list.add(
            Achievement(
                id = "ach_streak_14",
                title = "Chief of Party",
                description = "Maintain a 14-day study streak",
                category = AchievementCategory.STREAK,
                iconEmoji = "👑",
                currentProgress = progress.streakDays.coerceAtMost(14),
                targetProgress = 14,
                isUnlocked = progress.streakDays >= 14
            )
        )

        // 5. Sharp Shooter (>= 90% score)
        val has90 = modules.any { it.percentage >= 90 }
        list.add(
            Achievement(
                id = "ach_sharp_shooter",
                title = "Sharp Shooter",
                description = "Score 90% or higher on any practice quiz",
                category = AchievementCategory.ACCURACY,
                iconEmoji = "🎯",
                currentProgress = if (has90) 1 else 0,
                targetProgress = 1,
                isUnlocked = has90,
                unlockedDate = if (has90) "Unlocked" else null
            )
        )

        // 6. Flawless (100% on a module)
        val has100 = modules.any { it.percentage == 100 }
        list.add(
            Achievement(
                id = "ach_flawless",
                title = "Flawless",
                description = "Achieve a perfect 100% score on a module",
                category = AchievementCategory.ACCURACY,
                iconEmoji = "💯",
                currentProgress = if (has100) 1 else 0,
                targetProgress = 1,
                isUnlocked = has100
            )
        )

        // 7. Century Club (100 total questions answered)
        list.add(
            Achievement(
                id = "ach_century_club",
                title = "Century Club",
                description = "Answer 100 total practice questions",
                category = AchievementCategory.VOLUME,
                iconEmoji = "🏆",
                currentProgress = progress.totalQuestionsAnswered.coerceAtMost(100),
                targetProgress = 100,
                isUnlocked = progress.totalQuestionsAnswered >= 100
            )
        )

        // 8. Boundary Law Savvy (Mastery >= 80%)
        val lawStat = stats[FsDomain.BOUNDARY_LAW_REAL_PROPERTY]
        val lawMastered = (lawStat != null && lawStat.attempted >= 5 && lawStat.accuracyPercentage >= 80)
        list.add(
            Achievement(
                id = "ach_master_law",
                title = "Boundary Law Savvy",
                description = "Demonstrate 80%+ mastery in Boundary Law & Real Property",
                category = AchievementCategory.DOMAIN_MASTERY,
                iconEmoji = "📜",
                currentProgress = lawStat?.accuracyPercentage?.coerceAtMost(100) ?: 0,
                targetProgress = 80,
                isUnlocked = lawMastered,
                unlockedDate = if (lawMastered) "Unlocked" else null
            )
        )

        // 9. Survey Computations Master
        val compStat = stats[FsDomain.SURVEY_COMPUTATIONS]
        val compMastered = (compStat != null && compStat.attempted >= 5 && compStat.accuracyPercentage >= 80)
        list.add(
            Achievement(
                id = "ach_master_comp",
                title = "Computations Master",
                description = "Demonstrate 80%+ mastery in Survey Computations",
                category = AchievementCategory.DOMAIN_MASTERY,
                iconEmoji = "📐",
                currentProgress = compStat?.accuracyPercentage?.coerceAtMost(100) ?: 0,
                targetProgress = 80,
                isUnlocked = compMastered
            )
        )

        // 10. Applied Math Specialist
        val mathStat = stats[FsDomain.APPLIED_MATH_STATISTICS]
        val mathMastered = (mathStat != null && mathStat.attempted >= 5 && mathStat.accuracyPercentage >= 80)
        list.add(
            Achievement(
                id = "ach_master_math",
                title = "Math & Stats Specialist",
                description = "Demonstrate 80%+ mastery in Applied Math & Statistics",
                category = AchievementCategory.DOMAIN_MASTERY,
                iconEmoji = "📊",
                currentProgress = mathStat?.accuracyPercentage?.coerceAtMost(100) ?: 0,
                targetProgress = 80,
                isUnlocked = mathMastered
            )
        )

        // 11. Simulated Pro (Complete a full Mock Exam)
        val hasMock = mockExams.isNotEmpty()
        list.add(
            Achievement(
                id = "ach_mock_exam",
                title = "Simulated Pro",
                description = "Complete a full timed FS Mock Exam",
                category = AchievementCategory.MOCK_EXAM,
                iconEmoji = "🎓",
                currentProgress = if (hasMock) 1 else 0,
                targetProgress = 1,
                isUnlocked = hasMock,
                unlockedDate = if (hasMock) "Unlocked" else null
            )
        )

        _achievements.value = list
    }

    private fun refreshLeaderboard(progress: UserProgress) {
        val userPoints = (progress.totalCorrect * 15) + (progress.streakDays * 50) + 120
        val handle = _userHandle.value

        val peerRoster = listOf(
            LeaderboardUser(1, "PlumbBob99", 1950, 21, 150, 92, badgeLabel = "Survey Veteran"),
            LeaderboardUser(2, "GeodeticCadet", 1680, 18, 135, 88, badgeLabel = "Geodesy Specialist"),
            LeaderboardUser(3, "BenchmarkPro", 1420, 14, 110, 85, badgeLabel = "CBT Ready"),
            LeaderboardUser(4, "CompassAzimuth", 1240, 11, 95, 83),
            LeaderboardUser(5, "SectionCorner", 1080, 9, 82, 80),
            LeaderboardUser(6, "ZenithAngle", 940, 8, 70, 78),
            LeaderboardUser(7, "TransitKing", 820, 6, 60, 76),
            LeaderboardUser(8, "LambertConic", 710, 5, 52, 75),
            LeaderboardUser(9, "UTMZoner", 610, 4, 45, 73),
            LeaderboardUser(10, "AliquotAce", 520, 3, 38, 71)
        )

        val currentUserEntry = LeaderboardUser(
            rank = 0,
            handle = handle,
            scorePoints = userPoints,
            streakDays = progress.streakDays,
            questionsAnswered = progress.totalQuestionsAnswered,
            accuracy = progress.overallAccuracy,
            isCurrentUser = true,
            badgeLabel = if (userPoints >= 1000) "Top Contender" else "Active Candidate"
        )

        val combined = (peerRoster + currentUserEntry).sortedByDescending { it.scorePoints }
        val ranked = combined.mapIndexed { idx, u -> u.copy(rank = idx + 1) }
        _leaderboard.value = ranked
    }

    private fun loadMockExams() {
        val jsonStr = prefs.getString("mock_exams_json", null)
        if (jsonStr != null) {
            try {
                val array = JSONArray(jsonStr)
                val list = mutableListOf<MockExamResult>()
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    val id = obj.getString("id")
                    val timestamp = obj.getLong("timestamp")
                    val total = obj.getInt("totalQuestions")
                    val correct = obj.getInt("correctCount")
                    val scorePct = obj.getInt("scorePercentage")
                    val timeSpent = obj.getInt("timeSpentSeconds")
                    val passing = obj.getBoolean("isPassing")

                    val domainScoresMap = mutableMapOf<FsDomain, Pair<Int, Int>>()
                    if (obj.has("domainScores")) {
                        val dsObj = obj.getJSONObject("domainScores")
                        dsObj.keys().forEach { dKey ->
                            val dom = FsDomain.fromId(dKey)
                            if (dom != null) {
                                val pairArr = dsObj.getJSONArray(dKey)
                                domainScoresMap[dom] = Pair(pairArr.getInt(0), pairArr.getInt(1))
                            }
                        }
                    }

                    list.add(
                        MockExamResult(
                            id = id,
                            timestamp = timestamp,
                            totalQuestions = total,
                            correctCount = correct,
                            scorePercentage = scorePct,
                            timeSpentSeconds = timeSpent,
                            isPassing = passing,
                            domainScores = domainScoresMap,
                            userAnswers = emptyMap(),
                            flaggedQuestionIds = emptySet()
                        )
                    )
                }
                _mockExamResults.value = list
            } catch (e: Exception) {
                _mockExamResults.value = emptyList()
            }
        }
    }

    fun saveMockExamResult(result: MockExamResult) {
        val updated = listOf(result) + _mockExamResults.value
        _mockExamResults.value = updated

        // Persist mock exams
        val array = JSONArray()
        updated.forEach { r ->
            val obj = JSONObject().apply {
                put("id", r.id)
                put("timestamp", r.timestamp)
                put("totalQuestions", r.totalQuestions)
                put("correctCount", r.correctCount)
                put("scorePercentage", r.scorePercentage)
                put("timeSpentSeconds", r.timeSpentSeconds)
                put("isPassing", r.isPassing)
                val dsObj = JSONObject()
                r.domainScores.forEach { (dom, score) ->
                    val pairArr = JSONArray().put(score.first).put(score.second)
                    dsObj.put(dom.name, pairArr)
                }
                put("domainScores", dsObj)
            }
            array.put(obj)
        }
        prefs.edit().putString("mock_exams_json", array.toString()).apply()

        // Also treat as a completed module
        saveCompletedQuiz(
            domain = null,
            questions = QuestionBank.questions.filter { result.userAnswers.containsKey(it.id) },
            userAnswers = result.userAnswers,
            durationSeconds = result.timeSpentSeconds
        )
    }

    fun saveCompletedQuiz(
        domain: FsDomain?,
        questions: List<Question>,
        userAnswers: Map<String, Int>,
        durationSeconds: Int
    ): CompletedModule {
        var correct = 0
        var incorrect = 0
        questions.forEach { q ->
            val ans = userAnswers[q.id]
            if (ans != null) {
                if (ans == q.correctIndex) correct++ else incorrect++
            } else {
                incorrect++
            }
        }

        val title = if (domain != null) "${domain.title} Practice" else "Practice Exam Session"
        val module = CompletedModule(
            id = "mod_${System.currentTimeMillis()}",
            timestamp = System.currentTimeMillis(),
            title = title,
            domain = domain,
            totalQuestions = questions.size,
            correctCount = correct,
            incorrectCount = incorrect,
            skippedCount = 0,
            userAnswers = userAnswers,
            durationSeconds = durationSeconds
        )

        val updated = listOf(module) + _completedModules.value
        _completedModules.value = updated
        saveModules(updated)

        val today = getTodayDateString()
        val currentProgress = _userProgress.value
        val newTodayCount = currentProgress.questionsAnsweredToday + questions.size
        val newStreak = if (currentProgress.lastActiveDate == today) {
            currentProgress.streakDays
        } else {
            currentProgress.streakDays + 1
        }

        val newProgress = currentProgress.copy(
            streakDays = newStreak,
            lastActiveDate = today,
            totalQuestionsAnswered = currentProgress.totalQuestionsAnswered + questions.size,
            totalCorrect = currentProgress.totalCorrect + correct,
            questionsAnsweredToday = newTodayCount
        )
        _userProgress.value = newProgress

        prefs.edit()
            .putInt("streak_days", newStreak)
            .putString("last_active_date", today)
            .putInt("today_count", newTodayCount)
            .apply()

        computeDomainStats(updated)
        updateAchievements(updated, newProgress, _domainStats.value, _mockExamResults.value)
        refreshLeaderboard(newProgress)
        return module
    }

    private fun saveModules(modules: List<CompletedModule>) {
        val array = JSONArray()
        modules.forEach { m ->
            val obj = JSONObject().apply {
                put("id", m.id)
                put("timestamp", m.timestamp)
                put("title", m.title)
                put("domain", m.domain?.name)
                put("totalQuestions", m.totalQuestions)
                put("correctCount", m.correctCount)
                put("incorrectCount", m.incorrectCount)
                put("skippedCount", m.skippedCount)
                put("durationSeconds", m.durationSeconds)
                val ansObj = JSONObject()
                m.userAnswers.forEach { (k, v) -> ansObj.put(k, v) }
                put("userAnswers", ansObj)
            }
            array.put(obj)
        }
        prefs.edit().putString("modules_json", array.toString()).apply()
    }

    private fun parseModules(jsonStr: String): List<CompletedModule> {
        return try {
            val array = JSONArray(jsonStr)
            val list = mutableListOf<CompletedModule>()
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                val domainName = if (obj.has("domain") && !obj.isNull("domain")) obj.getString("domain") else null
                val domain = domainName?.let { FsDomain.fromId(it) }

                val userAnsMap = mutableMapOf<String, Int>()
                if (obj.has("userAnswers")) {
                    val ansObj = obj.getJSONObject("userAnswers")
                    ansObj.keys().forEach { k ->
                        userAnsMap[k] = ansObj.getInt(k)
                    }
                }

                list.add(
                    CompletedModule(
                        id = obj.getString("id"),
                        timestamp = obj.getLong("timestamp"),
                        title = obj.getString("title"),
                        domain = domain,
                        totalQuestions = obj.getInt("totalQuestions"),
                        correctCount = obj.getInt("correctCount"),
                        incorrectCount = obj.getInt("incorrectCount"),
                        skippedCount = obj.optInt("skippedCount", 0),
                        userAnswers = userAnsMap,
                        durationSeconds = obj.optInt("durationSeconds", 0)
                    )
                )
            }
            list
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun getTodayDateString(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        return sdf.format(Date())
    }
}
