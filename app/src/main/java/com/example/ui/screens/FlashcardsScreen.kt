package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SwipeVertical
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.FlashcardItem
import com.example.data.StudyGuideData
import com.example.model.FsDomain
import com.example.ui.theme.AccentAmber
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.CorrectBadgeBg
import com.example.ui.theme.CorrectBadgeText
import com.example.ui.theme.IncorrectBadgeBg
import com.example.ui.theme.IncorrectBadgeText
import com.example.ui.theme.PrimaryBlue
import kotlin.math.roundToInt

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FlashcardsScreen(
    initialCards: List<FlashcardItem> = StudyGuideData.flashcards,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    var selectedDomain by remember { mutableStateOf<FsDomain?>(null) }
    var deck = remember(selectedDomain) {
        val filtered = if (selectedDomain != null) {
            initialCards.filter { it.domain == selectedDomain }
        } else {
            initialCards
        }
        mutableStateListOf<FlashcardItem>().apply { addAll(filtered) }
    }

    var currentIndex by remember(deck) { mutableIntStateOf(0) }
    var isRevealed by remember { mutableStateOf(false) }

    val masteredCardIds = remember { mutableStateListOf<String>() }
    val needsPracticeCardIds = remember { mutableStateListOf<String>() }

    var dragOffsetY by remember { mutableFloatStateOf(0f) }
    var dragOffsetX by remember { mutableFloatStateOf(0f) }

    val animatedOffsetY by animateFloatAsState(
        targetValue = dragOffsetY,
        animationSpec = spring(),
        label = "drag_offset_y"
    )

    val animatedOffsetX by animateFloatAsState(
        targetValue = dragOffsetX,
        animationSpec = spring(),
        label = "drag_offset_x"
    )

    val rotation by animateFloatAsState(
        targetValue = if (isRevealed) 180f else 0f,
        animationSpec = tween(durationMillis = 350),
        label = "card_flip_rotation"
    )

    val currentCard = if (deck.isNotEmpty()) deck.getOrElse(currentIndex) { deck.first() } else null

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("flashcards_back_button")) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = "Flashcard Study Mode",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = if (deck.isNotEmpty()) "Card ${currentIndex + 1} of ${deck.size} • Mastered: ${masteredCardIds.size}" else "0 cards",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Shuffle button
                IconButton(
                    onClick = {
                        val shuffled = deck.shuffled()
                        deck.clear()
                        deck.addAll(shuffled)
                        currentIndex = 0
                        isRevealed = false
                    },
                    modifier = Modifier.testTag("flashcards_shuffle_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Shuffle,
                        contentDescription = "Shuffle",
                        tint = AccentCyan
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Domain Quick Filter Row
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                val allSelected = (selectedDomain == null)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (allSelected) PrimaryBlue else MaterialTheme.colorScheme.surfaceVariant)
                        .clickable {
                            selectedDomain = null
                            isRevealed = false
                        }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "All (${initialCards.size})",
                        fontSize = 11.sp,
                        fontWeight = if (allSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (allSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                listOf(
                    FsDomain.CURVES,
                    FsDomain.PLSS,
                    FsDomain.BOUNDARY_LAW_PRINCIPLES,
                    FsDomain.GEODESY_AND_PROJECTIONS,
                    FsDomain.SURVEY_METHODS_EQUIPMENT,
                    FsDomain.APPLIED_MATH_COGO
                ).forEach { dom ->
                    val isDomSelected = (selectedDomain == dom)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isDomSelected) PrimaryBlue else MaterialTheme.colorScheme.surfaceVariant)
                            .clickable {
                                selectedDomain = dom
                                isRevealed = false
                            }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = dom.shortCode,
                            fontSize = 11.sp,
                            fontWeight = if (isDomSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isDomSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Progress Bar
            LinearProgressIndicator(
                progress = {
                    if (deck.isNotEmpty()) ((currentIndex + 1).toFloat() / deck.size).coerceIn(0f, 1f)
                    else 0f
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = PrimaryBlue,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (currentCard != null) {
                // Interactive Swipe & Flip Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .offset { IntOffset(animatedOffsetX.roundToInt(), animatedOffsetY.roundToInt()) }
                        .graphicsLayer {
                            rotationY = rotation
                            cameraDistance = 14f * density
                        }
                        .clip(RoundedCornerShape(22.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .border(
                            1.5.dp,
                            if (isRevealed) AccentCyan else MaterialTheme.colorScheme.outline,
                            RoundedCornerShape(22.dp)
                        )
                        .pointerInput(currentCard.id, isRevealed) {
                            detectDragGestures(
                                onDrag = { change, dragAmount ->
                                    change.consume()
                                    dragOffsetY = (dragOffsetY + dragAmount.y).coerceIn(-120f, 120f)
                                    dragOffsetX = (dragOffsetX + dragAmount.x).coerceIn(-120f, 120f)
                                },
                                onDragEnd = {
                                    // If swiped up or horizontally past threshold -> trigger reveal!
                                    if (dragOffsetY < -40f || dragOffsetY > 40f || dragOffsetX < -50f || dragOffsetX > 50f) {
                                        isRevealed = !isRevealed
                                    }
                                    dragOffsetY = 0f
                                    dragOffsetX = 0f
                                },
                                onDragCancel = {
                                    dragOffsetY = 0f
                                    dragOffsetX = 0f
                                }
                            )
                        }
                        .clickable { isRevealed = !isRevealed }
                        .padding(22.dp)
                        .testTag("flashcard_body"),
                    contentAlignment = Alignment.Center
                ) {
                    if (rotation <= 90f) {
                        // === FRONT OF CARD: CONCEPT / QUESTION ===
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState()),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                // Domain badge
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(MaterialTheme.colorScheme.secondaryContainer)
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = currentCard.domain.title,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = currentCard.frontTitle,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = currentCard.frontQuestion,
                                    fontSize = 19.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 28.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            // Swipe/Tap Reveal Indicator
                            Box(
                                modifier = Modifier
                                    .padding(top = 20.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .border(1.dp, PrimaryBlue, RoundedCornerShape(16.dp))
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.SwipeVertical,
                                        contentDescription = null,
                                        tint = PrimaryBlue,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Swipe to reveal answer ↺",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = PrimaryBlue
                                    )
                                }
                            }
                        }
                    } else {
                        // === BACK OF CARD: DETAILED EXPLANATION & FORMULAS ===
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .graphicsLayer { rotationY = 180f }
                                .verticalScroll(rememberScrollState()),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(CorrectBadgeBg)
                                        .padding(horizontal = 12.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "Answer & Solution",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = CorrectBadgeText
                                    )
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                Text(
                                    text = currentCard.backAnswer,
                                    fontSize = 15.sp,
                                    lineHeight = 22.sp,
                                    textAlign = TextAlign.Start,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                if (currentCard.formula != null) {
                                    Spacer(modifier = Modifier.height(14.dp))
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(MaterialTheme.colorScheme.secondaryContainer)
                                            .padding(horizontal = 12.dp, vertical = 8.dp)
                                    ) {
                                        Text(
                                            text = currentCard.formula,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSecondaryContainer
                                        )
                                    }
                                }

                                if (currentCard.memoryTrick != null) {
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(AccentAmber.copy(alpha = 0.15f))
                                            .padding(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Lightbulb,
                                            contentDescription = null,
                                            tint = AccentAmber,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = currentCard.memoryTrick,
                                            fontSize = 12.sp,
                                            color = AccentAmber,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            }

                            // Rating actions on back
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Button(
                                    onClick = {
                                        needsPracticeCardIds.add(currentCard.id)
                                        if (currentIndex < deck.size - 1) {
                                            isRevealed = false
                                            currentIndex++
                                        } else {
                                            onBack()
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = IncorrectBadgeBg),
                                    shape = RoundedCornerShape(16.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("❌ Review again", color = IncorrectBadgeText, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                }

                                Button(
                                    onClick = {
                                        masteredCardIds.add(currentCard.id)
                                        if (currentIndex < deck.size - 1) {
                                            isRevealed = false
                                            currentIndex++
                                        } else {
                                            onBack()
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = CorrectBadgeBg),
                                    shape = RoundedCornerShape(16.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("✅ Mastered", color = CorrectBadgeText, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }
                    }
                }
            } else {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Text("No cards in this domain.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Navigation Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = {
                        if (currentIndex > 0) {
                            isRevealed = false
                            currentIndex--
                        }
                    },
                    enabled = currentIndex > 0,
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.size(width = 110.dp, height = 48.dp)
                ) {
                    Text("Previous")
                }

                IconButton(
                    onClick = { isRevealed = !isRevealed },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Flip",
                        tint = PrimaryBlue
                    )
                }

                Button(
                    onClick = {
                        if (currentIndex < deck.size - 1) {
                            isRevealed = false
                            currentIndex++
                        } else {
                            onBack()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.size(width = 110.dp, height = 48.dp)
                ) {
                    Text(if (currentIndex < deck.size - 1) "Next" else "Done", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}
