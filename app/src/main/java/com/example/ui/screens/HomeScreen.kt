package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Architecture
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.SatelliteAlt
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Achievement
import com.example.model.AppThemeMode
import com.example.model.CompletedModule
import com.example.model.DomainStat
import com.example.model.FsDomain
import com.example.model.LeaderboardUser
import com.example.model.UserProgress
import com.example.ui.theme.SerifSectionHeading
import com.example.ui.theme.TabularNumStyle
import com.example.ui.theme.calm
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class AppTab(val title: String, val testTag: String) {
    TODAY("Today", "tab_today"),
    PRACTICE("Practice", "tab_practice"),
    LEARN("Learn", "tab_learn"),
    PROGRESS("Progress", "tab_progress")
}

@Composable
fun HomeScreen(
    userProgress: UserProgress,
    completedModules: List<CompletedModule>,
    domainStats: Map<FsDomain, DomainStat>,
    achievements: List<Achievement>,
    leaderboard: List<LeaderboardUser>,
    userHandle: String = "Surveyor#402",
    currentThemeMode: AppThemeMode,
    onSelectThemeMode: (AppThemeMode) -> Unit,
    onStartDomainQuiz: (FsDomain) -> Unit,
    onOpenQuizConfig: () -> Unit,
    onStartMockExam: () -> Unit,
    onStartAdaptiveQuiz: () -> Unit,
    onOpenStudyGuide: () -> Unit,
    onOpenFlashcards: () -> Unit,
    onOpenAchievements: () -> Unit,
    onOpenLeaderboard: () -> Unit,
    onReviewModule: (CompletedModule) -> Unit
) {
    var selectedTab by rememberSaveable { mutableStateOf(AppTab.TODAY) }
    var showThemeDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = MaterialTheme.calm.bg,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.calm.surface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "FS Exam Prep",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.calm.ink
                    )
                    IconButton(
                        onClick = { showThemeDialog = true },
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("settings_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Appearance & Settings",
                            tint = MaterialTheme.calm.steel,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                HorizontalDivider(color = MaterialTheme.calm.divider, thickness = 1.dp)
            }
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.calm.surface)
            ) {
                HorizontalDivider(color = MaterialTheme.calm.divider, thickness = 1.dp)
                NavigationBar(
                    containerColor = MaterialTheme.calm.surface,
                    tonalElevation = 0.dp,
                    modifier = Modifier.height(64.dp)
                ) {
                    val activeIndicatorColor = MaterialTheme.calm.accentContainer
                    val selectedColor = MaterialTheme.calm.accentBright
                    val unselectedColor = MaterialTheme.calm.steel

                    AppTab.entries.forEach { tab ->
                        val isSelected = (selectedTab == tab)
                        val icon = when (tab) {
                            AppTab.TODAY -> Icons.Default.Home
                            AppTab.PRACTICE -> Icons.Default.PlayArrow
                            AppTab.LEARN -> Icons.Default.MenuBook
                            AppTab.PROGRESS -> Icons.Default.Leaderboard
                        }

                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { selectedTab = tab },
                            icon = {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = tab.title,
                                    modifier = Modifier.size(22.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = tab.title,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = selectedColor,
                                selectedTextColor = selectedColor,
                                unselectedIconColor = unselectedColor,
                                unselectedTextColor = unselectedColor,
                                indicatorColor = activeIndicatorColor
                            ),
                            modifier = Modifier.testTag(tab.testTag)
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                AppTab.TODAY -> TodayTabContent(
                    userHandle = userHandle,
                    userProgress = userProgress,
                    completedModules = completedModules,
                    domainStats = domainStats,
                    onStartDomainQuiz = onStartDomainQuiz,
                    onStartAdaptiveQuiz = onStartAdaptiveQuiz
                )
                AppTab.PRACTICE -> PracticeTabContent(
                    domainStats = domainStats,
                    onStartDomainQuiz = onStartDomainQuiz,
                    onStartAdaptiveQuiz = onStartAdaptiveQuiz,
                    onStartMockExam = onStartMockExam,
                    onOpenQuizConfig = onOpenQuizConfig
                )
                AppTab.LEARN -> LearnTabContent(
                    onOpenStudyGuide = onOpenStudyGuide,
                    onOpenFlashcards = onOpenFlashcards
                )
                AppTab.PROGRESS -> ProgressTabContent(
                    domainStats = domainStats,
                    achievements = achievements,
                    leaderboard = leaderboard,
                    completedModules = completedModules,
                    onOpenAchievements = onOpenAchievements,
                    onOpenLeaderboard = onOpenLeaderboard,
                    onReviewModule = onReviewModule
                )
            }
        }
    }

    if (showThemeDialog) {
        ThemePickerDialog(
            currentMode = currentThemeMode,
            onSelectMode = { newMode ->
                onSelectThemeMode(newMode)
                showThemeDialog = false
            },
            onDismiss = { showThemeDialog = false }
        )
    }
}

// ============================================================================
// 1. TODAY TAB (§1.3: Minimal, in exact order, nothing else)
// ============================================================================
@Composable
private fun TodayTabContent(
    userHandle: String,
    userProgress: UserProgress,
    completedModules: List<CompletedModule>,
    domainStats: Map<FsDomain, DomainStat>,
    onStartDomainQuiz: (FsDomain) -> Unit,
    onStartAdaptiveQuiz: () -> Unit
) {
    val weakestDomain = remember(domainStats) {
        val attempted = domainStats.values.filter { it.attempted > 0 }
        val needsWork = attempted.filter { it.isNeedsWork }
        if (needsWork.isNotEmpty()) {
            needsWork.minByOrNull { it.accuracyPercentage }?.domain ?: FsDomain.SURVEY_COMPUTATIONS
        } else if (attempted.isNotEmpty()) {
            attempted.minByOrNull { it.accuracyPercentage }?.domain ?: FsDomain.SURVEY_COMPUTATIONS
        } else {
            FsDomain.SURVEY_COMPUTATIONS
        }
    }
    val weakestStat = domainStats[weakestDomain]

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            // 1. A short greeting line with the user's handle
            Text(
                text = "Hello, $userHandle",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.calm.ink
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Fundamentals of Surveying Exam Preparation",
                fontSize = 14.sp,
                color = MaterialTheme.calm.steel
            )
        }

        item {
            // 2. One readiness summary card: overall progress %, current streak, daily-goal progress
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.calm.surface)
                    .border(1.dp, MaterialTheme.calm.divider, RoundedCornerShape(8.dp))
                    .padding(20.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Overall Readiness",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.calm.steel
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${userProgress.overallAccuracy}%",
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.calm.ink,
                                style = TabularNumStyle
                            )
                        }
                    }

                    HorizontalDivider(
                        color = MaterialTheme.calm.divider,
                        thickness = 1.dp,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "${userProgress.streakDays} days",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.calm.ink,
                                style = TabularNumStyle
                            )
                            Text(
                                text = "Current streak",
                                fontSize = 12.sp,
                                color = MaterialTheme.calm.steel
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "${userProgress.questionsAnsweredToday} of ${userProgress.dailyGoalTarget}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.calm.ink,
                                style = TabularNumStyle
                            )
                            Text(
                                text = "Daily goal (questions)",
                                fontSize = 12.sp,
                                color = MaterialTheme.calm.steel
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    LinearProgressIndicator(
                        progress = { userProgress.dailyProgressFraction },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        color = MaterialTheme.calm.accent,
                        trackColor = MaterialTheme.calm.divider
                    )
                }
            }
        }

        item {
            // 3. ONE primary button: "Continue studying" or "Start today's session"
            val hasActivity = completedModules.isNotEmpty()
            val buttonText = if (hasActivity) "Continue studying" else "Start today's session"

            Button(
                onClick = {
                    if (weakestDomain != null) {
                        onStartDomainQuiz(weakestDomain)
                    } else {
                        onStartAdaptiveQuiz()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("today_primary_action_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.calm.accent,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp),
                elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp, 0.dp, 0.dp)
            ) {
                Text(
                    text = buttonText,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }

        item {
            // 4. A single "Up next" row showing the weakest domain with a Start action
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.calm.surface)
                    .border(1.dp, MaterialTheme.calm.divider, RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.calm.accentContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = getDomainIcon(weakestDomain),
                            contentDescription = null,
                            tint = MaterialTheme.calm.accentBright,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "UP NEXT • FOCUS AREA",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.calm.steel,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = weakestDomain.title,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.calm.ink
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${weakestStat?.accuracyPercentage ?: 0}% accuracy • ${weakestStat?.attempted ?: 0} questions",
                            fontSize = 12.sp,
                            color = MaterialTheme.calm.steel,
                            style = TabularNumStyle
                        )
                    }
                    Button(
                        onClick = { onStartDomainQuiz(weakestDomain) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.calm.accent,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(6.dp),
                        elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp),
                        modifier = Modifier
                            .height(36.dp)
                            .testTag("today_up_next_start")
                    ) {
                        Text("Start", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// ============================================================================
// 2. PRACTICE TAB (§1.4: Flat list with dividers, one action per row)
// ============================================================================
@Composable
private fun PracticeTabContent(
    domainStats: Map<FsDomain, DomainStat>,
    onStartDomainQuiz: (FsDomain) -> Unit,
    onStartAdaptiveQuiz: () -> Unit,
    onStartMockExam: () -> Unit,
    onOpenQuizConfig: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Practice Modes",
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.calm.ink
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Adaptive Practice
        item {
            ActionRow(
                icon = Icons.Default.AutoAwesome,
                title = "Adaptive Practice",
                subtitle = "Dynamic 10-question drill weighted to your weakest domains",
                actionLabel = "Start",
                onAction = onStartAdaptiveQuiz,
                testTag = "practice_adaptive_row"
            )
            HorizontalDivider(color = MaterialTheme.calm.divider, thickness = 1.dp)
        }

        // Mock Exam (Mock card: B2, B10, B11)
        item {
            ActionRow(
                icon = Icons.Default.Timer,
                title = "Full-Length FS Mock Exam",
                subtitle = "Timed 5h 20m • 110 exam questions covering all 7 FS domains. Includes live countdown timer, question palette, flagging, and domain diagnostic report.",
                actionLabel = "Start",
                onAction = onStartMockExam,
                testTag = "practice_mock_exam_row"
            )
            HorizontalDivider(color = MaterialTheme.calm.divider, thickness = 1.dp)
        }

        // Quiz Configuration (FAB destination per §1.5)
        item {
            ActionRow(
                icon = Icons.Default.Tune,
                title = "Custom Quiz Configuration",
                subtitle = "Configure domains, question count (5–30), and difficulty",
                actionLabel = "Configure",
                onAction = onOpenQuizConfig,
                testTag = "practice_config_row"
            )
            HorizontalDivider(color = MaterialTheme.calm.divider, thickness = 1.dp)
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Knowledge Areas (7 Domains)",
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.calm.ink
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Targeted domain drills with immediate answer explanations",
                fontSize = 13.sp,
                color = MaterialTheme.calm.steel
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        items(FsDomain.entries) { domain ->
            val stat = domainStats[domain] ?: DomainStat(domain = domain)
            DomainQuizRow(
                domain = domain,
                stat = stat,
                onStartQuiz = { onStartDomainQuiz(domain) }
            )
            HorizontalDivider(color = MaterialTheme.calm.divider, thickness = 1.dp)
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// ============================================================================
// 3. LEARN TAB (§1.4: Study Guide and Flashcards, one row each)
// ============================================================================
@Composable
private fun LearnTabContent(
    onOpenStudyGuide: () -> Unit,
    onOpenFlashcards: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Study Resources",
                style = SerifSectionHeading,
                color = MaterialTheme.calm.ink
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Core principles, reference formulas, and active recall",
                fontSize = 14.sp,
                color = MaterialTheme.calm.steel
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            ActionRow(
                icon = Icons.Default.MenuBook,
                title = "Study Guide",
                subtitle = "Key formulas, reference values, and exam tips across core surveying topics",
                actionLabel = "Open",
                onAction = onOpenStudyGuide,
                testTag = "learn_study_guide_row"
            )
            HorizontalDivider(color = MaterialTheme.calm.divider, thickness = 1.dp)
        }

        item {
            ActionRow(
                icon = Icons.Default.Style,
                title = "Flashcards",
                subtitle = "Active-recall cards for rapid revision of formulas, definitions, and field procedures",
                actionLabel = "Study",
                onAction = onOpenFlashcards,
                testTag = "learn_flashcards_row"
            )
            HorizontalDivider(color = MaterialTheme.calm.divider, thickness = 1.dp)
        }
    }
}

// ============================================================================
// 4. PROGRESS TAB (§1.4: Detailed stats, badges, leaderboard, history, disclaimer)
// ============================================================================
@Composable
private fun ProgressTabContent(
    domainStats: Map<FsDomain, DomainStat>,
    achievements: List<Achievement>,
    leaderboard: List<LeaderboardUser>,
    completedModules: List<CompletedModule>,
    onOpenAchievements: () -> Unit,
    onOpenLeaderboard: () -> Unit,
    onReviewModule: (CompletedModule) -> Unit
) {
    val unlockedCount = remember(achievements) { achievements.count { it.isUnlocked } }
    val myRankEntry = remember(leaderboard) { leaderboard.find { it.isCurrentUser } }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Your Progress & Stats",
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.calm.ink
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Badges & Achievements row
        item {
            ActionRow(
                icon = Icons.Default.EmojiEvents,
                title = "Badges & Achievements",
                subtitle = "$unlockedCount of ${achievements.size} Unlocked • Tap to view all",
                actionLabel = "View All",
                onAction = onOpenAchievements,
                testTag = "progress_achievements_row"
            )
            HorizontalDivider(color = MaterialTheme.calm.divider, thickness = 1.dp)
        }

        // Leaderboard row
        item {
            val rankText = if (myRankEntry != null) {
                "Rank #${myRankEntry.rank} • ${myRankEntry.accuracy}% accuracy • ${myRankEntry.questionsAnswered} answered"
            } else {
                "Example standings to benchmark your accuracy and streaks"
            }

            ActionRow(
                icon = Icons.Default.Leaderboard,
                title = "Sample Leaderboard",
                subtitle = rankText,
                actionLabel = "View All",
                onAction = onOpenLeaderboard,
                testTag = "progress_leaderboard_row"
            )
            HorizontalDivider(color = MaterialTheme.calm.divider, thickness = 1.dp)
        }

        // Knowledge Area Diagnostics section
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Knowledge Area Diagnostics",
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.calm.ink
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Performance breakdown across all 7 FS domains",
                fontSize = 13.sp,
                color = MaterialTheme.calm.steel
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        items(FsDomain.entries) { domain ->
            val stat = domainStats[domain] ?: DomainStat(domain = domain)
            DomainDiagnosticRow(domain = domain, stat = stat)
            HorizontalDivider(color = MaterialTheme.calm.divider, thickness = 1.dp)
        }

        // Completed Practice Modules History
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Recent Practice Sessions",
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.calm.ink
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Review answered questions and detailed explanations",
                fontSize = 13.sp,
                color = MaterialTheme.calm.steel
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        if (completedModules.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No completed practice sessions yet. Start a quiz to track your history.",
                        fontSize = 13.sp,
                        color = MaterialTheme.calm.steel,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            items(completedModules) { module ->
                CompletedModuleRow(module = module, onReview = { onReviewModule(module) })
                HorizontalDivider(color = MaterialTheme.calm.divider, thickness = 1.dp)
            }
        }

        // §2.3 Non-affiliation disclaimer verbatim at bottom of Progress
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Independent study tool. Not affiliated with or endorsed by NCEES. All questions are original practice material.",
                fontSize = 11.sp,
                color = MaterialTheme.calm.steel,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// ============================================================================
// HELPER COMPOSABLES (Calm Pro Flat Style)
// ============================================================================
@Composable
private fun ActionRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    actionLabel: String,
    onAction: () -> Unit,
    testTag: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onAction() }
            .padding(vertical = 14.dp)
            .testTag(testTag),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.calm.surface)
                .border(1.dp, MaterialTheme.calm.divider, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.calm.accent,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.calm.ink
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = MaterialTheme.calm.steel,
                lineHeight = 16.sp
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        TextButton(
            onClick = onAction,
            colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.calm.accent)
        ) {
            Text(
                text = actionLabel,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.width(2.dp))
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
private fun DomainQuizRow(
    domain: FsDomain,
    stat: DomainStat,
    onStartQuiz: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onStartQuiz() }
            .padding(vertical = 12.dp)
            .testTag("practice_domain_${domain.slug}"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(MaterialTheme.calm.surface)
                .border(1.dp, MaterialTheme.calm.divider, RoundedCornerShape(6.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = getDomainIcon(domain),
                contentDescription = null,
                tint = MaterialTheme.calm.accent,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = domain.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.calm.ink,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (stat.isStrength) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Strength",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.calm.go,
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(MaterialTheme.calm.goContainer)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                } else if (stat.isNeedsWork) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Needs Work",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.calm.warn,
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(MaterialTheme.calm.warnContainer)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "${stat.accuracyPercentage}% accuracy • ${stat.attempted} answered",
                fontSize = 12.sp,
                color = MaterialTheme.calm.steel,
                style = TabularNumStyle
            )
        }
        Button(
            onClick = onStartQuiz,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.calm.accent,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(6.dp),
            elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp),
            modifier = Modifier.height(32.dp)
        ) {
            Text("Quiz", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun DomainDiagnosticRow(
    domain: FsDomain,
    stat: DomainStat
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = domain.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.calm.ink
            )
            Text(
                text = "${stat.accuracyPercentage}% (${stat.correct}/${stat.attempted})",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (stat.isStrength) MaterialTheme.calm.go else MaterialTheme.calm.ink,
                style = TabularNumStyle
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        val fraction = if (stat.attempted > 0) (stat.correct.toFloat() / stat.attempted.toFloat()).coerceIn(0f, 1f) else 0f
        LinearProgressIndicator(
            progress = { fraction },
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp)),
            color = if (stat.isStrength) MaterialTheme.calm.go else MaterialTheme.calm.accent,
            trackColor = MaterialTheme.calm.divider
        )
    }
}

@Composable
private fun CompletedModuleRow(
    module: CompletedModule,
    onReview: () -> Unit
) {
    val dateStr = remember(module.timestamp) {
        SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(Date(module.timestamp))
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onReview() }
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = module.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.calm.ink
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "$dateStr • ${module.totalQuestions} questions",
                fontSize = 12.sp,
                color = MaterialTheme.calm.steel,
                style = TabularNumStyle
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "${module.percentage}%",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = if (module.percentage >= 70) MaterialTheme.calm.go else MaterialTheme.calm.steel,
                style = TabularNumStyle
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Review",
                tint = MaterialTheme.calm.steel,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

private fun getDomainIcon(domain: FsDomain): ImageVector = when (domain) {
    FsDomain.APPLIED_MATH_STATISTICS -> Icons.Default.Calculate
    FsDomain.BOUNDARY_LAW_REAL_PROPERTY -> Icons.Default.Gavel
    FsDomain.BUSINESS_CONCEPTS_ETHICS -> Icons.Default.MenuBook
    FsDomain.MAPPING_PROCESSES_METHODS -> Icons.Default.SatelliteAlt
    FsDomain.SURVEY_COMPUTATIONS -> Icons.Default.Architecture
    FsDomain.SURVEYING_PRINCIPLES_GEODESY -> Icons.Default.Public
    FsDomain.SURVEYING_PROCESSES_METHODS -> Icons.Default.Construction
}

// ============================================================================
// THEME PICKER DIALOG (§1.2: Moved to Settings Gear Icon in Top App Bar)
// ============================================================================
@Composable
private fun ThemePickerDialog(
    currentMode: AppThemeMode,
    onSelectMode: (AppThemeMode) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.calm.surface,
        title = {
            Text(
                text = "Appearance",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.calm.ink
            )
        },
        text = {
            Column {
                Text(
                    text = "Choose your preferred theme mode for studying:",
                    fontSize = 13.sp,
                    color = MaterialTheme.calm.steel
                )
                Spacer(modifier = Modifier.height(14.dp))
                AppThemeMode.entries.forEach { mode ->
                    val label = when (mode) {
                        AppThemeMode.SYSTEM -> "System default"
                        AppThemeMode.LIGHT -> "Light mode"
                        AppThemeMode.DARK -> "Dark mode"
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelectMode(mode) }
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (currentMode == mode),
                            onClick = { onSelectMode(mode) },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = MaterialTheme.calm.accent,
                                unselectedColor = MaterialTheme.calm.steel
                            )
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = label,
                            fontSize = 14.sp,
                            fontWeight = if (currentMode == mode) FontWeight.SemiBold else FontWeight.Normal,
                            color = MaterialTheme.calm.ink
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.calm.accent)
            ) {
                Text("Close", fontWeight = FontWeight.SemiBold)
            }
        }
    )
}
