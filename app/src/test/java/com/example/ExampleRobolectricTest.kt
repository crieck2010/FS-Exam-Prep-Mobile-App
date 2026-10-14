package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.QuestionBank
import com.example.data.QuizRepository
import com.example.model.FsDomain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
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
        assertTrue("Questions must not be empty", QuestionBank.questions.isNotEmpty())
        val curveQuestions = QuestionBank.getQuestionsForQuiz(
            domain = FsDomain.CURVES,
            count = 5,
            focusOnGrowth = false
        )
        assertTrue(curveQuestions.isNotEmpty())
        curveQuestions.forEach { q ->
            assertEquals(FsDomain.CURVES, q.domain)
        }
    }

    @Test
    fun `verify mock exam generates questions spanning all domains`() {
        val mockQuestions = QuestionBank.getMockExamQuestions(count = 32)
        assertEquals(32, mockQuestions.size)
        val representedDomains = mockQuestions.map { it.domain }.toSet()
        assertEquals("All 8 domains must be covered in mock exam", 8, representedDomains.size)
    }

    @Test
    fun `verify adaptive question selection`() {
        val lowMasteryMap = mapOf(
            FsDomain.GEODESY_AND_PROJECTIONS to 25,
            FsDomain.APPLIED_MATH_COGO to 30
        )
        val adaptiveQuestions = QuestionBank.getAdaptiveQuestions(lowMasteryMap, count = 10)
        assertTrue(adaptiveQuestions.isNotEmpty())
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
