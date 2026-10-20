package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Achievement
import com.example.model.AchievementCategory
import com.example.ui.theme.TabularNumStyle
import com.example.ui.theme.calm

@Composable
fun AchievementsScreen(
    achievements: List<Achievement>,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    var selectedCategory by remember { mutableStateOf<AchievementCategory?>(null) }

    val unlockedCount = remember(achievements) { achievements.count { it.isUnlocked } }
    val totalCount = achievements.size

    val filteredAchievements = remember(achievements, selectedCategory) {
        if (selectedCategory == null) achievements
        else achievements.filter { it.category == selectedCategory }
    }

    Scaffold(
        containerColor = MaterialTheme.calm.bg,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.calm.surface)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack, modifier = Modifier.testTag("achievements_back_button")) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.calm.steel
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Milestones & Badges",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.calm.ink
                    )
                    Text(
                        text = "$unlockedCount of $totalCount unlocked",
                        style = TabularNumStyle.copy(
                            fontSize = 12.sp,
                            color = MaterialTheme.calm.steel
                        )
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
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(4.dp))
                // Overview Hero Card (Flat, hairline border)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.calm.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.calm.divider),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.EmojiEvents,
                                contentDescription = null,
                                tint = MaterialTheme.calm.steel,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Achievement Progress",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.calm.ink
                                )
                                Text(
                                    text = "$unlockedCount / $totalCount badges earned",
                                    style = TabularNumStyle.copy(
                                        fontSize = 13.sp,
                                        color = MaterialTheme.calm.steel
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        LinearProgressIndicator(
                            progress = { if (totalCount > 0) (unlockedCount.toFloat() / totalCount) else 0f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(3.dp)
                                .clip(RoundedCornerShape(2.dp)),
                            color = MaterialTheme.calm.accentBright,
                            trackColor = MaterialTheme.calm.divider
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Earn badges for study consistency, high accuracy, and domain mastery.",
                            fontSize = 13.sp,
                            color = MaterialTheme.calm.steel
                        )
                    }
                }
            }

            // Category Filter Row
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChipItem(
                        label = "All",
                        isSelected = selectedCategory == null,
                        onClick = { selectedCategory = null }
                    )
                    FilterChipItem(
                        label = "Streaks",
                        isSelected = selectedCategory == AchievementCategory.STREAK,
                        onClick = { selectedCategory = AchievementCategory.STREAK }
                    )
                    FilterChipItem(
                        label = "Accuracy",
                        isSelected = selectedCategory == AchievementCategory.ACCURACY,
                        onClick = { selectedCategory = AchievementCategory.ACCURACY }
                    )
                    FilterChipItem(
                        label = "Domains",
                        isSelected = selectedCategory == AchievementCategory.DOMAIN_MASTERY,
                        onClick = { selectedCategory = AchievementCategory.DOMAIN_MASTERY }
                    )
                }
            }

            // Achievement Cards (Flat rows separated by hairline dividers)
            items(filteredAchievements) { achievement ->
                AchievementCard(achievement = achievement)
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun FilterChipItem(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val chipBg = if (isSelected) MaterialTheme.calm.accentContainer else MaterialTheme.calm.surface
    val chipBorder = if (isSelected) MaterialTheme.calm.accent else MaterialTheme.calm.divider
    val chipText = if (isSelected) MaterialTheme.calm.accentBright else MaterialTheme.calm.steel

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(chipBg)
            .border(1.dp, chipBorder, RoundedCornerShape(6.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 7.dp)
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            color = chipText
        )
    }
}

@Composable
private fun AchievementCard(achievement: Achievement) {
    val isUnlocked = achievement.isUnlocked

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.calm.surface)
            .border(1.dp, MaterialTheme.calm.divider, RoundedCornerShape(8.dp))
            .padding(14.dp)
            .testTag("ach_card_${achievement.id}")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon Halo
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (isUnlocked) MaterialTheme.calm.accentContainer else MaterialTheme.calm.surface)
                    .border(
                        1.dp,
                        if (isUnlocked) MaterialTheme.calm.accent else MaterialTheme.calm.divider,
                        RoundedCornerShape(6.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isUnlocked) {
                    Text(text = achievement.iconEmoji, fontSize = 22.sp)
                } else {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Locked",
                        tint = MaterialTheme.calm.steel,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = achievement.title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isUnlocked) MaterialTheme.calm.ink else MaterialTheme.calm.steel
                    )

                    if (isUnlocked) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(MaterialTheme.calm.goContainer)
                                .border(1.dp, MaterialTheme.calm.go.copy(alpha = 0.3f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = MaterialTheme.calm.go,
                                    modifier = Modifier.size(10.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "Unlocked",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.calm.go
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = achievement.description,
                    fontSize = 13.sp,
                    lineHeight = 17.sp,
                    color = MaterialTheme.calm.steel
                )

                if (!isUnlocked) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        LinearProgressIndicator(
                            progress = { achievement.progressFraction },
                            modifier = Modifier
                                .weight(1f)
                                .height(3.dp)
                                .clip(RoundedCornerShape(2.dp)),
                            color = MaterialTheme.calm.accentBright,
                            trackColor = MaterialTheme.calm.divider
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${achievement.currentProgress}/${achievement.targetProgress}",
                            style = TabularNumStyle.copy(
                                fontSize = 11.sp,
                                color = MaterialTheme.calm.steel
                            )
                        )
                    }
                }
            }
        }
    }
}
