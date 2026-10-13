package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DayPlan
import com.example.data.model.TopicRevision
import com.example.ui.components.TopicStatusSelector
import com.example.ui.components.getStatusContainerColor
import com.example.ui.components.getStatusColor
import com.example.ui.viewmodel.PlannerViewModel

@Composable
fun RoadmapScreen(
    viewModel: PlannerViewModel,
    modifier: Modifier = Modifier,
    onDaySelected: (Int) -> Unit = {}
) {
    val dayPlans by viewModel.dayPlans.collectAsState()
    val topicRevisions by viewModel.topicRevisions.collectAsState()
    var selectedPhaseFilter by remember { mutableIntStateOf(0) } // 0: All, 1: Phase 1, 2: Phase 2, 3: Phase 3
    var expandedDayNumber by remember { mutableStateOf<Int?>(null) }

    val filteredDays = remember(dayPlans, selectedPhaseFilter) {
        if (selectedPhaseFilter == 0) dayPlans
        else dayPlans.filter { it.phase == selectedPhaseFilter }
    }

    val totalCompletedDays = dayPlans.count { it.isCompleted }
    val totalDays = if (dayPlans.isNotEmpty()) dayPlans.size else 31
    val overallProgress = if (dayPlans.isNotEmpty()) totalCompletedDays.toFloat() / dayPlans.size.toFloat() else 0f

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("roadmap_screen_column"),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // Overall 31-Day Master Tracker Header
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "31-Day Master Roadmap",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = "$totalCompletedDays of $totalDays Days Finished (${(overallProgress * 100).toInt()}%)",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(4.dp)
                        ) {
                            Text(
                                text = "SSC CGL",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    LinearProgressIndicator(
                        progress = { overallProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
                    )
                }
            }
        }

        // Phase Selector Tabs
        item {
            TabRow(
                selectedTabIndex = selectedPhaseFilter,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                containerColor = Color.Transparent,
                divider = {}
            ) {
                listOf("All (31D)", "Phase 1 (1-10)", "Phase 2 (11-20)", "Phase 3 (21-31)").forEachIndexed { index, title ->
                    Tab(
                        selected = selectedPhaseFilter == index,
                        onClick = { selectedPhaseFilter = index },
                        text = {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = if (selectedPhaseFilter == index) FontWeight.Bold else FontWeight.Normal,
                                maxLines = 1
                            )
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        // List of Day Cards
        items(filteredDays, key = { it.dayNumber }) { day ->
            val isExpanded = expandedDayNumber == day.dayNumber
            val dayTopics = topicRevisions.filter { it.dayNumber == day.dayNumber }

            RoadmapDayCard(
                dayPlan = day,
                topics = dayTopics,
                isExpanded = isExpanded,
                onToggleExpand = {
                    expandedDayNumber = if (isExpanded) null else day.dayNumber
                },
                onGoToDay = {
                    viewModel.selectDay(day.dayNumber)
                    onDaySelected(day.dayNumber)
                },
                onUpdateTopicStatus = { topicId, newStatus ->
                    viewModel.updateTopicRevisionStatus(topicId, newStatus)
                }
            )
        }
    }
}

@Composable
fun RoadmapDayCard(
    dayPlan: DayPlan,
    topics: List<TopicRevision>,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onGoToDay: () -> Unit,
    onUpdateTopicStatus: (Long, String) -> Unit
) {
    val phaseColor = when (dayPlan.phase) {
        1 -> MaterialTheme.colorScheme.primary
        2 -> MaterialTheme.colorScheme.secondary
        else -> Color(0xFF7C3AED)
    }

    val phaseLabel = when (dayPlan.phase) {
        1 -> "Phase 1: Foundation"
        2 -> "Phase 2: Speed Drills"
        else -> "Phase 3: Simulation"
    }

    val masteredCount = topics.count { it.status == "Mastered" }
    val reviewCount = topics.count { it.status == "Needs Review" }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .border(
                width = if (dayPlan.isCompleted) 1.dp else 0.5.dp,
                color = if (dayPlan.isCompleted) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                shape = RoundedCornerShape(16.dp)
            )
            .testTag("day_card_${dayPlan.dayNumber}"),
        colors = CardDefaults.cardColors(
            containerColor = if (dayPlan.isCompleted) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggleExpand() },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = phaseColor.copy(alpha = 0.15f),
                        modifier = Modifier.size(42.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "D${dayPlan.dayNumber}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = phaseColor
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = phaseLabel,
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = phaseColor
                            )
                            if (dayPlan.isCompleted) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f)
                                ) {
                                    Text(
                                        text = "COMPLETED",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.tertiary,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                    )
                                }
                            }
                        }

                        Text(
                            text = dayPlan.title,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            maxLines = if (isExpanded) 3 else 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                IconButton(onClick = onToggleExpand) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = "Expand day"
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Task Progress Bar & Topic Mastery Summary
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                LinearProgressIndicator(
                    progress = { dayPlan.progressPercent() },
                    modifier = Modifier
                        .weight(1f)
                        .height(5.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = if (dayPlan.isCompleted) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "${dayPlan.completedTaskCount()}/10 tasks",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (topics.isNotEmpty()) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = if (masteredCount > 0) Color(0xFFDCFCE7) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                    ) {
                        Text(
                            text = "$masteredCount/${topics.size} Rev. Mastered",
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (masteredCount > 0) Color(0xFF16A34A) else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            // Expanded Subject Details & Interactive Topic Revision Statuses
            if (isExpanded) {
                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Subject Revisions & Preparation Status:",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = "Mark as you revise",
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                if (topics.isNotEmpty()) {
                    topics.forEach { topic ->
                        val subjectColor = when {
                            topic.subject.contains("Quant", ignoreCase = true) -> MaterialTheme.colorScheme.primary
                            topic.subject.contains("Reason", ignoreCase = true) -> Color(0xFF0D9488)
                            topic.subject.contains("English", ignoreCase = true) -> Color(0xFF7C3AED)
                            else -> Color(0xFFEA580C)
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f), RoundedCornerShape(10.dp))
                                .padding(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = topic.subject,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = subjectColor
                                )

                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = getStatusContainerColor(topic.status)
                                ) {
                                    Text(
                                        text = topic.status,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = getStatusColor(topic.status),
                                        fontSize = 10.sp,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(3.dp))

                            Text(
                                text = topic.topicTitle,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            TopicStatusSelector(
                                currentStatus = topic.status,
                                onStatusSelected = { newStatus ->
                                    onUpdateTopicStatus(topic.id, newStatus)
                                }
                            )
                        }
                    }
                } else {
                    DetailedSubjectRow("Quantitative Aptitude", dayPlan.quantTopic, MaterialTheme.colorScheme.primary)
                    DetailedSubjectRow("Reasoning", dayPlan.reasoningTopic, Color(0xFF0D9488))
                    DetailedSubjectRow("English Language", dayPlan.englishTopic, Color(0xFF7C3AED))
                    DetailedSubjectRow("General Awareness", dayPlan.gaTopic, Color(0xFFEA580C))
                }

                DetailedSubjectRow("Mock Test", dayPlan.mockTestName, MaterialTheme.colorScheme.secondary)

                if (dayPlan.userNotes.isNotBlank()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "Personal Notes:",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = dayPlan.userNotes,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onGoToDay,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.Schedule, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Open Day ${dayPlan.dayNumber} Full Schedule")
                }
            }
        }
    }
}

@Composable
fun DetailedSubjectRow(
    subjectName: String,
    content: String,
    accentColor: Color
) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            text = subjectName,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = accentColor
        )
        Text(
            text = content,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
