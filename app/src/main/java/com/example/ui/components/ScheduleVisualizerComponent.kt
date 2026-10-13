package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DayPlan
import com.example.data.model.ScheduleTemplate
import com.example.data.model.SlotType
import com.example.data.model.TimeSlot
import java.util.Calendar

// Color palette for schedule categories (harmonized with light lavender theme)
object ScheduleColors {
    val Study = Color(0xFF7E57C2)         // Rich Lavender Focus
    val MockTest = Color(0xFF9333EA)      // Vivid Purple / Violet
    val MockAnalysis = Color(0xFF6B21A8)  // Deep Imperial Violet
    val Meal = Color(0xFFF59E0B)          // Warm Amber / Golden Orange
    val Walk = Color(0xFF10B981)          // Fresh Emerald Green
    val Revision = Color(0xFFD946EF)      // Orchid / Lavender-Rose
    val Break = Color(0xFF8C82A2)         // Soft Lavender-Slate
}

/**
 * Returns the theme color for each slot type according to the required color-coding:
 * - Study: Blue
 * - Mock Test & Analysis: Purple / Violet
 * - Meals: Amber / Orange
 * - Walk: Emerald Green
 * - Revision: Coral
 * - Break: Slate
 */
fun getSlotColor(type: SlotType): Color = when (type) {
    SlotType.STUDY_QUANT,
    SlotType.STUDY_REASONING,
    SlotType.STUDY_ENGLISH,
    SlotType.STUDY_GA -> ScheduleColors.Study
    SlotType.MOCK_TEST -> ScheduleColors.MockTest
    SlotType.MOCK_ANALYSIS -> ScheduleColors.MockAnalysis
    SlotType.LUNCH, SlotType.DINNER -> ScheduleColors.Meal
    SlotType.WALKING -> ScheduleColors.Walk
    SlotType.REVISION -> ScheduleColors.Revision
    SlotType.BREAK -> ScheduleColors.Break
}

fun getSlotCategoryLabel(type: SlotType): String = when (type) {
    SlotType.STUDY_QUANT,
    SlotType.STUDY_REASONING,
    SlotType.STUDY_ENGLISH,
    SlotType.STUDY_GA -> "Study"
    SlotType.MOCK_TEST -> "Mock Test"
    SlotType.MOCK_ANALYSIS -> "Mock Analysis"
    SlotType.LUNCH, SlotType.DINNER -> "Meal"
    SlotType.WALKING -> "Walk"
    SlotType.REVISION -> "Revision"
    SlotType.BREAK -> "Break"
}

fun getSlotIcon(type: SlotType): ImageVector = when (type) {
    SlotType.STUDY_QUANT -> Icons.Default.Calculate
    SlotType.STUDY_REASONING -> Icons.Default.Psychology
    SlotType.STUDY_ENGLISH -> Icons.Default.MenuBook
    SlotType.STUDY_GA -> Icons.Default.Public
    SlotType.MOCK_TEST -> Icons.Default.Assignment
    SlotType.MOCK_ANALYSIS -> Icons.Default.Insights
    SlotType.LUNCH -> Icons.Default.Restaurant
    SlotType.DINNER -> Icons.Default.DinnerDining
    SlotType.WALKING -> Icons.Default.DirectionsWalk
    SlotType.REVISION -> Icons.Default.AutoFixHigh
    SlotType.BREAK -> Icons.Default.Coffee
}

@Composable
fun ScheduleVisualizerComponent(
    modifier: Modifier = Modifier,
    plan: DayPlan?,
    onSlotSelected: (TimeSlot) -> Unit = {},
    onStartTimer: (TimeSlot) -> Unit = {},
    onToggleCompletion: (TimeSlot) -> Unit = {}
) {
    var selectedSlotId by remember { mutableStateOf<String?>("slot_5_mock") } // Default preview to Mock Test
    val selectedSlot = ScheduleTemplate.dailySlots.find { it.id == selectedSlotId } ?: ScheduleTemplate.dailySlots.first()

    // Calculate current time position relative to 10 AM (600 min) - 10 PM (1320 min)
    val now = Calendar.getInstance()
    val currentMinuteOfDay = now.get(Calendar.HOUR_OF_DAY) * 60 + now.get(Calendar.MINUTE)
    val isInScheduleWindow = currentMinuteOfDay in 600..1320
    val timelineProgress = if (isInScheduleWindow) {
        (currentMinuteOfDay - 600).toFloat() / 720f
    } else null

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("schedule_visualizer_component"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header Row with Title & 10 AM - 10 PM Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "10 AM – 10 PM Visual Schedule",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Text(
                        text = "Color-coded 12-hour continuous routine tracker",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = "12 Hours Total",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Category Legend Badges Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CategoryLegendBadge(label = "Study (4.75h)", color = ScheduleColors.Study, icon = Icons.Default.MenuBook)
                CategoryLegendBadge(label = "Mock & Analysis (2h)", color = ScheduleColors.MockTest, icon = Icons.Default.Assignment)
                CategoryLegendBadge(label = "Meals (2h)", color = ScheduleColors.Meal, icon = Icons.Default.Restaurant)
                CategoryLegendBadge(label = "Walking (1h)", color = ScheduleColors.Walk, icon = Icons.Default.DirectionsWalk)
                CategoryLegendBadge(label = "Revision (1h)", color = ScheduleColors.Revision, icon = Icons.Default.AutoFixHigh)
                CategoryLegendBadge(label = "Breaks (45m)", color = ScheduleColors.Break, icon = Icons.Default.Coffee)
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Time axis indicators (10 AM, 1 PM, 4 PM, 7 PM, 10 PM)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("10 AM", style = MaterialTheme.typography.labelSmall, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("1 PM", style = MaterialTheme.typography.labelSmall, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("4 PM", style = MaterialTheme.typography.labelSmall, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("7 PM", style = MaterialTheme.typography.labelSmall, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("10 PM", style = MaterialTheme.typography.labelSmall, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Proportional Multi-Segment Horizontal Gantt Timeline Bar
            // Total minutes = 720
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize()
                ) {
                    ScheduleTemplate.dailySlots.forEach { slot ->
                        val isSelected = slot.id == selectedSlotId
                        val color = getSlotColor(slot.type)
                        val weight = slot.durationMinutes.toFloat() / 720f

                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(weight)
                                .background(if (isSelected) color else color.copy(alpha = 0.85f))
                                .border(
                                    width = if (isSelected) 2.dp else 0.5.dp,
                                    color = if (isSelected) Color.White else Color.Black.copy(alpha = 0.15f)
                                )
                                .clickable {
                                    selectedSlotId = slot.id
                                    onSlotSelected(slot)
                                }
                                .testTag("timeline_segment_${slot.id}"),
                            contentAlignment = Alignment.Center
                        ) {
                            // Show small icon if slot is wide enough (> 30 min)
                            if (slot.durationMinutes >= 45) {
                                Icon(
                                    imageVector = getSlotIcon(slot.type),
                                    contentDescription = slot.title,
                                    tint = Color.White.copy(alpha = 0.9f),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }

                // Live Current Time Indicator Marker
                if (timelineProgress != null) {
                    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                        val indicatorOffset = maxWidth * timelineProgress
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(3.dp)
                                .offset(x = indicatorOffset)
                                .background(Color.Red)
                        )
                    }
                }
            }

            // Current Time Status text if within 10 AM - 10 PM
            if (timelineProgress != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(Color.Red, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Current Time Position",
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 10.sp,
                        color = Color.Red,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Interactive Selected Slot Card
            SelectedSlotDetailCard(
                slot = selectedSlot,
                plan = plan,
                onStartTimer = { onStartTimer(selectedSlot) },
                onToggleCheck = { onToggleCompletion(selectedSlot) }
            )
        }
    }
}

@Composable
fun CategoryLegendBadge(
    label: String,
    color: Color,
    icon: ImageVector
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = color.copy(alpha = 0.12f),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                color = color
            )
        }
    }
}

@Composable
fun SelectedSlotDetailCard(
    slot: TimeSlot,
    plan: DayPlan?,
    onStartTimer: () -> Unit,
    onToggleCheck: () -> Unit
) {
    val categoryColor = getSlotColor(slot.type)
    val categoryLabel = getSlotCategoryLabel(slot.type)
    val icon = getSlotIcon(slot.type)

    val isCompleted = when (slot.type) {
        SlotType.STUDY_QUANT -> plan?.quantCompleted == true
        SlotType.STUDY_REASONING -> plan?.reasoningCompleted == true
        SlotType.LUNCH -> plan?.lunchCompleted == true
        SlotType.MOCK_TEST -> plan?.mockCompleted == true
        SlotType.MOCK_ANALYSIS -> plan?.mockAnalysisCompleted == true
        SlotType.STUDY_ENGLISH -> plan?.englishCompleted == true
        SlotType.WALKING -> plan?.walkingCompleted == true
        SlotType.STUDY_GA -> plan?.gaCompleted == true
        SlotType.DINNER -> plan?.dinnerCompleted == true
        SlotType.REVISION -> plan?.revisionCompleted == true
        SlotType.BREAK -> false
    }

    val customTopic = when (slot.type) {
        SlotType.STUDY_QUANT -> plan?.quantTopic
        SlotType.STUDY_REASONING -> plan?.reasoningTopic
        SlotType.STUDY_ENGLISH -> plan?.englishTopic
        SlotType.STUDY_GA -> plan?.gaTopic
        SlotType.MOCK_TEST -> plan?.mockTestName
        else -> null
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = categoryColor.copy(alpha = 0.4f),
                shape = RoundedCornerShape(14.dp)
            )
            .testTag("selected_slot_detail_card"),
        colors = CardDefaults.cardColors(
            containerColor = categoryColor.copy(alpha = 0.08f)
        ),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = categoryColor,
                        modifier = Modifier.size(38.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = categoryColor.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = categoryLabel.uppercase(),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = categoryColor,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${slot.startTime} – ${slot.endTime} (${slot.durationMinutes}m)",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Text(
                            text = slot.title,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                if (slot.type != SlotType.BREAK) {
                    IconButton(
                        onClick = onToggleCheck,
                        modifier = Modifier.testTag("selected_slot_check_btn")
                    ) {
                        Icon(
                            imageVector = if (isCompleted) Icons.Default.CheckCircle else Icons.Outlined.Circle,
                            contentDescription = "Mark Complete",
                            tint = if (isCompleted) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = customTopic ?: slot.subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 48.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 48.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onStartTimer,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = categoryColor),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier.height(34.dp)
                ) {
                    Icon(
                        Icons.Default.Timer,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Focus Timer (${slot.durationMinutes}m)", style = MaterialTheme.typography.labelSmall)
                }

                if (slot.type != SlotType.BREAK) {
                    OutlinedButton(
                        onClick = onToggleCheck,
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Text(
                            text = if (isCompleted) "Completed" else "Mark Done",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        }
    }
}
