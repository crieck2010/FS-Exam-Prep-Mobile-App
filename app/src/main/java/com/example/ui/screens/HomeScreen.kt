package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Architecture
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.SatelliteAlt
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Achievement
import com.example.model.AppThemeMode
import com.example.model.CompletedModule
import com.example.model.DomainStat
import com.example.model.FsDomain
import com.example.model.LeaderboardUser
import com.example.model.UserProgress
import com.example.ui.theme.TabularNumStyle
import com.example.ui.theme.calm
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    userProgress: UserProgress,
    completedModules: List<CompletedModule>,
    domainStats: Map<FsDomain, DomainStat>,
    achievements: List<Achievement>,
    leaderboard: List<LeaderboardUser>,
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
    var domainFilter by remember { mutableStateOf("All") }
    var showThemeDialog by remember { mutableStateOf(false) }

    val unlockedBadges = remember(achievements) { achievements.count { it.isUnlocked } }
    val myLeaderboardEntry = remember(leaderboard) { leaderboard.find { it.isCurrentUser } }

    Scaffold(
        containerColor = MaterialTheme.calm.bg,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onOpenQuizConfig,
                containerColor = MaterialTheme.calm.accent,
                contentColor = Color.White,
                shape = RoundedCornerShape(8.dp),
                elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp),
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .testTag("fab_start_practice")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Start Practice",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Practice",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        color = Color.White
                    )
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(10.dp))
                // Top Header Row with Theme Selector & Muted Streak Tracker
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "NCEES FS Exam Prep",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.calm.ink
                        )
                        Text(
                            text = "Fundamentals of Surveying",
                            fontSize = 13.sp,
                            color = MaterialTheme.calm.steel
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Theme Toggle Button
                        IconButton(
                            onClick = { showThemeDialog = true },
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(MaterialTheme.calm.surface)
                                .border(1.dp, MaterialTheme.calm.divider, RoundedCornerShape(6.dp))
                                .testTag("theme_selector_btn")
                        ) {
                            Icon(
                                imageVector = when (currentThemeMode) {
                                    AppThemeMode.SYSTEM -> Icons.Default.BrightnessAuto
                                    AppThemeMode.LIGHT -> Icons.Default.LightMode
                                    AppThemeMode.DARK -> Icons.Default.DarkMode
                                },
                                contentDescription = "Change appearance",
                                tint = MaterialTheme.calm.steel,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Streak Tracker Pill — Muted line-style
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(MaterialTheme.calm.surface)
                                .border(1.dp, MaterialTheme.calm.divider, RoundedCornerShape(6.dp))
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                                .testTag("streak_tracker_pill")
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.LocalFireDepartment,
                                    contentDescription = "Streak",
                                    tint = MaterialTheme.calm.steel,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${userProgress.streakDays}d Streak",
                                    style = TabularNumStyle.copy(
                                        color = MaterialTheme.calm.ink,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // Daily Target & Overall Progress Hero Card (Flat, hairline border)
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("progress_card"),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.calm.surface),
                    border = BorderStroke(1.dp, MaterialTheme.calm.divider),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Daily Goal Progress",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.calm.ink
                            )
                            Text(
                                text = "${userProgress.questionsAnsweredToday}/${userProgress.dailyGoalTarget} questions",
                                style = TabularNumStyle.copy(
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.calm.accentBright
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Thin progress bar (4dp stroke)
                        LinearProgressIndicator(
                            progress = { userProgress.dailyProgressFraction },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp)),
                            color = MaterialTheme.calm.accentBright,
                            trackColor = MaterialTheme.calm.divider
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            StatBox(
                                label = "Total Solved",
                                value = "${userProgress.totalQuestionsAnswered}",
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            StatBox(
                                label = "Accuracy",
                                value = "${userProgress.overallAccuracy}%",
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            StatBox(
                                label = "Modules",
                                value = "${completedModules.size}",
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // Gamification Bar: Badges & Leaderboard (Muted line-style rows, zero bright rewards)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.calm.surface)
                            .border(1.dp, MaterialTheme.calm.divider, RoundedCornerShape(8.dp))
                            .clickable { onOpenAchievements() }
                            .padding(12.dp)
                            .testTag("home_achievements_card")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.EmojiEvents,
                                contentDescription = null,
                                tint = MaterialTheme.calm.steel,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Badges",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.calm.ink
                                )
                                Text(
                                    text = "$unlockedBadges/${achievements.size} Unlocked",
                                    style = TabularNumStyle.copy(
                                        fontSize = 12.sp,
                                        color = MaterialTheme.calm.steel
                                    )
                                )
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.calm.surface)
                            .border(1.dp, MaterialTheme.calm.divider, RoundedCornerShape(8.dp))
                            .clickable { onOpenLeaderboard() }
                            .padding(12.dp)
                            .testTag("home_leaderboard_card")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Leaderboard,
                                contentDescription = null,
                                tint = MaterialTheme.calm.steel,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Leaderboard",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.calm.ink
                                )
                                Text(
                                    text = if (myLeaderboardEntry != null) "Rank #${myLeaderboardEntry.rank}" else "Rank #4",
                                    style = TabularNumStyle.copy(
                                        fontSize = 12.sp,
                                        color = MaterialTheme.calm.steel
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // Quick Tools: Flashcards & Study Guide
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickToolCard(
                        title = "Flashcard Mode",
                        subtitle = "Swipe-to-reveal formulas",
                        icon = Icons.Default.Style,
                        modifier = Modifier.weight(1f),
                        onClick = onOpenFlashcards,
                        testTag = "home_flashcards_btn"
                    )

                    QuickToolCard(
                        title = "Study Guide",
                        subtitle = "Key exam cheat-sheet",
                        icon = Icons.Default.MenuBook,
                        modifier = Modifier.weight(1f),
                        onClick = onOpenStudyGuide,
                        testTag = "home_study_guide_btn"
                    )
                }
            }

            // Mock Exam Mode Hero Card (Flat, solid surface, no gradients)
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.calm.surface)
                        .border(1.dp, MaterialTheme.calm.divider, RoundedCornerShape(8.dp))
                        .padding(18.dp)
                        .testTag("mock_exam_card")
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(MaterialTheme.calm.accentContainer)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Timer,
                                        contentDescription = null,
                                        tint = MaterialTheme.calm.accentBright,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "FULL CBT SIMULATION",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.calm.accentBright
                                    )
                                }
                            }

                            Text(
                                text = "Timed 90m",
                                style = TabularNumStyle.copy(
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.calm.steel
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Full-Length NCEES FS Mock Exam",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.calm.ink
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "32 exam questions covering all 8 domains per official specifications. Includes live countdown timer, question palette, flagging, and domain diagnostic report.",
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            color = MaterialTheme.calm.steel
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = onStartMockExam,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.calm.accent,
                                contentColor = Color.White
                            ),
                            elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("btn_launch_mock_exam")
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Start Mock Exam",
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }

            // Adaptive Learning Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.calm.surface)
                        .border(1.dp, MaterialTheme.calm.divider, RoundedCornerShape(8.dp))
                        .padding(18.dp)
                        .testTag("adaptive_learning_card")
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = MaterialTheme.calm.steel,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Adaptive Learning System",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.calm.ink
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Dynamically adjusts question difficulty and domain focus based on your weak areas while advancing curve geometry.",
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            color = MaterialTheme.calm.steel
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = onStartAdaptiveQuiz,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.calm.accent,
                                contentColor = Color.White
                            ),
                            elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("btn_launch_adaptive_drill")
                        ) {
                            Text(
                                text = "Start Adaptive Practice (10 Qs)",
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            // Domains & Practice Picker Section
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Exam Domains",
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.calm.ink
                            )
                            Text(
                                text = "Select a specific domain to answer practice questions",
                                fontSize = 14.sp,
                                color = MaterialTheme.calm.steel
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("All", "Strengths", "Needs Work").forEach { filter ->
                            val isSelected = domainFilter == filter
                            val chipBg = if (isSelected) MaterialTheme.calm.accentContainer else MaterialTheme.calm.surface
                            val chipBorder = if (isSelected) MaterialTheme.calm.accent else MaterialTheme.calm.divider
                            val chipText = if (isSelected) MaterialTheme.calm.accentBright else MaterialTheme.calm.steel

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(chipBg)
                                    .border(1.dp, chipBorder, RoundedCornerShape(6.dp))
                                    .clickable { domainFilter = filter }
                                    .padding(horizontal = 14.dp, vertical = 7.dp)
                            ) {
                                Text(
                                    text = filter,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                    color = chipText
                                )
                            }
                        }
                    }
                }
            }

            // Filtered Domain Cards (Flat surface rows separated by hairline dividers)
            val filteredDomains = FsDomain.entries.filter { domain ->
                val stat = domainStats[domain] ?: DomainStat(domain)
                when (domainFilter) {
                    "Strengths" -> stat.isStrength
                    "Needs Work" -> stat.isNeedsWork
                    else -> true
                }
            }

            items(filteredDomains) { domain ->
                val stat = domainStats[domain] ?: DomainStat(domain)
                DomainCard(
                    domain = domain,
                    stat = stat,
                    onStartQuiz = { onStartDomainQuiz(domain) }
                )
            }

            // Completed Practice Modules Section
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Completed Practice Modules",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.calm.ink
                    )
                    Text(
                        text = "Review answers, explanations, and score breakdowns",
                        fontSize = 14.sp,
                        color = MaterialTheme.calm.steel
                    )
                }
            }

            if (completedModules.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.calm.surface)
                            .border(1.dp, MaterialTheme.calm.divider, RoundedCornerShape(8.dp))
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No completed modules yet. Start your first quiz below!",
                            color = MaterialTheme.calm.steel,
                            fontSize = 14.sp
                        )
                    }
                }
            } else {
                items(completedModules) { module ->
                    CompletedModuleCard(
                        module = module,
                        onReview = { onReviewModule(module) }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }

    // Appearance / Theme Picker Dialog
    if (showThemeDialog) {
        AlertDialog(
            onDismissRequest = { showThemeDialog = false },
            title = {
                Text(
                    text = "Appearance Theme",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.calm.ink
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Choose your preferred color theme:",
                        fontSize = 13.sp,
                        color = MaterialTheme.calm.steel
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    AppThemeMode.entries.forEach { mode ->
                        val isSelected = currentThemeMode == mode
                        val itemBg = if (isSelected) MaterialTheme.calm.accentContainer else MaterialTheme.calm.surface
                        val itemBorder = if (isSelected) MaterialTheme.calm.accent else MaterialTheme.calm.divider

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(itemBg)
                                .border(1.dp, itemBorder, RoundedCornerShape(6.dp))
                                .clickable {
                                    onSelectThemeMode(mode)
                                    showThemeDialog = false
                                }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = {
                                    onSelectThemeMode(mode)
                                    showThemeDialog = false
                                },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = MaterialTheme.calm.accent,
                                    unselectedColor = MaterialTheme.calm.steel
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = mode.title,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (isSelected) MaterialTheme.calm.accentBright else MaterialTheme.calm.ink
                                )
                                Text(
                                    text = mode.subtitle,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.calm.steel
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showThemeDialog = false }) {
                    Text("Done", color = MaterialTheme.calm.accentBright)
                }
            },
            containerColor = MaterialTheme.calm.surface,
            shape = RoundedCornerShape(8.dp)
        )
    }
}

@Composable
private fun StatBox(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(MaterialTheme.calm.surface)
            .border(1.dp, MaterialTheme.calm.divider, RoundedCornerShape(6.dp))
            .padding(vertical = 10.dp, horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = value,
                style = TabularNumStyle.copy(
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.calm.ink
                )
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                fontSize = 12.sp,
                color = MaterialTheme.calm.steel
            )
        }
    }
}

@Composable
private fun QuickToolCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    testTag: String
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.calm.surface)
            .border(1.dp, MaterialTheme.calm.divider, RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(12.dp)
            .testTag(testTag)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(MaterialTheme.calm.accentContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.calm.accentBright,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.calm.ink
                )
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = MaterialTheme.calm.steel
                )
            }
        }
    }
}

@Composable
private fun DomainCard(
    domain: FsDomain,
    stat: DomainStat,
    onStartQuiz: () -> Unit
) {
    val icon = when (domain) {
        FsDomain.APPLIED_MATH_STATISTICS -> Icons.Default.Calculate
        FsDomain.BOUNDARY_LAW_REAL_PROPERTY -> Icons.Default.Gavel
        FsDomain.BUSINESS_CONCEPTS_ETHICS -> Icons.Default.MenuBook
        FsDomain.MAPPING_PROCESSES_METHODS -> Icons.Default.SatelliteAlt
        FsDomain.SURVEY_COMPUTATIONS -> Icons.Default.Architecture
        FsDomain.SURVEYING_PRINCIPLES_GEODESY -> Icons.Default.Public
        FsDomain.SURVEYING_PROCESSES_METHODS -> Icons.Default.Construction
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.calm.surface)
            .border(1.dp, MaterialTheme.calm.divider, RoundedCornerShape(8.dp))
            .clickable { onStartQuiz() }
            .padding(16.dp)
            .testTag("domain_card_${domain.shortCode.lowercase()}")
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(MaterialTheme.calm.accentContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.calm.accentBright,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = domain.title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.calm.ink
                    )
                    Text(
                        text = domain.description,
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                        fontSize = 12.sp,
                        color = MaterialTheme.calm.steel
                    )
                }
                if (stat.isStrength) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(MaterialTheme.calm.goContainer)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Strength",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.calm.go
                        )
                    }
                } else if (stat.isNeedsWork) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(MaterialTheme.calm.warnContainer)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Needs Work",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.calm.warn
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            val accuracy = if (stat.attempted > 0) stat.accuracyPercentage else 0
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (stat.attempted > 0) "Accuracy: $accuracy% (${stat.correct}/${stat.attempted})" else "Not tested yet",
                    style = TabularNumStyle.copy(
                        fontSize = 12.sp,
                        color = MaterialTheme.calm.steel
                    )
                )
                Text(
                    text = "Practice ›",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.calm.accentBright
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            LinearProgressIndicator(
                progress = { (accuracy.toFloat() / 100f).coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = when {
                    accuracy >= 70 -> MaterialTheme.calm.go
                    accuracy >= 50 -> MaterialTheme.calm.accentBright
                    else -> MaterialTheme.calm.warn
                },
                trackColor = MaterialTheme.calm.divider
            )
        }
    }
}

@Composable
private fun CompletedModuleCard(
    module: CompletedModule,
    onReview: () -> Unit
) {
    val dateStr = remember(module.timestamp) {
        val sdf = SimpleDateFormat("MMM d, h:mm a", Locale.getDefault())
        sdf.format(Date(module.timestamp))
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.calm.surface)
            .border(1.dp, MaterialTheme.calm.divider, RoundedCornerShape(8.dp))
            .clickable { onReview() }
            .padding(14.dp)
            .testTag("completed_module_${module.id}")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = module.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.calm.ink
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$dateStr • ${module.totalQuestions} questions",
                    fontSize = 12.sp,
                    color = MaterialTheme.calm.steel
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(horizontalAlignment = Alignment.End) {
                val isPassing = module.percentage >= 70
                val badgeBg = if (isPassing) MaterialTheme.calm.goContainer else MaterialTheme.calm.warnContainer
                val badgeText = if (isPassing) MaterialTheme.calm.go else MaterialTheme.calm.warn

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(badgeBg)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${module.correctCount}/${module.totalQuestions} (${module.percentage}%)",
                        style = TabularNumStyle.copy(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = badgeText
                        )
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Review ›",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.calm.accentBright
                )
            }
        }
    }
}
