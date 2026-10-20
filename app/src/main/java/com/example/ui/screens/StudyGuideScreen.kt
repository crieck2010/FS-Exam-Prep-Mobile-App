package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.example.data.StudyGuideData
import com.example.data.StudyTopic
import com.example.ui.theme.SerifDisplayHeadline
import com.example.ui.theme.SerifSectionHeading
import com.example.ui.theme.calm

@Composable
fun StudyGuideScreen(
    topics: List<StudyTopic> = StudyGuideData.topics,
    onBack: () -> Unit
) {
    BackHandler { onBack() }
    var searchQuery by remember { mutableStateOf("") }

    val filteredTopics = remember(searchQuery) {
        if (searchQuery.isBlank()) topics
        else topics.filter {
            it.title.contains(searchQuery, ignoreCase = true) ||
                    it.summary.contains(searchQuery, ignoreCase = true) ||
                    it.domain.title.contains(searchQuery, ignoreCase = true)
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
                IconButton(onClick = onBack, modifier = Modifier.testTag("study_guide_back_button")) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.calm.steel
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    // Serif (Noto Serif) used for Study Guide section heading per Brand Guide §2.2
                    Text(
                        text = "Learn: FS Study Guide",
                        style = SerifSectionHeading.copy(color = MaterialTheme.calm.ink)
                    )
                    Text(
                        text = "Formulas, key concepts & exam tips",
                        fontSize = 13.sp,
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
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search formulas (e.g. arc, PLSS, curvature)...", fontSize = 14.sp) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = MaterialTheme.calm.steel
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.calm.accent,
                        unfocusedBorderColor = MaterialTheme.calm.divider,
                        focusedContainerColor = MaterialTheme.calm.surface,
                        unfocusedContainerColor = MaterialTheme.calm.surface
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("study_search_field")
                )
            }

            if (filteredTopics.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            // Display headline in empty state uses Noto Serif per Brand Guide §2.2
                            Text(
                                text = "No matching topics",
                                style = SerifDisplayHeadline.copy(color = MaterialTheme.calm.ink)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Try adjusting your search terms.",
                                fontSize = 14.sp,
                                color = MaterialTheme.calm.steel
                            )
                        }
                    }
                }
            } else {
                items(filteredTopics) { topic ->
                    StudyTopicCard(topic = topic)
                }
            }

            item {
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}

@Composable
private fun StudyTopicCard(topic: StudyTopic) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.calm.surface)
            .border(1.dp, MaterialTheme.calm.divider, RoundedCornerShape(8.dp))
            .clickable { expanded = !expanded }
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(MaterialTheme.calm.surface)
                            .border(1.dp, MaterialTheme.calm.divider, RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = topic.domain.title,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.calm.steel
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = topic.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.calm.ink
                    )
                }
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = MaterialTheme.calm.steel,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = topic.summary,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = MaterialTheme.calm.steel
            )

            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 14.dp)) {
                    Text(
                        text = "Key Formulas:",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.calm.ink
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    topic.keyFormulas.forEach { formula ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(MaterialTheme.calm.accentContainer)
                                .border(1.dp, MaterialTheme.calm.accent.copy(alpha = 0.2f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = formula,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.calm.accentBright
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Exam Tips & Gotchas:",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.calm.ink
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    topic.examTips.forEach { tip ->
                        Row(modifier = Modifier.padding(vertical = 2.dp)) {
                            Text("• ", color = MaterialTheme.calm.steel, fontSize = 13.sp)
                            Text(
                                text = tip,
                                fontSize = 13.sp,
                                lineHeight = 18.sp,
                                color = MaterialTheme.calm.steel
                            )
                        }
                    }
                }
            }
        }
    }
}
