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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.example.ui.theme.TabularNumStyle
import com.example.ui.theme.calm

@Composable
fun LeaderboardScreen(
    leaderboard: List<LeaderboardUser>,
    currentUserHandle: String,
    onUpdateHandle: (String) -> Unit,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    var selectedFilter by remember { mutableStateOf("All-Time") }
    var showEditDialog by remember { mutableStateOf(false) }
    var editHandleText by remember { mutableStateOf(currentUserHandle) }

    val myEntry = remember(leaderboard) { leaderboard.find { it.isCurrentUser } }

    val sortedList = remember(leaderboard, selectedFilter) {
        when (selectedFilter) {
            "Top Streaks" -> leaderboard.sortedByDescending { it.streakDays }
            "This Week" -> leaderboard.sortedByDescending { it.scorePoints }
            else -> leaderboard.sortedBy { it.rank }
        }
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
                IconButton(onClick = onBack, modifier = Modifier.testTag("leaderboard_back_button")) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.calm.steel
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Peer Study Benchmarks",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.calm.ink
                    )
                    Text(
                        text = "Anonymous cohort comparison",
                        fontSize = 12.sp,
                        color = MaterialTheme.calm.steel
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Sample standings shown for illustration. Real rankings arrive with optional sign-in.",
                        fontSize = 11.sp,
                        color = MaterialTheme.calm.steel
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
                // User's Current Standing Card (Flat, hairline border)
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
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(MaterialTheme.calm.accentContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = null,
                                        tint = MaterialTheme.calm.accentBright,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = currentUserHandle,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.calm.ink
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        IconButton(
                                            onClick = {
                                                editHandleText = currentUserHandle
                                                showEditDialog = true
                                            },
                                            modifier = Modifier.size(22.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Edit,
                                                contentDescription = "Edit Callsign",
                                                tint = MaterialTheme.calm.steel,
                                                modifier = Modifier.size(14.dp)
                                            )
                                        }
                                    }
                                    Text(
                                        text = "Your Anonymous Profile",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.calm.steel
                                    )
                                }
                            }

                            // Rank Pill
                            if (myEntry != null) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(MaterialTheme.calm.surface)
                                        .border(1.dp, MaterialTheme.calm.divider, RoundedCornerShape(6.dp))
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "Rank #${myEntry.rank}",
                                        style = TabularNumStyle.copy(
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.calm.ink
                                        )
                                    )
                                }
                            }
                        }

                        if (myEntry != null) {
                            Spacer(modifier = Modifier.height(14.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Points: ${myEntry.scorePoints} pts",
                                    style = TabularNumStyle.copy(
                                        fontSize = 13.sp,
                                        color = MaterialTheme.calm.steel
                                    )
                                )
                                Text(
                                    text = "Streak: ${myEntry.streakDays} days",
                                    style = TabularNumStyle.copy(
                                        fontSize = 13.sp,
                                        color = MaterialTheme.calm.steel
                                    )
                                )
                                Text(
                                    text = "Accuracy: ${myEntry.accuracy}%",
                                    style = TabularNumStyle.copy(
                                        fontSize = 13.sp,
                                        color = MaterialTheme.calm.steel
                                    )
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
                        val chipBg = if (isSelected) MaterialTheme.calm.accentContainer else MaterialTheme.calm.surface
                        val chipBorder = if (isSelected) MaterialTheme.calm.accent else MaterialTheme.calm.divider
                        val chipText = if (isSelected) MaterialTheme.calm.accentBright else MaterialTheme.calm.steel

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(chipBg)
                                .border(1.dp, chipBorder, RoundedCornerShape(6.dp))
                                .clickable { selectedFilter = filter }
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

            // Leaderboard User Rows (Plain ranked list, flat rows, no arcade art)
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
            title = {
                Text(
                    text = "Edit Anonymous Callsign",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.calm.ink
                )
            },
            text = {
                Column {
                    Text(
                        text = "Choose a privacy-friendly username to display on the peer leaderboard.",
                        fontSize = 13.sp,
                        color = MaterialTheme.calm.steel
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = editHandleText,
                        onValueChange = { editHandleText = it.take(20) },
                        label = { Text("Callsign") },
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.calm.accent,
                            focusedLabelColor = MaterialTheme.calm.accent,
                            unfocusedBorderColor = MaterialTheme.calm.divider
                        ),
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
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.calm.accent,
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Save", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Cancel", color = MaterialTheme.calm.steel)
                }
            },
            containerColor = MaterialTheme.calm.surface,
            shape = RoundedCornerShape(8.dp)
        )
    }
}

@Composable
private fun LeaderboardRow(user: LeaderboardUser) {
    val isMe = user.isCurrentUser

    val rowBg = if (isMe) MaterialTheme.calm.accentContainer else MaterialTheme.calm.surface
    val rowBorder = if (isMe) MaterialTheme.calm.accent else MaterialTheme.calm.divider

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(rowBg)
            .border(1.dp, rowBorder, RoundedCornerShape(8.dp))
            .padding(14.dp)
            .testTag("leaderboard_row_${user.rank}")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Rank Number (Plain ranked list, tabular numerals)
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(MaterialTheme.calm.surface)
                    .border(1.dp, MaterialTheme.calm.divider, RoundedCornerShape(6.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${user.rank}",
                    style = TabularNumStyle.copy(
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.calm.ink
                    )
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = user.handle,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.calm.ink
                    )
                    if (isMe) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(MaterialTheme.calm.accent)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "YOU",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${user.questionsAnswered} questions • ${user.accuracy}% acc",
                        style = TabularNumStyle.copy(
                            fontSize = 12.sp,
                            color = MaterialTheme.calm.steel
                        )
                    )
                    if (user.badgeLabel != null) {
                        Text(text = " • ", color = MaterialTheme.calm.steel, fontSize = 12.sp)
                        Text(
                            text = user.badgeLabel,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.calm.accentBright
                        )
                    }
                }
            }

            // Streak & Points (Tabular numerals)
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${user.scorePoints} pts",
                    style = TabularNumStyle.copy(
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.calm.ink
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocalFireDepartment,
                        contentDescription = null,
                        tint = MaterialTheme.calm.steel,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "${user.streakDays}d",
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
