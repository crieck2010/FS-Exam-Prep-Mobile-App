package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.QuestionBank
import com.example.data.QuizRepository
import com.example.model.FsDomain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("FS Prep", appName)
    }

    @Test
    fun `verify question bank loaded and domain filtering works`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        QuestionBank.initialize(context)

        assertNull("QuestionBank loadError must be null", QuestionBank.loadError)
        assertEquals("Questions count must be 480", 480, QuestionBank.questions.size)

        val compQuestions = QuestionBank.getQuestionsForQuiz(
            domain = FsDomain.SURVEY_COMPUTATIONS,
            count = 10,
            focusOnGrowth = false
        )
        assertTrue(compQuestions.isNotEmpty())
        compQuestions.forEach { q ->
            assertEquals(FsDomain.SURVEY_COMPUTATIONS, q.domain)
            assertTrue(q.id.isNotEmpty())
            assertTrue(q.questionText.isNotEmpty())
            assertEquals(4, q.options.size)
            assertTrue(q.correctIndex in 0..3)
            assertTrue(q.explanation.isNotEmpty())
        }
    }

    @Test
    fun `verify mock exam generates 110 questions spanning all 7 domains proportionally`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        QuestionBank.initialize(context)

        val mockQuestions = QuestionBank.getMockExamQuestions()
        assertEquals("Default mock exam size must be 110", 110, mockQuestions.size)

        val domainCounts = mockQuestions.groupBy { it.domain }
        assertEquals("All 7 domains must be covered in mock exam", 7, domainCounts.size)

        // Check proportional distribution
        // survey-computations is 120/480 = 25% -> 27 or 28 questions
        assertTrue("survey-computations should have ~28 questions", (domainCounts[FsDomain.SURVEY_COMPUTATIONS]?.size ?: 0) in 25..30)
    }

    @Test
    fun `verify adaptive question selection`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        QuestionBank.initialize(context)

        val lowMasteryMap = mapOf(
            FsDomain.SURVEYING_PRINCIPLES_GEODESY to 25,
            FsDomain.SURVEY_COMPUTATIONS to 30
        )
        val adaptiveQuestions = QuestionBank.getAdaptiveQuestions(lowMasteryMap, count = 10)
        assertTrue(adaptiveQuestions.isNotEmpty())
        assertEquals(10, adaptiveQuestions.size)
    }

    @Test
    fun `verify achievements and leaderboard initialized`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val repo = QuizRepository(context)
        val achievements = repo.achievements.value
        assertTrue("Achievements should be seeded", achievements.isNotEmpty())

        val leaderboard = repo.leaderboard.value
        assertTrue("Leaderboard should be populated", leaderboard.isNotEmpty())
        val hasCurrentUser = leaderboard.any { it.isCurrentUser }
        assertTrue("Leaderboard must include current user entry", hasCurrentUser)
    }
}
