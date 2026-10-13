package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DayPlan
import com.example.ui.theme.*
import kotlin.math.cos
import kotlin.math.sin

/**
 * Streak analytics data structure for FocusCGL 31-Day Plan.
 */
data class StreakData(
    val currentStreak: Int,
    val longestStreak: Int,
    val totalActiveDays: Int,
    val streakGoal: Int = 31,
    val completedDayNumbers: Set<Int>,
    val consecutiveDays: List<Int>
)

/**
 * Calculates unbroken consecutive days of completed study blocks.
 */
fun calculate31DayStreak(dayPlans: List<DayPlan>): StreakData {
    if (dayPlans.isEmpty()) return StreakData(0, 0, 0, 31, emptySet(), emptyList())

    val sortedPlans = dayPlans.sortedBy { it.dayNumber }
    val completedDayNumbers = sortedPlans
        .filter { it.completedBlocksCount > 0 }
        .map { it.dayNumber }
        .toSet()

    // Find maximum consecutive chain
    var maxStreak = 0
    var currentChain = 0
    for (i in 1..31) {
        if (completedDayNumbers.contains(i)) {
            currentChain++
            if (currentChain > maxStreak) {
                maxStreak = currentChain
            }
        } else {
            currentChain = 0
        }
    }

    // Active current streak ending at the latest completed day
    val latestCompletedDay = sortedPlans
        .filter { it.completedBlocksCount > 0 }
        .maxOfOrNull { it.dayNumber } ?: 0

    var activeStreak = 0
    val activeDays = mutableListOf<Int>()
    if (latestCompletedDay > 0) {
        var day = latestCompletedDay
        while (day >= 1 && completedDayNumbers.contains(day)) {
            activeStreak++
            activeDays.add(day)
            day--
        }
    }

    return StreakData(
        currentStreak = activeStreak,
        longestStreak = maxStreak,
        totalActiveDays = completedDayNumbers.size,
        streakGoal = 31,
        completedDayNumbers = completedDayNumbers,
        consecutiveDays = activeDays.reversed()
    )
}

/**
 * Visual Tracker Card for the 31-Day Study Streak.
 * Displays consecutive days of completed study blocks as a progress ring
 * with 31 daily graduation markers, live streak analytics, and an interactive
 * 31-day visual continuity ribbon.
 */
@Composable
fun ThirtyOneDayStreakCard(
    dayPlans: List<DayPlan>,
    modifier: Modifier = Modifier,
    onNavigateToDay: (Int) -> Unit = {}
) {
    val streakData = remember(dayPlans) { calculate31DayStreak(dayPlans) }
    val progressFraction = (streakData.currentStreak.toFloat() / streakData.streakGoal.toFloat()).coerceIn(0f, 1f)

    val animatedProgress by animateFloatAsState(
        targetValue = progressFraction,
        animationSpec = tween(durationMillis = 1000),
        label = "streak_ring_progress"
    )

    // Motivational status text
    val (statusTitle, statusColor) = when {
        streakData.currentStreak >= 31 -> "🏆 31-Day Master Champion!" to Color(0xFF059669)
        streakData.currentStreak >= 21 -> "⚡ 21-Day Elite Discipline!" to Color(0xFF7C3AED)
        streakData.currentStreak >= 14 -> "🔥 14-Day Halfway Milestone!" to Color(0xFFEA580C)
        streakData.currentStreak >= 7 -> "🚀 7-Day Unstoppable Habit!" to Color(0xFF2563EB)
        streakData.currentStreak > 0 -> "✨ ${streakData.currentStreak} Days Unbroken" to Color(0xFFD97706)
        else -> "🌱 Day 1 Ready to Begin" to Color(0xFF64748B)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .testTag("thirty_one_day_streak_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = PureWhiteSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, LavenderOutline)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // Header: Title + Motivational Milestone Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFFFFF7ED),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFDBA74)),
                        modifier = Modifier.size(38.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.LocalFireDepartment,
                                contentDescription = "Streak Fire",
                                tint = Color(0xFFEA580C),
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "31-Day Study Streak Tracker",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Consecutive Days of Completed Study Blocks",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = statusColor.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = statusTitle,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = statusColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Main Visual: 31-Day Progress Ring + Streak Key Metrics
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 31-Day Progress Ring
                Box(
                    modifier = Modifier
                        .size(145.dp)
                        .testTag("streak_progress_ring"),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.size(135.dp)) {
                        val strokeW = 12.dp.toPx()
                        val diameter = size.minDimension - strokeW
                        val topLeft = Offset(strokeW / 2, strokeW / 2)
                        val arcSize = Size(diameter, diameter)
                        val centerOffset = Offset(size.width / 2, size.height / 2)
                        val radius = diameter / 2f

                        // 1. Background Track
                        drawArc(
                            color = Color(0xFFF1F5F9),
                            startAngle = -90f,
                            sweepAngle = 360f,
                            useCenter = false,
                            topLeft = topLeft,
                            size = arcSize,
                            style = Stroke(width = strokeW, cap = StrokeCap.Round)
                        )

                        // 2. 31 Tick Markers around the circumference
                        val totalTicks = 31
                        for (i in 0 until totalTicks) {
                            val angleDeg = -90f + (i.toFloat() / totalTicks.toFloat()) * 360f
                            val angleRad = Math.toRadians(angleDeg.toDouble())
                            val isCompletedTick = streakData.completedDayNumbers.contains(i + 1)

                            val tickInnerR = radius - strokeW / 2 - 3.dp.toPx()
                            val tickOuterR = radius + strokeW / 2 + 3.dp.toPx()

                            val startX = centerOffset.x + (tickInnerR * cos(angleRad)).toFloat()
                            val startY = centerOffset.y + (tickInnerR * sin(angleRad)).toFloat()
                            val endX = centerOffset.x + (tickOuterR * cos(angleRad)).toFloat()
                            val endY = centerOffset.y + (tickOuterR * sin(angleRad)).toFloat()

                            drawLine(
                                color = if (isCompletedTick) Color(0xFFF97316).copy(alpha = 0.6f) else Color(0xFFCBD5E1).copy(alpha = 0.4f),
                                start = Offset(startX, startY),
                                end = Offset(endX, endY),
                                strokeWidth = 1.2.dp.toPx()
                            )
                        }

                        // 3. Animated Streak Progress Arc with gradient
                        if (animatedProgress > 0f) {
                            drawArc(
                                brush = Brush.sweepGradient(
                                    colors = listOf(
                                        Color(0xFFF59E0B), // Amber
                                        Color(0xFFEA580C), // Orange flame
                                        Color(0xFF8B5CF6), // Royal purple
                                        Color(0xFFF59E0B)  // Wrap
                                    )
                                ),
                                startAngle = -90f,
                                sweepAngle = animatedProgress * 360f,
                                useCenter = false,
                                topLeft = topLeft,
                                size = arcSize,
                                style = Stroke(width = strokeW, cap = StrokeCap.Round)
                            )
                        }
                    }

                    // Inside Ring: Streak Value & Flame Center
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = null,
                            tint = Color(0xFFEA580C),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${streakData.currentStreak}",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF0F172A),
                            lineHeight = 28.sp
                        )
                        Text(
                            text = "DAY STREAK",
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF64748B),
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "${streakData.currentStreak}/31 Days",
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = LavenderPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.width(18.dp))

                // Right Column: Streak KPI Metrics
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StreakMetricRow(
                        icon = Icons.Default.FlashOn,
                        iconColor = Color(0xFFEA580C),
                        label = "CURRENT STREAK",
                        value = "${streakData.currentStreak} Days",
                        subtext = if (streakData.currentStreak > 0) "Active & unbroken" else "Complete today's block"
                    )

                    StreakMetricRow(
                        icon = Icons.Default.EmojiEvents,
                        iconColor = Color(0xFFD97706),
                        label = "BEST STREAK",
                        value = "${streakData.longestStreak} Days",
                        subtext = "Personal Record"
                    )

                    StreakMetricRow(
                        icon = Icons.Default.Flag,
                        iconColor = Color(0xFF7C3AED),
                        label = "REMAINING TO 31",
                        value = "${(streakData.streakGoal - streakData.currentStreak).coerceAtLeast(0)} Days",
                        subtext = "To full 31-day master"
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 31-Day Visual Continuity Ribbon (Days 1 to 31)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "31-Day Visual Journey (Tap Day to Study):",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${streakData.totalActiveDays} of 31 Days Active",
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF059669)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Scrollable 31-day pills
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                for (dayNum in 1..31) {
                    val isCompleted = streakData.completedDayNumbers.contains(dayNum)
                    val isInActiveStreak = streakData.consecutiveDays.contains(dayNum)

                    val pillBg = when {
                        isInActiveStreak -> Color(0xFFFFF7ED)
                        isCompleted -> Color(0xFFF0FDF4)
                        else -> LightLavenderBackground
                    }

                    val pillBorder = when {
                        isInActiveStreak -> Color(0xFFEA580C)
                        isCompleted -> Color(0xFF10B981)
                        else -> LavenderOutline.copy(alpha = 0.6f)
                    }

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = pillBg,
                        border = androidx.compose.foundation.BorderStroke(1.dp, pillBorder),
                        modifier = Modifier
                            .clickable { onNavigateToDay(dayNum) }
                            .testTag("streak_pill_day_$dayNum")
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "D$dayNum",
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isInActiveStreak) Color(0xFFEA580C) else MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            if (isInActiveStreak) {
                                Icon(
                                    imageVector = Icons.Default.LocalFireDepartment,
                                    contentDescription = null,
                                    tint = Color(0xFFEA580C),
                                    modifier = Modifier.size(12.dp)
                                )
                            } else if (isCompleted) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = Color(0xFF10B981),
                                    modifier = Modifier.size(12.dp)
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFCBD5E1))
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StreakMetricRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    label: String,
    value: String,
    subtext: String
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = iconColor.copy(alpha = 0.06f),
        border = androidx.compose.foundation.BorderStroke(1.dp, iconColor.copy(alpha = 0.18f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = iconColor
                    )
                    Text(
                        text = value,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Text(
                    text = subtext,
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 8.5.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
