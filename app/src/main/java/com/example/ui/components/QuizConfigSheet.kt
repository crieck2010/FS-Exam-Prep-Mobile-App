package com.example.ui.components

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.model.FsDomain
import com.example.model.QuestionDifficulty
import com.example.ui.theme.PrimaryBlue

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun QuizConfigSheet(
    sheetState: SheetState,
    initialDomain: FsDomain? = null,
    onDismiss: () -> Unit,
    onStartQuiz: (domain: FsDomain?, count: Int, focusGrowth: Boolean, difficulty: QuestionDifficulty) -> Unit
) {
    var selectedDomain by remember { mutableStateOf<FsDomain?>(initialDomain) }
    var focusGrowth by remember { mutableStateOf(false) }
    var selectedCount by remember { mutableIntStateOf(10) }
    var isCustomCount by remember { mutableStateOf(false) }
    var customCountText by remember { mutableStateOf("15") }
    var selectedDifficulty by remember { mutableStateOf(QuestionDifficulty.ABOUT_THE_SAME) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp)
                .testTag("quiz_config_sheet")
        ) {
            Text(
                text = "Add more questions",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Focus Section
            Text(
                text = "Focus",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ChipOption(
                    text = "All topics",
                    selected = !focusGrowth && selectedDomain == null,
                    onClick = {
                        focusGrowth = false
                        selectedDomain = null
                    },
                    modifier = Modifier.testTag("chip_all_topics")
                )
                ChipOption(
                    text = "Growth areas",
                    selected = focusGrowth,
                    onClick = {
                        focusGrowth = true
                        selectedDomain = null
                    },
                    modifier = Modifier.testTag("chip_growth_areas")
                )
            }

            if (selectedDomain != null) {
                Spacer(modifier = Modifier.height(8.dp))
                ChipOption(
                    text = selectedDomain!!.title,
                    selected = true,
                    onClick = { },
                    modifier = Modifier.testTag("chip_selected_domain")
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Number of Questions Section
            Text(
                text = "Number of questions",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(5, 10, 20).forEach { count ->
                    ChipOption(
                        text = "$count",
                        selected = !isCustomCount && selectedCount == count,
                        onClick = {
                            isCustomCount = false
                            selectedCount = count
                        },
                        modifier = Modifier.testTag("chip_count_$count")
                    )
                }

                ChipOption(
                    text = "Custom",
                    icon = { Icon(Icons.Default.Edit, contentDescription = "Custom count", modifier = Modifier.size(14.dp)) },
                    selected = isCustomCount,
                    onClick = {
                        isCustomCount = true
                    },
                    modifier = Modifier.testTag("chip_count_custom")
                )
            }

            if (isCustomCount) {
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = customCountText,
                    onValueChange = { customCountText = it.filter { ch -> ch.isDigit() }.take(2) },
                    label = { Text("Enter number of questions (1-50)") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryBlue,
                        focusedLabelColor = PrimaryBlue
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("custom_count_input")
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Difficulty Section
            Text(
                text = "Difficulty",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ChipOption(
                    text = "Easier",
                    selected = selectedDifficulty == QuestionDifficulty.EASIER,
                    onClick = { selectedDifficulty = QuestionDifficulty.EASIER },
                    modifier = Modifier.testTag("chip_diff_easier")
                )
                ChipOption(
                    text = "About the same",
                    selected = selectedDifficulty == QuestionDifficulty.ABOUT_THE_SAME,
                    onClick = { selectedDifficulty = QuestionDifficulty.ABOUT_THE_SAME },
                    modifier = Modifier.testTag("chip_diff_same")
                )
                ChipOption(
                    text = "Harder",
                    selected = selectedDifficulty == QuestionDifficulty.HARDER,
                    onClick = { selectedDifficulty = QuestionDifficulty.HARDER },
                    modifier = Modifier.testTag("chip_diff_harder")
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Bottom Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.testTag("cancel_button")
                ) {
                    Text(
                        text = "Cancel",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 15.sp
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Button(
                    onClick = {
                        val count = if (isCustomCount) {
                            customCountText.toIntOrNull()?.coerceIn(1, 50) ?: 10
                        } else {
                            selectedCount
                        }
                        onStartQuiz(selectedDomain, count, focusGrowth, selectedDifficulty)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.testTag("add_questions_button")
                ) {
                    Text(
                        text = "Add questions",
                        fontWeight = FontWeight.Medium,
                        fontSize = 15.sp,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun ChipOption(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: (@Composable () -> Unit)? = null
) {
    val bgColor = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
    val borderColor = if (selected) PrimaryBlue else MaterialTheme.colorScheme.outline
    val textColor = if (selected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (selected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = textColor,
                    modifier = Modifier
                        .size(14.dp)
                        .padding(end = 4.dp)
                )
            }
            Text(
                text = text,
                color = textColor,
                fontSize = 14.sp,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
            )
            if (icon != null) {
                Spacer(modifier = Modifier.width(4.dp))
                icon()
            }
        }
    }
}
