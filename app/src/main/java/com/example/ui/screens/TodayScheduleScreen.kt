package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DayPlan
import com.example.data.model.PomodoroMode
import com.example.data.model.ScheduleTemplate
import com.example.data.model.SlotType
import com.example.data.model.TimeSlot
import com.example.ui.components.AddTopicRevisionDialog
import com.example.ui.components.DailySubjectRevisionSection
import com.example.ui.components.PomodoroTimerComponent
import com.example.ui.components.ScheduleVisualizerComponent
import com.example.ui.viewmodel.PlannerViewModel

@Composable
fun TodayScheduleScreen(
    viewModel: PlannerViewModel,
    modifier: Modifier = Modifier,
    onNavigateToMockTest: (Int) -> Unit = {}
) {
    val dayPlans by viewModel.dayPlans.collectAsState()
    val selectedDayNum by viewModel.selectedDayNumber.collectAsState()
    val currentDayPlan by viewModel.currentDayPlan.collectAsState()
    val activeSlot by viewModel.activeSlot.collectAsState()
    val pomodoroMode by viewModel.pomodoroMode.collectAsState()
    val pomodoroCycles by viewModel.pomodoroCycles.collectAsState()
    val timerSecondsRemaining by viewModel.timerSecondsRemaining.collectAsState()
    val timerTotalSeconds by viewModel.timerTotalSeconds.collectAsState()
    val isTimerRunning by viewModel.isTimerRunning.collectAsState()
    val topicRevisions by viewModel.topicRevisions.collectAsState()

    var showNotesDialog by remember { mutableStateOf(false) }
    var showAddTopicDialog by remember { mutableStateOf(false) }
    var notesText by remember(currentDayPlan) { mutableStateOf(currentDayPlan?.userNotes ?: "") }

    val plan = currentDayPlan ?: dayPlans.find { it.dayNumber == selectedDayNum }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("today_schedule_column"),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // Day selector carousel
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Daily Routine (10 AM - 10 PM)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "31-Day Master Schedule • Day $selectedDayNum of 31",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.clickable { showNotesDialog = true }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.EditNote,
                                contentDescription = "Day Notes",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Day Notes",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Horizontal list of days 1 to 31
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.testTag("day_selector_row")
                ) {
                    items((1..31).toList()) { day ->
                        val dayPlanItem = dayPlans.find { it.dayNumber == day }
                        val isSelected = day == selectedDayNum
                        val isCompleted = dayPlanItem?.isCompleted == true
                        val progress = dayPlanItem?.progressPercent() ?: 0f

                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.selectDay(day) },
                            label = {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "Day $day",
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                    if (isCompleted) {
                                        Text(
                                            text = "Done",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontSize = 9.sp,
                                            color = MaterialTheme.colorScheme.tertiary
                                        )
                                    } else if (progress > 0) {
                                        Text(
                                            text = "${(progress * 100).toInt()}%",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontSize = 9.sp
                                        )
                                    }
                                }
                            },
                            leadingIcon = if (isCompleted) {
                                {
                                    Icon(
                                        Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.tertiary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            } else null,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            shape = RoundedCornerShape(16.dp)
                        )
                    }
                }
            }
        }

        // Today's Progress Card
        item {
            if (plan != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .testTag("day_progress_card"),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Day ${plan.dayNumber}: ${plan.title}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "Phase ${plan.phase} Target • ${plan.completedTaskCount()} of ${plan.totalTaskCount()} tasks completed",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                                )
                            }
                            CircularProgressIndicator(
                                progress = { plan.progressPercent() },
                                modifier = Modifier.size(42.dp),
                                color = if (plan.isCompleted) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary,
                                trackColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f),
                                strokeWidth = 4.dp
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        LinearProgressIndicator(
                            progress = { plan.progressPercent() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = if (plan.isCompleted) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.surface
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(
                                onClick = { viewModel.resetDayProgress(plan.dayNumber) },
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "Reset Day",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            FilledTonalButton(
                                onClick = { viewModel.markAllDayBlocksCompleted(plan.dayNumber, true) },
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "Mark All Done",
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        }
                    }
                }
            }
        }

        // 10 AM to 10 PM Visual Schedule Ribbon Component
        item {
            ScheduleVisualizerComponent(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                plan = plan,
                onStartTimer = { slot ->
                    viewModel.startTimerForSlot(slot)
                },
                onToggleCompletion = { slot ->
                    if (plan != null) {
                        val isCurrentlyDone = when (slot.type) {
                            SlotType.STUDY_QUANT -> plan.quantCompleted
                            SlotType.STUDY_REASONING -> plan.reasoningCompleted
                            SlotType.LUNCH -> plan.lunchCompleted
                            SlotType.MOCK_TEST -> plan.mockCompleted
                            SlotType.MOCK_ANALYSIS -> plan.mockAnalysisCompleted
                            SlotType.STUDY_ENGLISH -> plan.englishCompleted
                            SlotType.WALKING -> plan.walkingCompleted
                            SlotType.STUDY_GA -> plan.gaCompleted
                            SlotType.DINNER -> plan.dinnerCompleted
                            SlotType.REVISION -> plan.revisionCompleted
                            SlotType.BREAK -> false
                        }
                        viewModel.markStudyBlockCompleted(plan.dayNumber, slot.type, !isCurrentlyDone)
                    }
                }
            )
        }

        // Pomodoro Active Focus Timer Component (if active slot exists)
        item {
            if (activeSlot != null) {
                val activeTopic = when (activeSlot!!.type) {
                    SlotType.STUDY_QUANT -> plan?.quantTopic
                    SlotType.STUDY_REASONING -> plan?.reasoningTopic
                    SlotType.STUDY_ENGLISH -> plan?.englishTopic
                    SlotType.STUDY_GA -> plan?.gaTopic
                    SlotType.MOCK_TEST -> plan?.mockTestName
                    else -> null
                }
                val isCurrentBlockDone = when (activeSlot!!.type) {
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

                PomodoroTimerComponent(
                    slot = activeSlot!!,
                    topicName = activeTopic,
                    mode = pomodoroMode,
                    cyclesCompleted = pomodoroCycles,
                    secondsRemaining = timerSecondsRemaining,
                    totalSeconds = timerTotalSeconds,
                    isRunning = isTimerRunning,
                    isBlockCompleted = isCurrentBlockDone,
                    onModeSelected = { mode ->
                        viewModel.setPomodoroMode(mode)
                    },
                    onPlayPause = {
                        if (isTimerRunning) viewModel.pauseTimer() else viewModel.resumeTimer()
                    },
                    onReset = { viewModel.resetTimer() },
                    onAdjustTime = { delta -> viewModel.adjustTimer(delta) },
                    onSkipPhase = { viewModel.skipToNextPomodoroPhase() },
                    onToggleBlockComplete = {
                        if (plan != null) {
                            val taskKey = when (activeSlot!!.type) {
                                SlotType.STUDY_QUANT -> "quant"
                                SlotType.STUDY_REASONING -> "reasoning"
                                SlotType.LUNCH -> "lunch"
                                SlotType.MOCK_TEST -> "mock"
                                SlotType.MOCK_ANALYSIS -> "mock_analysis"
                                SlotType.STUDY_ENGLISH -> "english"
                                SlotType.WALKING -> "walking"
                                SlotType.STUDY_GA -> "ga"
                                SlotType.DINNER -> "dinner"
                                SlotType.REVISION -> "revision"
                                SlotType.BREAK -> null
                            }
                            if (taskKey != null) {
                                viewModel.toggleTask(plan.dayNumber, taskKey)
                            }
                        }
                    },
                    onDismiss = { viewModel.dismissActiveTimer() },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }
        }

        // Today's Syllabus Overview
        if (plan != null) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            Icon(
                                Icons.Default.MenuBook,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Day ${plan.dayNumber} Curriculum Targets",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        SubjectBulletItem(
                            subject = "Quant",
                            topic = plan.quantTopic,
                            color = MaterialTheme.colorScheme.primary
                        )
                        SubjectBulletItem(
                            subject = "Reasoning",
                            topic = plan.reasoningTopic,
                            color = Color(0xFF0D9488)
                        )
                        SubjectBulletItem(
                            subject = "English",
                            topic = plan.englishTopic,
                            color = Color(0xFF7C3AED)
                        )
                        SubjectBulletItem(
                            subject = "GA & CA",
                            topic = plan.gaTopic,
                            color = Color(0xFFEA580C)
                        )
                        SubjectBulletItem(
                            subject = "Mock Test",
                            topic = plan.mockTestName,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }

            item {
                DailySubjectRevisionSection(
                    dayNumber = plan.dayNumber,
                    topics = topicRevisions,
                    onStatusChange = { topicId, newStatus ->
                        viewModel.updateTopicRevisionStatus(topicId, newStatus)
                    },
                    onNotesChange = { topicId, notes ->
                        viewModel.updateTopicRevisionNotes(topicId, notes)
                    },
                    onLogTopicClick = { showAddTopicDialog = true }
                )
            }
        }

        // Timeline Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Hourly Schedule (10 AM to 10 PM)",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "12h Window • 8.5h Study",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // 10 AM to 10 PM Schedule Slots
        items(ScheduleTemplate.dailySlots) { slot ->
            val isSlotActive = activeSlot?.id == slot.id
            val isChecked = when (slot.type) {
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

            val taskKey = when (slot.type) {
                SlotType.STUDY_QUANT -> "quant"
                SlotType.STUDY_REASONING -> "reasoning"
                SlotType.LUNCH -> "lunch"
                SlotType.MOCK_TEST -> "mock"
                SlotType.MOCK_ANALYSIS -> "mock_analysis"
                SlotType.STUDY_ENGLISH -> "english"
                SlotType.WALKING -> "walking"
                SlotType.STUDY_GA -> "ga"
                SlotType.DINNER -> "dinner"
                SlotType.REVISION -> "revision"
                SlotType.BREAK -> null
            }

            ScheduleSlotCard(
                slot = slot,
                plan = plan,
                isChecked = isChecked,
                isSlotActive = isSlotActive,
                activeSecondsRemaining = if (isSlotActive) timerSecondsRemaining else null,
                isTimerRunning = if (isSlotActive) isTimerRunning else false,
                onToggleCheck = {
                    if (taskKey != null && plan != null) {
                        viewModel.toggleTask(plan.dayNumber, taskKey)
                    }
                },
                onStartPomodoro = {
                    viewModel.startPomodoroForSlot(slot, PomodoroMode.POMODORO_25)
                },
                onStartFullBlock = {
                    viewModel.startPomodoroForSlot(slot, PomodoroMode.FULL_SESSION)
                },
                onPlayPauseTimer = {
                    if (isTimerRunning) viewModel.pauseTimer() else viewModel.resumeTimer()
                },
                onOpenMockDetails = {
                    if (slot.type == SlotType.MOCK_TEST && plan != null) {
                        onNavigateToMockTest(plan.dayNumber)
                    }
                }
            )
        }
    }

    // Day Notes Dialog
    if (showNotesDialog && plan != null) {
        AlertDialog(
            onDismissRequest = { showNotesDialog = false },
            title = { Text("Day ${plan.dayNumber} Personal Notes") },
            text = {
                Column {
                    Text(
                        "Record your personal doubts, formula reminders, and daily reflection:",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    OutlinedTextField(
                        value = notesText,
                        onValueChange = { notesText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .testTag("day_notes_field"),
                        placeholder = { Text("e.g. Need to revise Circle Tangent theorem; English cloze test accuracy improved...") }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.saveNotes(plan.dayNumber, notesText)
                        showNotesDialog = false
                    }
                ) {
                    Text("Save Notes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showNotesDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Add Topic Revision Dialog
    if (showAddTopicDialog && plan != null) {
        AddTopicRevisionDialog(
            initialDayNumber = plan.dayNumber,
            onDismiss = { showAddTopicDialog = false },
            onConfirm = { day, subject, title, status, notes ->
                viewModel.addTopicRevision(
                    dayNumber = day,
                    subject = subject,
                    topicTitle = title,
                    status = status,
                    notes = notes
                )
                showAddTopicDialog = false
            }
        )
    }
}

@Composable
fun ScheduleSlotCard(
    slot: TimeSlot,
    plan: DayPlan?,
    isChecked: Boolean,
    isSlotActive: Boolean,
    activeSecondsRemaining: Int? = null,
    isTimerRunning: Boolean = false,
    onToggleCheck: () -> Unit,
    onStartPomodoro: () -> Unit,
    onStartFullBlock: () -> Unit,
    onPlayPauseTimer: () -> Unit,
    onOpenMockDetails: () -> Unit
) {
    val cardColor = when {
        isChecked -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
        slot.type == SlotType.MOCK_TEST -> MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.35f)
        slot.type == SlotType.WALKING -> MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.35f)
        slot.isBreakOrMealOrWalk -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        else -> MaterialTheme.colorScheme.surface
    }

    val icon: ImageVector = when (slot.type) {
        SlotType.STUDY_QUANT -> Icons.Default.Calculate
        SlotType.BREAK -> Icons.Default.Coffee
        SlotType.STUDY_REASONING -> Icons.Default.Psychology
        SlotType.LUNCH -> Icons.Default.Restaurant
        SlotType.MOCK_TEST -> Icons.Default.Assignment
        SlotType.MOCK_ANALYSIS -> Icons.Default.Insights
        SlotType.STUDY_ENGLISH -> Icons.Default.MenuBook
        SlotType.WALKING -> Icons.Default.DirectionsWalk
        SlotType.STUDY_GA -> Icons.Default.Public
        SlotType.DINNER -> Icons.Default.DinnerDining
        SlotType.REVISION -> Icons.Default.AutoFixHigh
    }

    val badgeColor = when (slot.type) {
        SlotType.MOCK_TEST, SlotType.MOCK_ANALYSIS -> MaterialTheme.colorScheme.secondary
        SlotType.WALKING -> MaterialTheme.colorScheme.tertiary
        SlotType.LUNCH, SlotType.DINNER, SlotType.BREAK -> MaterialTheme.colorScheme.onSurfaceVariant
        else -> MaterialTheme.colorScheme.primary
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 5.dp)
            .border(
                width = if (isSlotActive) 1.5.dp else 0.5.dp,
                color = if (isSlotActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                shape = RoundedCornerShape(14.dp)
            )
            .testTag("slot_card_${slot.id}"),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Leading icon container
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = badgeColor.copy(alpha = 0.15f),
                    modifier = Modifier.size(38.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = badgeColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Title & Time info
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${slot.startTime} – ${slot.endTime}",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = badgeColor
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = badgeColor.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "${slot.durationMinutes}m",
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = badgeColor,
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                            )
                        }

                        if (isSlotActive) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = MaterialTheme.colorScheme.primary
                            ) {
                                Text(
                                    text = "ACTIVE",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        }
                    }

                    Text(
                        text = slot.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                // Checkbox for completion (if not pure break)
                if (slot.type != SlotType.BREAK) {
                    IconButton(
                        onClick = onToggleCheck,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("check_${slot.id}")
                    ) {
                        Icon(
                            imageVector = if (isChecked) Icons.Default.CheckCircle else Icons.Outlined.Circle,
                            contentDescription = "Mark task completed",
                            tint = if (isChecked) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Subtitle / Topic details
            val customTopicNote = when (slot.type) {
                SlotType.STUDY_QUANT -> plan?.quantTopic
                SlotType.STUDY_REASONING -> plan?.reasoningTopic
                SlotType.STUDY_ENGLISH -> plan?.englishTopic
                SlotType.STUDY_GA -> plan?.gaTopic
                SlotType.MOCK_TEST -> plan?.mockTestName
                else -> null
            }

            Text(
                text = customTopicNote ?: slot.subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 50.dp)
            )

            // Active Countdown Badge (when timer is currently running for this slot)
            if (isSlotActive && activeSecondsRemaining != null) {
                val activeMin = activeSecondsRemaining / 60
                val activeSec = activeSecondsRemaining % 60
                val formattedCountdown = String.format("%02d:%02d", activeMin, activeSec)

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
                    modifier = Modifier
                        .padding(start = 50.dp, top = 8.dp)
                        .testTag("slot_countdown_${slot.id}")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Remaining: $formattedCountdown",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(
                            onClick = onPlayPauseTimer,
                            modifier = Modifier.size(22.dp)
                        ) {
                            Icon(
                                imageVector = if (isTimerRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (isTimerRunning) "Pause" else "Resume",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            // Actions (Pomodoro 25m, Full Block countdown, Score Tracker)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 50.dp, top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilledTonalButton(
                    onClick = onStartPomodoro,
                    modifier = Modifier
                        .height(32.dp)
                        .testTag("pomodoro_btn_${slot.id}"),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "🍅 25m Focus",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                OutlinedButton(
                    onClick = onStartFullBlock,
                    modifier = Modifier
                        .height(32.dp)
                        .testTag("full_block_btn_${slot.id}"),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = null,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${slot.durationMinutes}m Block",
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 11.sp
                    )
                }

                if (slot.type == SlotType.MOCK_TEST) {
                    Button(
                        onClick = onOpenMockDetails,
                        modifier = Modifier
                            .height(32.dp)
                            .testTag("mock_tracker_btn"),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Score,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Score Tracker",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SubjectBulletItem(
    subject: String,
    topic: String,
    color: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        verticalAlignment = Alignment.Top
    ) {
        Surface(
            shape = RoundedCornerShape(4.dp),
            color = color.copy(alpha = 0.15f),
            modifier = Modifier.padding(top = 2.dp)
        ) {
            Text(
                text = subject,
                style = MaterialTheme.typography.labelSmall,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = color,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = topic,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}
