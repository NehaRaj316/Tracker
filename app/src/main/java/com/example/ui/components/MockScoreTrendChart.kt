package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MockTest
import com.example.ui.theme.*

enum class TrendViewMode(val title: String, val maxScore: Float) {
    TOTAL("Total Score (/200)", 200f),
    QUANT("Quant (/50)", 50f),
    REASONING("Reasoning (/50)", 50f),
    ENGLISH("English (/50)", 50f),
    GA("General Awareness (/50)", 50f)
}

@Composable
fun MockScoreTrendChartCard(
    mockTests: List<MockTest>,
    modifier: Modifier = Modifier,
    onLogScoreClick: () -> Unit = {},
    onSelectMockForEdit: (MockTest) -> Unit = {}
) {
    val attemptedMocks = remember(mockTests) {
        mockTests.filter { it.isAttempted }.sortedBy { it.testNumber }
    }

    var selectedViewMode by remember { mutableStateOf(TrendViewMode.TOTAL) }
    var selectedMockIndex by remember(attemptedMocks) {
        mutableStateOf(if (attemptedMocks.isNotEmpty()) attemptedMocks.lastIndex else -1)
    }

    // Keep selected index in bounds if attempted list changes
    LaunchedEffect(attemptedMocks.size) {
        if (attemptedMocks.isNotEmpty() && (selectedMockIndex < 0 || selectedMockIndex >= attemptedMocks.size)) {
            selectedMockIndex = attemptedMocks.lastIndex
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .testTag("score_trend_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = PureWhiteSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, LavenderOutline)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // Header: Title & Log Score Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = LavenderContainer,
                        modifier = Modifier.size(38.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.TrendingUp,
                                contentDescription = null,
                                tint = LavenderPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Score Progression Trend",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Room DB Tracking • 30 Full Mocks",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Button(
                    onClick = onLogScoreClick,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LavenderPrimary,
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("btn_dashboard_log_score")
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Log Score", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (attemptedMocks.isEmpty()) {
                // Empty state when no mock scores logged yet
                ScoreTrendEmptyState(onLogScoreClick = onLogScoreClick)
            } else {
                // Stat Metric Badges Row
                val latestMock = attemptedMocks.last()
                val firstMock = attemptedMocks.first()
                val scoreProgression = latestMock.totalScore - firstMock.totalScore
                val avgScore = attemptedMocks.map { it.totalScore }.average().toFloat()
                val maxScore = attemptedMocks.maxOf { it.totalScore }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Latest Score
                    ScoreMiniMetricBox(
                        title = "LATEST SCORE",
                        value = "${String.format("%.1f", latestMock.totalScore)}",
                        subtitle = if (attemptedMocks.size > 1) {
                            val prevMock = attemptedMocks[attemptedMocks.lastIndex - 1]
                            val diff = latestMock.totalScore - prevMock.totalScore
                            val sign = if (diff >= 0) "+" else ""
                            "$sign${String.format("%.1f", diff)} vs prev"
                        } else "Mock #${latestMock.testNumber}",
                        containerColor = LavenderContainer,
                        contentColor = OnLavenderContainer,
                        modifier = Modifier.weight(1f)
                    )

                    // Average Score
                    ScoreMiniMetricBox(
                        title = "AVG SCORE",
                        value = "${String.format("%.1f", avgScore)}",
                        subtitle = "${attemptedMocks.size} tests done",
                        containerColor = LightLavenderBackground,
                        contentColor = LavenderPrimary,
                        modifier = Modifier.weight(1f)
                    )

                    // Trajectory / Growth
                    val isGrowth = scoreProgression >= 0
                    ScoreMiniMetricBox(
                        title = "TRAJECTORY",
                        value = if (scoreProgression >= 0) "+${String.format("%.1f", scoreProgression)}" else String.format("%.1f", scoreProgression),
                        subtitle = if (isGrowth) "Total Gain ↑" else "Total Shift ↓",
                        containerColor = if (isGrowth) EmeraldSuccess.copy(alpha = 0.14f) else RoseWarning.copy(alpha = 0.14f),
                        contentColor = if (isGrowth) EmeraldSuccess else RoseWarning,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Subject / Total View Mode Filter Chips
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    TrendViewMode.entries.forEach { mode ->
                        val isSelected = selectedViewMode == mode
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedViewMode = mode },
                            label = { Text(mode.title, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = LavenderPrimary,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // The Score Trend Line Canvas
                ScoreProgressionCanvas(
                    attemptedMocks = attemptedMocks,
                    viewMode = selectedViewMode,
                    selectedIndex = selectedMockIndex,
                    onSelectIndex = { selectedMockIndex = it }
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Selected Mock Detail Inspector Card
                if (selectedMockIndex in attemptedMocks.indices) {
                    val inspectedMock = attemptedMocks[selectedMockIndex]
                    SelectedMockInspectorCard(
                        mock = inspectedMock,
                        onEditClick = { onSelectMockForEdit(inspectedMock) }
                    )
                }
            }
        }
    }
}

@Composable
fun ScoreProgressionCanvas(
    attemptedMocks: List<MockTest>,
    viewMode: TrendViewMode,
    selectedIndex: Int,
    onSelectIndex: (Int) -> Unit
) {
    val maxScoreCap = viewMode.maxScore
    val cutoffThreshold = if (viewMode == TrendViewMode.TOTAL) 140f else 35f

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(LightLavenderBackground.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
            .border(1.dp, LavenderOutline.copy(alpha = 0.6f), RoundedCornerShape(14.dp))
            .padding(horizontal = 14.dp, vertical = 12.dp)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .testTag("score_trend_canvas")
                .pointerInput(attemptedMocks) {
                    detectTapGestures { offset ->
                        if (attemptedMocks.isEmpty()) return@detectTapGestures
                        val paddingStart = 30f
                        val paddingEnd = 20f
                        val chartWidth = size.width - paddingStart - paddingEnd

                        val step = if (attemptedMocks.size > 1) chartWidth / (attemptedMocks.size - 1) else chartWidth
                        val clickedIndex = if (attemptedMocks.size == 1) {
                            0
                        } else {
                            val relX = (offset.x - paddingStart).coerceIn(0f, chartWidth)
                            val idx = (relX / step).toInt().coerceIn(0, attemptedMocks.lastIndex)
                            // check if closer to idx or idx + 1
                            if (idx < attemptedMocks.lastIndex && (relX - idx * step) > (step / 2)) {
                                idx + 1
                            } else {
                                idx
                            }
                        }
                        onSelectIndex(clickedIndex)
                    }
                }
        ) {
            val chartWidth = size.width
            val chartHeight = size.height
            val paddingStart = 34f
            val paddingEnd = 24f
            val paddingTop = 20f
            val paddingBottom = 26f

            val plotWidth = chartWidth - paddingStart - paddingEnd
            val plotHeight = chartHeight - paddingTop - paddingBottom

            // Helper to get score based on selected view mode
            fun getScore(mock: MockTest): Float = when (viewMode) {
                TrendViewMode.TOTAL -> mock.totalScore
                TrendViewMode.QUANT -> mock.quantScore
                TrendViewMode.REASONING -> mock.reasoningScore
                TrendViewMode.ENGLISH -> mock.englishScore
                TrendViewMode.GA -> mock.gaScore
            }

            // Draw horizontal benchmark gridlines (e.g. 0, 50%, cutoff, 100%)
            val gridLevels = if (viewMode == TrendViewMode.TOTAL) {
                listOf(50f, 100f, 140f, 180f, 200f)
            } else {
                listOf(15f, 30f, 35f, 45f, 50f)
            }

            for (level in gridLevels) {
                val yNorm = 1f - (level / maxScoreCap)
                val y = paddingTop + yNorm * plotHeight
                val isCutoff = level == cutoffThreshold

                val strokeStyle = if (isCutoff) {
                    Stroke(width = 1.5f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 6f), 0f))
                } else {
                    Stroke(width = 1f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 8f), 0f))
                }

                val lineColor = if (isCutoff) {
                    AmberAccent.copy(alpha = 0.85f)
                } else {
                    Color(0xFF8B829E).copy(alpha = 0.25f)
                }

                drawLine(
                    color = lineColor,
                    start = Offset(paddingStart, y),
                    end = Offset(chartWidth - paddingEnd, y),
                    strokeWidth = strokeStyle.width,
                    pathEffect = strokeStyle.pathEffect
                )
            }

            if (attemptedMocks.isEmpty()) return@Canvas

            // Compute Coordinates
            val points = attemptedMocks.mapIndexed { index, mock ->
                val x = if (attemptedMocks.size == 1) {
                    paddingStart + plotWidth / 2f
                } else {
                    paddingStart + (index.toFloat() / (attemptedMocks.size - 1).toFloat()) * plotWidth
                }
                val score = getScore(mock).coerceIn(0f, maxScoreCap)
                val y = paddingTop + (1f - (score / maxScoreCap)) * plotHeight
                Offset(x, y)
            }

            // 1. Draw Gradient Area Fill under the trend curve
            if (points.size >= 2) {
                val fillPath = Path().apply {
                    moveTo(points.first().x, paddingTop + plotHeight)
                    lineTo(points.first().x, points.first().y)

                    for (i in 0 until points.size - 1) {
                        val p0 = points[i]
                        val p1 = points[i + 1]
                        val midX = (p0.x + p1.x) / 2f
                        cubicTo(midX, p0.y, midX, p1.y, p1.x, p1.y)
                    }

                    lineTo(points.last().x, paddingTop + plotHeight)
                    close()
                }

                drawPath(
                    path = fillPath,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            LavenderPrimary.copy(alpha = 0.28f),
                            LavenderSecondary.copy(alpha = 0.10f),
                            Color.Transparent
                        ),
                        startY = paddingTop,
                        endY = paddingTop + plotHeight
                    )
                )
            }

            // 2. Draw Trend Curve Line
            if (points.size >= 2) {
                val curvePath = Path().apply {
                    moveTo(points.first().x, points.first().y)
                    for (i in 0 until points.size - 1) {
                        val p0 = points[i]
                        val p1 = points[i + 1]
                        val midX = (p0.x + p1.x) / 2f
                        cubicTo(midX, p0.y, midX, p1.y, p1.x, p1.y)
                    }
                }

                drawPath(
                    path = curvePath,
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            LavenderPrimary,
                            LavenderSecondary,
                            Color(0xFF6366F1)
                        ),
                        startX = paddingStart,
                        endX = chartWidth - paddingEnd
                    ),
                    style = Stroke(
                        width = 3.5.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                )
            }

            // 3. Draw Selected Item Cursor Guide
            if (selectedIndex in points.indices) {
                val selPoint = points[selectedIndex]
                drawLine(
                    color = LavenderPrimary.copy(alpha = 0.5f),
                    start = Offset(selPoint.x, paddingTop),
                    end = Offset(selPoint.x, paddingTop + plotHeight),
                    strokeWidth = 1.5.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f)
                )
            }

            // 4. Draw Data Points (Dots)
            points.forEachIndexed { index, pt ->
                val isSelected = index == selectedIndex

                // Glowing halo if selected
                if (isSelected) {
                    drawCircle(
                        color = LavenderPrimary.copy(alpha = 0.25f),
                        radius = 12.dp.toPx(),
                        center = pt
                    )
                }

                // White outer ring
                drawCircle(
                    color = Color.White,
                    radius = if (isSelected) 6.5.dp.toPx() else 4.5.dp.toPx(),
                    center = pt
                )

                // Colored core
                drawCircle(
                    color = if (isSelected) LavenderPrimary else LavenderSecondary,
                    radius = if (isSelected) 4.5.dp.toPx() else 3.dp.toPx(),
                    center = pt
                )
            }
        }

        // Overlay Labels: Benchmark Cutoff Legend in top-right
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 4.dp, top = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(AmberAccent, CircleShape)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = if (viewMode == TrendViewMode.TOTAL) "Cutoff: 140 pts" else "Target: 35+ pts",
                style = MaterialTheme.typography.labelSmall,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = AmberAccent
            )
        }
    }
}

@Composable
fun SelectedMockInspectorCard(
    mock: MockTest,
    onEditClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = LavenderContainer.copy(alpha = 0.45f),
        border = androidx.compose.foundation.BorderStroke(1.dp, LavenderOutline),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = LavenderPrimary
                    ) {
                        Text(
                            text = "Mock #${mock.testNumber}",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = mock.provider,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${String.format("%.1f", mock.totalScore)} / 200",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = LavenderPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = onEditClick,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit score",
                            tint = LavenderPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Sectional Badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                SectionScorePill("Quant", mock.quantScore, LavenderPrimary, Modifier.weight(1f))
                SectionScorePill("Reasoning", mock.reasoningScore, Color(0xFF0D9488), Modifier.weight(1f))
                SectionScorePill("English", mock.englishScore, Color(0xFF7C3AED), Modifier.weight(1f))
                SectionScorePill("GA", mock.gaScore, Color(0xFFEA580C), Modifier.weight(1f))
            }

            if (mock.accuracyPercent > 0 || mock.timeTakenMinutes > 0) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Accuracy: ${String.format("%.1f", mock.accuracyPercent)}% (${mock.correctAnswers}C / ${mock.wrongAnswers}W / ${mock.attemptedQuestions}Att)",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Time: ${mock.timeTakenMinutes} mins",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (mock.weakTopics.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Weak Topics: ${mock.weakTopics}",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = RoseWarning
                )
            }
        }
    }
}

@Composable
fun SectionScorePill(
    label: String,
    score: Float,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = color.copy(alpha = 0.12f),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(vertical = 4.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label.take(5),
                style = MaterialTheme.typography.labelSmall,
                fontSize = 9.sp,
                color = color
            )
            Text(
                text = String.format("%.1f", score),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}

@Composable
fun ScoreMiniMetricBox(
    title: String,
    value: String,
    subtitle: String,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = containerColor,
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = contentColor.copy(alpha = 0.8f)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = contentColor
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                fontSize = 10.sp,
                color = contentColor.copy(alpha = 0.75f)
            )
        }
    }
}

@Composable
fun ScoreTrendEmptyState(onLogScoreClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = LightLavenderBackground,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.ShowChart,
                contentDescription = null,
                tint = LavenderPrimary,
                modifier = Modifier.size(44.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "No Mock Test Scores Logged Yet",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Log your test marks (Quant, Reasoning, English, GA) to render the score progression trend line and track your journey toward the 140+ cut-off target.",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(14.dp))
            Button(
                onClick = onLogScoreClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = LavenderPrimary,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Log First Mock Score")
            }
        }
    }
}
