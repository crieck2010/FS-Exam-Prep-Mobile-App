package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CompletedModule
import com.example.model.DomainStat
import com.example.model.FsDomain
import com.example.ui.components.ArcScoreGauge
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.TextPrimaryDark
import com.example.ui.theme.TextSecondaryDark

@Composable
fun ScoreSummaryScreen(
    module: CompletedModule,
    domainStats: Map<FsDomain, DomainStat>,
    onReviewQuestions: () -> Unit,
    onTakeAnotherQuiz: () -> Unit,
    onOpenFlashcards: () -> Unit,
    onOpenStudyGuide: () -> Unit,
    onNavigateHome: () -> Unit
) {
    BackHandler {
        onNavigateHome()
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = onNavigateHome,
                    modifier = Modifier.testTag("summary_close_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                Text(
                    text = "Score",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.width(48.dp)) // balance spacing
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Arc Score Gauge (30/40 Correct, 0 skipped • 10 incorrect)
            ArcScoreGauge(
                correctCount = module.correctCount,
                totalCount = module.totalQuestions,
                skippedCount = module.skippedCount,
                incorrectCount = module.incorrectCount
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Dynamic Motivation Headline
            val headline = when {
                module.percentage >= 85 -> "Outstanding performance! You are well-prepared."
                module.percentage >= 70 -> "Solid progress! Keep up the good work."
                module.percentage >= 50 -> "Good foundation! Focus on the growth areas below."
                else -> "Great practice run! Target the core areas below to level up."
            }
            Text(
                text = headline,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 30.sp,
                color = TextPrimaryDark
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Strengths Section
            Text(
                text = "Strengths",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimaryDark
            )
            Spacer(modifier = Modifier.height(10.dp))

            // Strength Bullet 1
            BulletInsight(
                domainTitle = "Boundary Law and Professional Principles",
                detail = "You show a strong understanding of essential legal concepts, correctly answering questions on fee simple absolute, accretion, eminent domain, and sequential conveyances. You also demonstrated a solid grasp of professional obligations and standards."
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Strength Bullet 2
            BulletInsight(
                domainTitle = "Surveying Methods and Field Equipment",
                detail = "Your practical knowledge is excellent. You comfortably navigated questions regarding leveling circuits, equipment calibration (peg tests), systematic errors with steel tapes, and basic photogrammetry principles."
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Focus Areas Section
            Text(
                text = "Focus areas",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimaryDark
            )
            Spacer(modifier = Modifier.height(10.dp))

            // Focus Area Bullet 1
            BulletInsight(
                domainTitle = "Geodesy and Map Projections",
                detail = "Take some time to review the specifics of coordinate systems and datums. Focus on understanding grid scale factors in Lambert Conformal Conic and UTM projections, and memorize fundamental reference frames like WGS84 and the NAVD88 origin."
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Focus Area Bullet 2
            BulletInsight(
                domainTitle = "Applied Math and Unit Conversions",
                detail = "Watch out for tricky unit conversions and quadrant directions. Double-check your steps when converting cubic feet to cubic yards (like in the average end area method), verifying bearing quadrants from azimuths, and applying statistical formulas."
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Keep Learning Section
            Text(
                text = "Keep Learning",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimaryDark
            )
            Spacer(modifier = Modifier.height(14.dp))

            // Card 1: Revise (Flashcards)
            LearningActionCard(
                badge = "Flashcards",
                title = "Revise",
                description = "Create a complete set of flashcards from all your quiz material. Good for quick review and mastering key concepts.",
                icon = Icons.Default.Style,
                onClick = onOpenFlashcards,
                testTag = "action_flashcards"
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Card 2: Learn (Study guide)
            LearningActionCard(
                badge = "Study guide",
                title = "Learn",
                description = "Generate a comprehensive study guide based on the materials you are studying. Good for in-depth review.",
                icon = Icons.Default.MenuBook,
                onClick = onOpenStudyGuide,
                testTag = "action_study_guide"
            )

            Spacer(modifier = Modifier.height(36.dp))

            // Bottom Actions: "Review" text button and "Take another quiz" primary button
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextButton(
                    onClick = onReviewQuestions,
                    modifier = Modifier.testTag("summary_review_button")
                ) {
                    Text(
                        text = "Review",
                        color = Color(0xFFCBD5E1),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = onTakeAnotherQuiz,
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("summary_take_another_button")
                ) {
                    Text(
                        text = "Take another quiz",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
private fun BulletInsight(
    domainTitle: String,
    detail: String
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "○ ",
            fontSize = 14.sp,
            color = TextSecondaryDark,
            modifier = Modifier.padding(top = 2.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = domainTitle,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                color = TextPrimaryDark
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = detail,
                fontSize = 14.sp,
                lineHeight = 22.sp,
                color = Color(0xFFCBD5E1)
            )
        }
    }
}

@Composable
private fun LearningActionCard(
    badge: String,
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    testTag: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(DarkSurface)
            .clickable { onClick() }
            .padding(20.dp)
            .testTag(testTag)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryDark
                    )
                    Text(
                        text = badge,
                        fontSize = 12.sp,
                        color = TextSecondaryDark
                    )
                }
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = PrimaryBlue,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = description,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = Color(0xFFCBD5E1)
            )
        }
    }
}
