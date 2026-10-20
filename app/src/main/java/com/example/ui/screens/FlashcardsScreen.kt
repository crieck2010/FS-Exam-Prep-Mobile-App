package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SwipeVertical
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.FlashcardItem
import com.example.data.StudyGuideData
import com.example.model.FsDomain
import com.example.ui.theme.SerifSectionHeading
import com.example.ui.theme.TabularNumStyle
import com.example.ui.theme.calm
import kotlin.math.roundToInt

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FlashcardsScreen(
    initialCards: List<FlashcardItem> = StudyGuideData.flashcards,
    initialDomain: FsDomain? = null,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    var selectedDomain by remember { mutableStateOf<FsDomain?>(initialDomain) }
    val deck = remember(selectedDomain) {
        val filtered = if (selectedDomain != null) {
            initialCards.filter { it.domain == selectedDomain }
        } else {
            initialCards
        }
        mutableStateListOf<FlashcardItem>().apply { addAll(filtered) }
    }

    var currentIndex by remember(selectedDomain) { mutableIntStateOf(0) }
    var isRevealed by remember { mutableStateOf(false) }

    val masteredCardIds = remember { mutableStateListOf<String>() }
    val needsPracticeCardIds = remember { mutableStateListOf<String>() }

    val currentCard = deck.getOrNull(currentIndex)

    val rotation by animateFloatAsState(
        targetValue = if (isRevealed) 180f else 0f,
        animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing),
        label = "flashcard_rotation"
    )

    var dragOffsetY by remember { mutableFloatStateOf(0f) }
    var dragOffsetX by remember { mutableFloatStateOf(0f) }

    val animatedOffsetY by animateFloatAsState(
        targetValue = dragOffsetY,
        animationSpec = tween(durationMillis = 150),
        label = "card_drag_y"
    )
    val animatedOffsetX by animateFloatAsState(
        targetValue = dragOffsetX,
        animationSpec = tween(durationMillis = 150),
        label = "card_drag_x"
    )

    val density = LocalDensity.current.density

    Scaffold(
        containerColor = MaterialTheme.calm.bg,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.calm.surface)
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("flashcards_back_btn")) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.calm.steel
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = "Flashcard Study Mode",
                            style = SerifSectionHeading.copy(color = MaterialTheme.calm.ink)
                        )
                        Text(
                            text = if (deck.isNotEmpty()) "Card ${currentIndex + 1} of ${deck.size} • Mastered: ${masteredCardIds.size}" else "0 cards",
                            style = TabularNumStyle.copy(
                                fontSize = 12.sp,
                                color = MaterialTheme.calm.steel
                            )
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
                        tint = MaterialTheme.calm.steel
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
                val allBg = if (allSelected) MaterialTheme.calm.accentContainer else MaterialTheme.calm.surface
                val allBorder = if (allSelected) MaterialTheme.calm.accent else MaterialTheme.calm.divider
                val allText = if (allSelected) MaterialTheme.calm.accentBright else MaterialTheme.calm.steel

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(allBg)
                        .border(1.dp, allBorder, RoundedCornerShape(6.dp))
                        .clickable {
                            selectedDomain = null
                            isRevealed = false
                        }
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = "All (${initialCards.size})",
                        style = TabularNumStyle.copy(
                            fontSize = 11.sp,
                            fontWeight = if (allSelected) FontWeight.SemiBold else FontWeight.Normal,
                            color = allText
                        )
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
                    val domBg = if (isDomSelected) MaterialTheme.calm.accentContainer else MaterialTheme.calm.surface
                    val domBorder = if (isDomSelected) MaterialTheme.calm.accent else MaterialTheme.calm.divider
                    val domText = if (isDomSelected) MaterialTheme.calm.accentBright else MaterialTheme.calm.steel

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(domBg)
                            .border(1.dp, domBorder, RoundedCornerShape(6.dp))
                            .clickable {
                                selectedDomain = dom
                                isRevealed = false
                            }
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = dom.shortCode,
                            fontSize = 11.sp,
                            fontWeight = if (isDomSelected) FontWeight.SemiBold else FontWeight.Normal,
                            color = domText
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Progress Bar (thin 3dp stroke)
            LinearProgressIndicator(
                progress = {
                    if (deck.isNotEmpty()) ((currentIndex + 1).toFloat() / deck.size).coerceIn(0f, 1f)
                    else 0f
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = MaterialTheme.calm.accentBright,
                trackColor = MaterialTheme.calm.divider
            )

            Spacer(modifier = Modifier.height(14.dp))

            if (currentCard != null) {
                // Interactive Swipe & Flip Card (Flat, 8dp radius, hairline border)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .offset { IntOffset(animatedOffsetX.roundToInt(), animatedOffsetY.roundToInt()) }
                        .graphicsLayer {
                            rotationY = rotation
                            cameraDistance = 14f * density
                        }
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.calm.surface)
                        .border(
                            1.dp,
                            if (isRevealed) MaterialTheme.calm.accent else MaterialTheme.calm.divider,
                            RoundedCornerShape(8.dp)
                        )
                        .pointerInput(currentCard.id, isRevealed) {
                            detectDragGestures(
                                onDrag = { change, dragAmount ->
                                    change.consume()
                                    dragOffsetY = (dragOffsetY + dragAmount.y).coerceIn(-120f, 120f)
                                    dragOffsetX = (dragOffsetX + dragAmount.x).coerceIn(-120f, 120f)
                                },
                                onDragEnd = {
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
                        .padding(20.dp)
                        .testTag("flashcard_body"),
                    contentAlignment = Alignment.Center
                ) {
                    if (rotation <= 90f) {
                        // FRONT OF CARD: CONCEPT / QUESTION
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
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(MaterialTheme.calm.surface)
                                        .border(1.dp, MaterialTheme.calm.divider, RoundedCornerShape(6.dp))
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = currentCard.domain.title,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.calm.steel
                                    )
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                Text(
                                    text = currentCard.frontTitle,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.calm.steel
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = currentCard.frontQuestion,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 26.sp,
                                    color = MaterialTheme.calm.ink
                                )
                            }

                            // Swipe/Tap Reveal Indicator
                            Box(
                                modifier = Modifier
                                    .padding(top = 18.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(MaterialTheme.calm.surface)
                                    .border(1.dp, MaterialTheme.calm.divider, RoundedCornerShape(6.dp))
                                    .padding(horizontal = 14.dp, vertical = 7.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.SwipeVertical,
                                        contentDescription = null,
                                        tint = MaterialTheme.calm.steel,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Swipe to reveal answer ↺",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.calm.steel
                                    )
                                }
                            }
                        }
                    } else {
                        // BACK OF CARD: DETAILED EXPLANATION & FORMULAS
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
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(MaterialTheme.calm.goContainer)
                                        .border(1.dp, MaterialTheme.calm.go.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "Answer & Solution",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.calm.go
                                    )
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                Text(
                                    text = currentCard.backAnswer,
                                    fontSize = 15.sp,
                                    lineHeight = 22.sp,
                                    textAlign = TextAlign.Start,
                                    color = MaterialTheme.calm.ink
                                )

                                if (currentCard.formula != null) {
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(MaterialTheme.calm.accentContainer)
                                            .border(1.dp, MaterialTheme.calm.accent.copy(alpha = 0.2f), RoundedCornerShape(6.dp))
                                            .padding(horizontal = 12.dp, vertical = 8.dp)
                                    ) {
                                        Text(
                                            text = currentCard.formula,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.calm.accentBright
                                        )
                                    }
                                }

                                if (currentCard.memoryTrick != null) {
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(MaterialTheme.calm.surface)
                                            .border(1.dp, MaterialTheme.calm.divider, RoundedCornerShape(6.dp))
                                            .padding(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Lightbulb,
                                            contentDescription = null,
                                            tint = MaterialTheme.calm.steel,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = currentCard.memoryTrick,
                                            fontSize = 12.sp,
                                            color = MaterialTheme.calm.steel,
                                            fontWeight = FontWeight.Normal
                                        )
                                    }
                                }
                            }

                            // Rating actions on back (Muted Go & Warn)
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
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.calm.warnContainer,
                                        contentColor = MaterialTheme.calm.warn
                                    ),
                                    elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    border = BorderStroke(1.dp, MaterialTheme.calm.warn.copy(alpha = 0.3f)),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("✕ Review again", color = MaterialTheme.calm.warn, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
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
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.calm.goContainer,
                                        contentColor = MaterialTheme.calm.go
                                    ),
                                    elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    border = BorderStroke(1.dp, MaterialTheme.calm.go.copy(alpha = 0.3f)),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("✓ Mastered", color = MaterialTheme.calm.go, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }
                    }
                }
            } else {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Text("No cards in this domain.", color = MaterialTheme.calm.steel)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

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
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, MaterialTheme.calm.divider),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.calm.ink,
                        disabledContentColor = MaterialTheme.calm.steel.copy(alpha = 0.4f)
                    ),
                    modifier = Modifier.size(width = 110.dp, height = 46.dp)
                ) {
                    Text("Previous")
                }

                IconButton(
                    onClick = { isRevealed = !isRevealed },
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.calm.surface)
                        .border(1.dp, MaterialTheme.calm.divider, RoundedCornerShape(8.dp))
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Flip",
                        tint = MaterialTheme.calm.steel
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
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.calm.accent,
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.size(width = 110.dp, height = 46.dp)
                ) {
                    Text(if (currentIndex < deck.size - 1) "Next" else "Done", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}
