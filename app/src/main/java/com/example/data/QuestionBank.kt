package com.example.data

import android.content.Context
import android.util.Log
import com.example.model.FsDomain
import com.example.model.Question
import com.example.model.QuestionDifficulty
import org.json.JSONObject
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader

object QuestionBank {

    private const val TAG = "QuestionBank"
    private const val BANK_ASSET_PATH = "banks/bank-seed-20261026.json"

    private var _questions: List<Question>? = null

    var loadError: String? = null
        private set

    var isLoaded: Boolean = false
        private set

    /**
     * Public API: list of all loaded questions.
     * Evaluates lazily from app assets.
     */
    val questions: List<Question>
        get() {
            ensureLoaded()
            return _questions ?: emptyList()
        }

    /**
     * Initialize the question bank with an Android Context.
     */
    fun initialize(context: Context) {
        if (isLoaded || loadError != null) return
        try {
            context.assets.open(BANK_ASSET_PATH).use { stream ->
                loadFromStream(stream)
            }
        } catch (e: Exception) {
            failWith("Failed opening asset '$BANK_ASSET_PATH': ${e.message}")
        }
    }

    private fun ensureLoaded() {
        if (isLoaded || loadError != null) return

        // 1. Try application context if available
        val streamFromApp: InputStream? = try {
            com.example.FsPrepApplication.instance?.assets?.open(BANK_ASSET_PATH)
        } catch (_: Throwable) {
            null
        }

        // 2. Try classLoader resources (e.g. In test runner or packaged jar)
        val streamFromClassLoader: InputStream? = streamFromApp
            ?: javaClass.classLoader?.getResourceAsStream("assets/$BANK_ASSET_PATH")
            ?: javaClass.classLoader?.getResourceAsStream(BANK_ASSET_PATH)

        // 3. Try filesystem fallback (useful in JVM unit tests)
        val stream: InputStream? = streamFromClassLoader ?: listOf(
            File("app/src/main/assets/$BANK_ASSET_PATH"),
            File("src/main/assets/$BANK_ASSET_PATH")
        ).firstOrNull { it.exists() }?.inputStream()

        if (stream == null) {
            failWith("Question bank asset not found at '$BANK_ASSET_PATH'")
            return
        }

        try {
            stream.use { loadFromStream(it) }
        } catch (e: Exception) {
            failWith("Failed loading question bank: ${e.message}")
        }
    }

    /**
     * Parses and strictly validates a question bank stream according to schema v1 rules.
     */
    fun loadFromStream(inputStream: InputStream) {
        if (isLoaded) return

        try {
            val jsonString = BufferedReader(InputStreamReader(inputStream, Charsets.UTF_8)).use { it.readText() }
            val root = JSONObject(jsonString)

            if (!root.has("questions")) {
                failWith("Bank JSON missing 'questions' array")
                return
            }

            val questionsArray = root.getJSONArray("questions")
            val actualCount = questionsArray.length()
            val declaredCount = root.optInt("question_count", -1)

            // Rule (b): question_count equals the actual number of questions
            if (declaredCount != actualCount) {
                failWith("question_count ($declaredCount) does not equal actual number of questions ($actualCount)")
                return
            }

            // Top-level schema version check if present
            if (root.has("schema_version") && root.getInt("schema_version") != 1) {
                failWith("Top-level schema_version is ${root.getInt("schema_version")}, expected 1")
                return
            }

            val parsedQuestions = ArrayList<Question>(actualCount)
            val seenQids = mutableSetOf<String>()

            for (i in 0 until actualCount) {
                val qObj = questionsArray.getJSONObject(i)

                // Rule (a): the bank's schema_version is not 1
                val schemaVersion = qObj.optInt("schema_version", -1)
                if (schemaVersion != 1) {
                    failWith("Question at index $i has schema_version $schemaVersion, expected 1")
                    return
                }

                // Rule (c): any qid is empty or duplicated
                val qid = qObj.optString("qid", "").trim()
                if (qid.isEmpty()) {
                    failWith("Question at index $i has empty qid")
                    return
                }
                if (!seenQids.add(qid)) {
                    failWith("Duplicate qid found: '$qid' at index $i")
                    return
                }

                // Rule (f): any stem or explanation is empty
                val stem = qObj.optString("stem", "").trim()
                if (stem.isEmpty()) {
                    failWith("Question '$qid' has empty stem")
                    return
                }
                val explanation = qObj.optString("explanation", "").trim()
                if (explanation.isEmpty()) {
                    failWith("Question '$qid' has empty explanation")
                    return
                }

                // Rule (d): any question does not have exactly 4 unique, non-empty choices
                if (!qObj.has("choices")) {
                    failWith("Question '$qid' missing 'choices' array")
                    return
                }
                val choicesArray = qObj.getJSONArray("choices")
                if (choicesArray.length() != 4) {
                    failWith("Question '$qid' must have exactly 4 choices (found ${choicesArray.length()})")
                    return
                }
                val choices = ArrayList<String>(4)
                val choiceSet = mutableSetOf<String>()
                for (cIdx in 0 until 4) {
                    val choice = choicesArray.getString(cIdx).trim()
                    if (choice.isEmpty()) {
                        failWith("Question '$qid' choice at index $cIdx is empty")
                        return
                    }
                    if (!choiceSet.add(choice)) {
                        failWith("Question '$qid' contains duplicate choices: '$choice'")
                        return
                    }
                    choices.add(choice)
                }

                // Rule (e): any answer_index is outside 0–3
                val answerIndex = qObj.optInt("answer_index", -1)
                if (answerIndex !in 0..3) {
                    failWith("Question '$qid' answer_index ($answerIndex) is outside 0–3")
                    return
                }

                // Domain taxonomy check: matched against the 7 slugs
                val domainSlug = qObj.optString("domain", "").trim()
                val domain = FsDomain.fromSlug(domainSlug)
                if (domain == null) {
                    failWith("Question '$qid' has unrecognized domain slug: '$domainSlug'")
                    return
                }

                // Difficulty mapping: 1 -> EASIER, 2 -> ABOUT_THE_SAME, 3 -> HARDER
                val diffInt = qObj.optInt("difficulty", 2)
                val difficulty = when (diffInt) {
                    1 -> QuestionDifficulty.EASIER
                    2 -> QuestionDifficulty.ABOUT_THE_SAME
                    3 -> QuestionDifficulty.HARDER
                    else -> QuestionDifficulty.ABOUT_THE_SAME
                }

                val topic = qObj.optString("topic", "").trim()

                parsedQuestions.add(
                    Question(
                        id = qid,
                        domain = domain,
                        difficulty = difficulty,
                        questionNumberLabel = "",
                        questionText = stem,
                        options = choices,
                        correctIndex = answerIndex,
                        explanation = explanation,
                        formulaHighlight = null,
                        topicTag = topic
                    )
                )
            }

            _questions = parsedQuestions
            isLoaded = true
            loadError = null
            Log.i(TAG, "Successfully validated and loaded ${_questions?.size} questions from engine bank")
        } catch (e: Exception) {
            failWith("JSON parsing error: ${e.message}")
        }
    }

    private fun failWith(message: String) {
        Log.e(TAG, "QuestionBank validation failure: $message")
        loadError = message
        _questions = emptyList()
        isLoaded = true
    }

    /**
     * Filters questions for domain quizzes.
     */
    fun getQuestionsForQuiz(
        domain: FsDomain?,
        count: Int,
        focusOnGrowth: Boolean,
        weakDomains: Set<FsDomain> = emptySet(),
        difficulty: QuestionDifficulty = QuestionDifficulty.ABOUT_THE_SAME
    ): List<Question> {
        val all = questions
        val filtered = when {
            domain != null -> all.filter { it.domain == domain }
            focusOnGrowth && weakDomains.isNotEmpty() -> all.filter { it.domain in weakDomains }
            else -> all
        }

        val pool = if (filtered.isEmpty()) all else filtered
        return pool.shuffled().take(count.coerceAtLeast(1).coerceAtMost(pool.size))
    }

    /**
     * Generates a balanced mock exam question set covering all 7 domains per official FS specifications.
     * Defaults to 110 questions distributed proportionally across the 7 knowledge areas.
     */
    fun getMockExamQuestions(count: Int = 110): List<Question> {
        val all = questions
        if (all.isEmpty()) return emptyList()
        val targetCount = count.coerceAtLeast(1).coerceAtMost(all.size)

        val domainGroups = all.groupBy { it.domain }
        val domainCounts = mutableMapOf<FsDomain, Int>()
        var allocated = 0

        // Calculate base quota per domain
        domainGroups.forEach { (domain, domainQuestions) ->
            val quota = ((domainQuestions.size.toDouble() / all.size.toDouble()) * targetCount).toInt()
            domainCounts[domain] = quota
            allocated += quota
        }

        // Distribute remaining slots by largest fractional remainder
        val remainders = domainGroups.keys.map { domain ->
            val exact = (domainGroups[domain]!!.size.toDouble() / all.size.toDouble()) * targetCount
            val remainder = exact - (domainCounts[domain] ?: 0)
            domain to remainder
        }.sortedByDescending { it.second }

        var remainderIndex = 0
        while (allocated < targetCount && remainderIndex < remainders.size) {
            val domain = remainders[remainderIndex].first
            domainCounts[domain] = (domainCounts[domain] ?: 0) + 1
            allocated++
            remainderIndex++
        }

        val selected = mutableListOf<Question>()
        domainCounts.forEach { (domain, quota) ->
            val domainPool = domainGroups[domain]?.shuffled() ?: emptyList()
            selected.addAll(domainPool.take(quota))
        }

        // Safety fallback if rounding fell short
        if (selected.size < targetCount) {
            val selectedIds = selected.map { it.id }.toSet()
            val remaining = all.filter { it.id !in selectedIds }.shuffled()
            selected.addAll(remaining.take(targetCount - selected.size))
        }

        return selected.shuffled().take(targetCount)
    }

    /**
     * Adaptive question selector: dynamically selects questions based on domain mastery.
     */
    fun getAdaptiveQuestions(
        domainMastery: Map<FsDomain, Int>, // domain -> mastery % (0..100)
        count: Int = 10
    ): List<Question> {
        val all = questions
        if (all.isEmpty()) return emptyList()

        val weightedPool = mutableListOf<Question>()

        FsDomain.entries.forEach { domain ->
            val mastery = domainMastery[domain] ?: 50
            val domainQuestions = all.filter { it.domain == domain }

            if (mastery < 60) {
                // Focus on growth: pick easier and standard questions
                val easyAndMed = domainQuestions.filter { it.difficulty != QuestionDifficulty.HARDER }
                weightedPool.addAll(easyAndMed)
                weightedPool.addAll(domainQuestions) // double weight
            } else if (mastery >= 80) {
                // High mastery: introduce harder questions
                val hardQuestions = domainQuestions.filter { it.difficulty == QuestionDifficulty.HARDER }
                if (hardQuestions.isNotEmpty()) weightedPool.addAll(hardQuestions)
                else weightedPool.addAll(domainQuestions.take(1))
            } else {
                weightedPool.addAll(domainQuestions)
            }
        }

        val finalPool = if (weightedPool.isEmpty()) all else weightedPool
        return finalPool.shuffled().distinctBy { it.id }.take(count.coerceAtLeast(1).coerceAtMost(all.size))
    }
}
