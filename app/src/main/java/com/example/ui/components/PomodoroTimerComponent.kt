package com.example.ui.components

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PomodoroMode
import com.example.data.model.TimeSlot

@Composable
fun PomodoroTimerComponent(
    slot: TimeSlot,
    topicName: String?,
    mode: PomodoroMode,
    cyclesCompleted: Int,
    secondsRemaining: Int,
    totalSeconds: Int,
    isRunning: Boolean,
    isBlockCompleted: Boolean,
    onModeSelected: (PomodoroMode) -> Unit,
    onPlayPause: () -> Unit,
    onReset: () -> Unit,
    onAdjustTime: (Int) -> Unit,
    onSkipPhase: () -> Unit,
    onToggleBlockComplete: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isBreak = mode.isBreak

    // Theme colors according to Focus vs Break
    val activeColor = if (isBreak) {
        Color(0xFF10B981) // Emerald Green for breaks
    } else {
        MaterialTheme.colorScheme.primary // Indigo/Blue for study focus
    }

    val containerColor = if (isBreak) {
        Color(0xFFECFDF5) // Light mint
    } else {
        MaterialTheme.colorScheme.surface
    }

    val progressFraction = if (totalSeconds > 0) {
        (1f - (secondsRemaining.toFloat() / totalSeconds.toFloat())).coerceIn(0f, 1f)
    } else 0f

    val animatedProgress by animateFloatAsState(
        targetValue = progressFraction,
        animationSpec = tween(durationMillis = 300),
        label = "pomodoro_progress"
    )

    val minutes = secondsRemaining / 60
    val seconds = secondsRemaining % 60
    val formattedTime = String.format("%02d:%02d", minutes, seconds)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("pomodoro_timer_component")
            .border(
                width = 1.5.dp,
                color = activeColor.copy(alpha = 0.5f),
                shape = RoundedCornerShape(20.dp)
            ),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Bar: Mode Badge & Dismiss Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = activeColor.copy(alpha = 0.15f),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = if (isBreak) Icons.Default.Coffee else Icons.Default.Timer,
                                contentDescription = null,
                                tint = activeColor,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isBreak) "BREAK SESSION" else "FOCUS SESSION",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = activeColor
                            )
                        }
                    }

                    Text(
                        text = "${slot.startTime} – ${slot.endTime}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(28.dp).testTag("pomodoro_close_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Dismiss timer",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Study Block Info
            Text(
                text = slot.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if (!topicName.isNullOrBlank()) {
                Text(
                    text = topicName,
                    style = MaterialTheme.typography.bodySmall,
                    color = activeColor,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Pomodoro Mode Selectors Scrollable Row
            val scrollState = rememberScrollState()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PomodoroModeChip(
                    title = "🍅 25m Focus",
                    isSelected = mode == PomodoroMode.POMODORO_25,
                    onClick = { onModeSelected(PomodoroMode.POMODORO_25) }
                )
                PomodoroModeChip(
                    title = "⏱️ Block (${slot.durationMinutes}m)",
                    isSelected = mode == PomodoroMode.FULL_SESSION,
                    onClick = { onModeSelected(PomodoroMode.FULL_SESSION) }
                )
                PomodoroModeChip(
                    title = "☕ 5m Break",
                    isSelected = mode == PomodoroMode.SHORT_BREAK,
                    onClick = { onModeSelected(PomodoroMode.SHORT_BREAK) }
                )
                PomodoroModeChip(
                    title = "🌴 15m Long",
                    isSelected = mode == PomodoroMode.LONG_BREAK,
                    onClick = { onModeSelected(PomodoroMode.LONG_BREAK) }
                )
                PomodoroModeChip(
                    title = "⚡ 50m Deep",
                    isSelected = mode == PomodoroMode.DEEP_FOCUS_50,
                    onClick = { onModeSelected(PomodoroMode.DEEP_FOCUS_50) }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Circular Countdown Dial
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .testTag("pomodoro_dial"),
                contentAlignment = Alignment.Center
            ) {
                val trackColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)

                Canvas(modifier = Modifier.size(150.dp)) {
                    val strokeWidth = 12.dp.toPx()
                    val diameter = size.minDimension - strokeWidth
                    val topLeft = Offset(strokeWidth / 2, strokeWidth / 2)
                    val arcSize = Size(diameter, diameter)

                    // Track Arc
                    drawArc(
                        color = trackColor,
                        startAngle = -90f,
                        sweepAngle = 360f,
                        useCenter = false,
                        topLeft = topLeft,
                        size = arcSize,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )

                    // Progress Arc
                    drawArc(
                        color = activeColor,
                        startAngle = -90f,
                        sweepAngle = animatedProgress * 360f,
                        useCenter = false,
                        topLeft = topLeft,
                        size = arcSize,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                }

                // Digits & Status
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = formattedTime,
                        style = MaterialTheme.typography.displaySmall,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (secondsRemaining < 60 && isRunning) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                    )

                    val statusText = when {
                        secondsRemaining == 0 -> "TIME'S UP!"
                        isRunning -> if (isBreak) "RESTING" else "FOCUSING"
                        else -> "PAUSED"
                    }

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = (if (isRunning) activeColor else MaterialTheme.colorScheme.surfaceVariant).copy(alpha = 0.18f),
                        modifier = Modifier.padding(top = 2.dp)
                    ) {
                        Text(
                            text = statusText,
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isRunning) activeColor else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Pomodoro Cycles Indicator (4 Tomato Cycles)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                val cycleInSet = cyclesCompleted % 4
                (0..3).forEach { index ->
                    val isCycleCompleted = index < cycleInSet
                    val isCurrentCycle = index == cycleInSet && !isBreak

                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(if (isCurrentCycle) 16.dp else 12.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    isCycleCompleted -> activeColor
                                    isCurrentCycle -> activeColor.copy(alpha = 0.45f)
                                    else -> MaterialTheme.colorScheme.surfaceVariant
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCycleCompleted) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(8.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Cycle ${(cyclesCompleted % 4) + 1} of 4",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Quick Fine-Tuning Chips (-1m, +5m, Skip phase)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = { onAdjustTime(-1) },
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                    modifier = Modifier.height(28.dp).testTag("pomodoro_minus_1m")
                ) {
                    Text("-1 min", style = MaterialTheme.typography.labelSmall, fontSize = 11.sp)
                }

                OutlinedButton(
                    onClick = { onAdjustTime(5) },
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                    modifier = Modifier.height(28.dp).testTag("pomodoro_plus_5m")
                ) {
                    Text("+5 min", style = MaterialTheme.typography.labelSmall, fontSize = 11.sp)
                }

                FilledTonalButton(
                    onClick = onSkipPhase,
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                    modifier = Modifier.height(28.dp).testTag("pomodoro_skip_phase")
                ) {
                    Icon(
                        imageVector = Icons.Default.SkipNext,
                        contentDescription = null,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = if (isBreak) "Back to Focus" else "Take Break",
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Main Action Controls: Play/Pause, Reset, Complete Block
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Play/Pause Primary Action
                Button(
                    onClick = onPlayPause,
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("pomodoro_play_pause_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = activeColor)
                ) {
                    Icon(
                        imageVector = if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isRunning) "Pause" else "Start"
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isRunning) "Pause" else if (secondsRemaining == totalSeconds) "Start" else "Resume",
                        fontWeight = FontWeight.Bold
                    )
                }

                // Reset Button
                OutlinedButton(
                    onClick = onReset,
                    modifier = Modifier
                        .height(44.dp)
                        .testTag("pomodoro_reset_button"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Reset"
                    )
                }

                // Mark Complete Action
                Button(
                    onClick = onToggleBlockComplete,
                    modifier = Modifier
                        .height(44.dp)
                        .testTag("pomodoro_mark_complete_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = if (isBlockCompleted) {
                        ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    } else {
                        ButtonDefaults.filledTonalButtonColors()
                    }
                ) {
                    Icon(
                        imageVector = if (isBlockCompleted) Icons.Default.CheckCircle else Icons.Outlined.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isBlockCompleted) "Completed" else "Done",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun PomodoroModeChip(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
        contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier
            .clickable(onClick = onClick)
            .testTag("chip_${title.replace(" ", "_")}")
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}
