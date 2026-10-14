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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.LeaderboardUser
import com.example.ui.theme.AccentAmber
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.PrimaryBlue

@Composable
fun LeaderboardScreen(
    leaderboard: List<LeaderboardUser>,
    currentUserHandle: String,
    onUpdateHandle: (String) -> Unit,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    var showEditDialog by remember { mutableStateOf(false) }
    var editHandleText by remember { mutableStateOf(currentUserHandle) }
    var selectedFilter by remember { mutableStateOf("All-Time") }

    val currentUser = leaderboard.find { it.isCurrentUser }

    val sortedList = remember(leaderboard, selectedFilter) {
        when (selectedFilter) {
            "Top Streaks" -> leaderboard.sortedByDescending { it.streakDays }
            else -> leaderboard.sortedBy { it.rank }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack, modifier = Modifier.testTag("leaderboard_back_btn")) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Community Leaderboard",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Anonymous peer ranking for NCEES FS exam prep",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
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
            // User Profile Banner Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .border(1.dp, PrimaryBlue, RoundedCornerShape(18.dp))
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = currentUserHandle,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                IconButton(
                                    onClick = {
                                        editHandleText = currentUserHandle
                                        showEditDialog = true
                                    },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Change handle",
                                        tint = PrimaryBlue,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Your Anonymous Callsign",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                            )
                        }

                        // User Rank Callout
                        if (currentUser != null) {
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "Rank #${currentUser.rank}",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AccentAmber
                                )
                                Text(
                                    text = "${currentUser.scorePoints} pts",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = AccentCyan
                                )
                            }
                        }
                    }
                }
            }

            // Filter Tabs
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("All-Time", "Top Streaks", "This Week").forEach { filter ->
                        val isSelected = selectedFilter == filter
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (isSelected) PrimaryBlue else MaterialTheme.colorScheme.surfaceVariant)
                                .clickable { selectedFilter = filter }
                                .padding(horizontal = 14.dp, vertical = 7.dp)
                        ) {
                            Text(
                                text = filter,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Leaderboard User Rows
            items(sortedList) { user ->
                LeaderboardRow(user = user)
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    // Change Handle Dialog
    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Edit Anonymous Callsign") },
            text = {
                Column {
                    Text(
                        text = "Choose a privacy-friendly username to display on the peer leaderboard.",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = editHandleText,
                        onValueChange = { editHandleText = it.take(20) },
                        label = { Text("Callsign") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onUpdateHandle(editHandleText)
                        showEditDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                ) {
                    Text("Save", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Cancel")
                }
            },
            containerColor = MaterialTheme.colorScheme.surface
        )
    }
}

@Composable
private fun LeaderboardRow(user: LeaderboardUser) {
    val isMe = user.isCurrentUser

    val rankColor = when (user.rank) {
        1 -> Color(0xFFFFD700) // Gold
        2 -> Color(0xFFC0C0C0) // Silver
        3 -> Color(0xFFCD7F32) // Bronze
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(if (isMe) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f) else MaterialTheme.colorScheme.surface)
            .border(
                1.dp,
                if (isMe) PrimaryBlue else MaterialTheme.colorScheme.outline,
                RoundedCornerShape(14.dp)
            )
            .padding(14.dp)
            .testTag("leaderboard_row_${user.rank}")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Rank Number / Trophy
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                if (user.rank <= 3) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = "Trophy",
                        tint = rankColor,
                        modifier = Modifier.size(18.dp)
                    )
                } else {
                    Text(
                        text = "#${user.rank}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = rankColor
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = user.handle,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isMe) PrimaryBlue else MaterialTheme.colorScheme.onSurface
                    )
                    if (isMe) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "YOU",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryBlue
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${user.questionsAnswered} questions • ${user.accuracy}% acc",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (user.badgeLabel != null) {
                        Text(text = " • ", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                        Text(
                            text = user.badgeLabel,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = AccentCyan
                        )
                    }
                }
            }

            // Streak & Points
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${user.scorePoints} pts",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocalFireDepartment,
                        contentDescription = null,
                        tint = AccentAmber,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "${user.streakDays}d",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AccentAmber
                    )
                }
            }
        }
    }
}
