package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DayPlan
import com.example.data.model.MockTest
import com.example.ui.components.MockScoreDialog
import com.example.ui.components.AddTopicRevisionDialog
import com.example.ui.components.MockScoreTrendChartCard
import com.example.ui.components.SubjectDrillDownCard
import com.example.ui.components.SubjectRevisionRoadmapSummaryCard
import com.example.ui.components.ThirtyOneDayStreakCard
import com.example.ui.viewmodel.PlannerViewModel

@Composable
fun SummaryDashboardScreen(
    viewModel: PlannerViewModel,
    modifier: Modifier = Modifier,
    onNavigateToDay: (Int) -> Unit = {},
    onNavigateToMocks: () -> Unit = {}
) {
    val dayPlans by viewModel.dayPlans.collectAsState()
    val mockTests by viewModel.mockTests.collectAsState()
    val totalCompletedBlocks by viewModel.totalCompletedBlocks.collectAsState()
    val overallProgress by viewModel.overallStudyProgress.collectAsState()
    val completedDaysCount by viewModel.completedDaysCount.collectAsState()
    val attemptedMocksCount by viewModel.attemptedMocksCount.collectAsState()
    val topicRevisions by viewModel.topicRevisions.collectAsState()

    // 31-day Goal Constants
    val totalGoalBlocks = 310 // 31 days * 10 blocks/day
    val totalDaysGoal = 31
    val totalMocksGoal = 31

    // Selected Day in interactive 30-Day Bar Chart
    var selectedBarDay by remember { mutableStateOf<DayPlan?>(null) }

    // Mock Score Logging Dialog State
    var loggingMockTest by remember { mutableStateOf<MockTest?>(null) }
    var showScoreDialog by remember { mutableStateOf(false) }
    var showAddTopicDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("summary_dashboard_screen"),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // Dashboard Title & Overview Header
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "31-Day Progress Summary",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Study Blocks Completed & 31-Day Roadmap Preparation",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text(
                            text = "${(overallProgress * 100).toInt()}% Done",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        // 31-Day Consecutive Study Streak Tracker
        item {
            ThirtyOneDayStreakCard(
                dayPlans = dayPlans,
                onNavigateToDay = onNavigateToDay
            )
        }

        // Section 1: Recharts Radial Ring Hero Card
        item {
            RadialGoalProgressCard(
                completedBlocks = totalCompletedBlocks,
                totalGoalBlocks = totalGoalBlocks,
                completedDays = completedDaysCount,
                totalDays = totalDaysGoal,
                attemptedMocks = attemptedMocksCount,
                totalMocks = totalMocksGoal,
                progressPercent = overallProgress
            )
        }

        // Section 2: Phase-Wise Completion Bar Chart (Target vs Actual)
        item {
            PhaseCompletionBarChartCard(dayPlans = dayPlans)
        }

        // Section 3: 30-Day Daily Block Distribution Chart
        item {
            DailyBlockDistributionChartCard(
                dayPlans = dayPlans,
                selectedDay = selectedBarDay,
                onSelectDay = { selectedBarDay = it },
                onNavigateToDay = onNavigateToDay
            )
        }

        // Section 4: SSC CGL Subject Performance Drill-Down View (Maths, English, Reasoning, GS)
        item {
            SubjectDrillDownCard(
                dayPlans = dayPlans,
                mockTests = mockTests,
                onNavigateToDay = onNavigateToDay
            )
        }

        // Section 5: Overall Subject-wise Block Completion Progress
        item {
            SubjectMasteryBreakdownCard(dayPlans = dayPlans)
        }

        // Section: 31-Day Subject Revision Mastery Roadmap Tracker
        item {
            val masteredCount = topicRevisions.count { it.status == "Mastered" }
            val needsReviewCount = topicRevisions.count { it.status == "Needs Review" }
            val notStartedCount = topicRevisions.count { it.status == "Not Started" }

            SubjectRevisionRoadmapSummaryCard(
                totalTopics = topicRevisions.size,
                masteredCount = masteredCount,
                needsReviewCount = needsReviewCount,
                notStartedCount = notStartedCount,
                onLogNewTopicClick = { showAddTopicDialog = true }
            )
        }

        // Section 6: Mock Test Score Progression Trend Line Card
        item {
            MockScoreTrendChartCard(
                mockTests = mockTests,
                onLogScoreClick = {
                    val nextPending = mockTests.firstOrNull { !it.isAttempted } ?: mockTests.firstOrNull() ?: MockTest(1, "SSC CGL Tier-1 Full Mock #1")
                    loggingMockTest = nextPending
                    showScoreDialog = true
                },
                onSelectMockForEdit = { mock ->
                    loggingMockTest = mock
                    showScoreDialog = true
                }
            )
        }

        // Section 6: Mock Test Completion & Overall Milestone Card
        item {
            MockTestPerformanceSummaryCard(
                mockTests = mockTests,
                onNavigateToMocks = onNavigateToMocks
            )
        }
    }

    // Mock Score Logging / Updating Dialog
    if (showScoreDialog && loggingMockTest != null) {
        MockScoreDialog(
            mockTest = loggingMockTest!!,
            availableTests = mockTests,
            allowChangeTestNumber = true,
            onDismiss = {
                showScoreDialog = false
                loggingMockTest = null
            },
            onSave = { updated ->
                viewModel.saveMockTest(
                    testNumber = updated.testNumber,
                    provider = updated.provider,
                    quantScore = updated.quantScore,
                    reasoningScore = updated.reasoningScore,
                    englishScore = updated.englishScore,
                    gaScore = updated.gaScore,
                    attempted = updated.attemptedQuestions,
                    correct = updated.correctAnswers,
                    wrong = updated.wrongAnswers,
                    timeMinutes = updated.timeTakenMinutes,
                    errorAnalysis = updated.errorAnalysis,
                    weakTopics = updated.weakTopics,
                    dateAttemptedMillis = updated.dateAttemptedMillis
                )
                showScoreDialog = false
                loggingMockTest = null
            },
            onReset = { testNum ->
                viewModel.resetMockTest(testNum)
                showScoreDialog = false
                loggingMockTest = null
            }
        )
    }

    // Add Topic Revision Dialog
    if (showAddTopicDialog) {
        AddTopicRevisionDialog(
            initialDayNumber = 1,
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

// -------------------------------------------------------------
// Component 1: Radial Goal Progress Hero Card (Recharts Radial Ring)
// -------------------------------------------------------------
@Composable
fun RadialGoalProgressCard(
    completedBlocks: Int,
    totalGoalBlocks: Int,
    completedDays: Int,
    totalDays: Int,
    attemptedMocks: Int,
    totalMocks: Int,
    progressPercent: Float
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progressPercent.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 900),
        label = "radial_progress"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .testTag("radial_goal_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.DonutLarge,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "30-Day Target Completion",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = "Goal: $totalGoalBlocks Blocks",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Circular Ring Chart
                Box(
                    modifier = Modifier.size(130.dp),
                    contentAlignment = Alignment.Center
                ) {
                    val primaryColor = MaterialTheme.colorScheme.primary
                    val secondaryColor = MaterialTheme.colorScheme.tertiary
                    val trackColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)

                    Canvas(modifier = Modifier.size(120.dp)) {
                        val strokeWidth = 14.dp.toPx()
                        val diameter = size.minDimension - strokeWidth
                        val topLeftOffset = Offset(strokeWidth / 2, strokeWidth / 2)
                        val arcSize = Size(diameter, diameter)

                        // Background track
                        drawArc(
                            color = trackColor,
                            startAngle = -90f,
                            sweepAngle = 360f,
                            useCenter = false,
                            topLeft = topLeftOffset,
                            size = arcSize,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )

                        // Progress arc
                        drawArc(
                            brush = Brush.sweepGradient(
                                colors = listOf(primaryColor, secondaryColor, primaryColor)
                            ),
                            startAngle = -90f,
                            sweepAngle = animatedProgress * 360f,
                            useCenter = false,
                            topLeft = topLeftOffset,
                            size = arcSize,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${(animatedProgress * 100).toInt()}%",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Achieved",
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.width(20.dp))

                // Key Statistics Legend
                Column(modifier = Modifier.weight(1f)) {
                    ProgressMetricItem(
                        icon = Icons.Default.CheckCircle,
                        iconColor = MaterialTheme.colorScheme.primary,
                        title = "Study Blocks Done",
                        value = "$completedBlocks / $totalGoalBlocks",
                        subtext = "${totalGoalBlocks - completedBlocks} remaining"
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    ProgressMetricItem(
                        icon = Icons.Default.CalendarToday,
                        iconColor = MaterialTheme.colorScheme.tertiary,
                        title = "Days 100% Finished",
                        value = "$completedDays / $totalDays Days",
                        subtext = "${totalDays - completedDays} days left"
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    ProgressMetricItem(
                        icon = Icons.Default.AssignmentTurnedIn,
                        iconColor = MaterialTheme.colorScheme.secondary,
                        title = "Mock Exams Attempted",
                        value = "$attemptedMocks / $totalMocks Mocks",
                        subtext = "${(attemptedMocks.toFloat() / totalMocks.toFloat() * 100).toInt()}% mock target"
                    )
                }
            }
        }
    }
}

@Composable
fun ProgressMetricItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    title: String,
    value: String,
    subtext: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .background(iconColor.copy(alpha = 0.15f), RoundedCornerShape(6.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(16.dp)
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "• $subtext",
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 9.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
        }
    }
}

// -------------------------------------------------------------
// Component 2: Phase Completion Bar Chart (Recharts Grouped Bar Style)
// -------------------------------------------------------------
@Composable
fun PhaseCompletionBarChartCard(dayPlans: List<DayPlan>) {
    // 4 Phases
    data class PhaseStat(
        val name: String,
        val phaseNum: Int,
        val daySpan: String,
        val targetBlocks: Int,
        val completedBlocks: Int
    )

    val phases = listOf(
        PhaseStat(
            name = "Phase 1: Foundation",
            phaseNum = 1,
            daySpan = "Days 1-8",
            targetBlocks = 80,
            completedBlocks = dayPlans.filter { it.phase == 1 }.sumOf { it.completedTaskCount() }
        ),
        PhaseStat(
            name = "Phase 2: Speed & Depth",
            phaseNum = 2,
            daySpan = "Days 9-16",
            targetBlocks = 80,
            completedBlocks = dayPlans.filter { it.phase == 2 }.sumOf { it.completedTaskCount() }
        ),
        PhaseStat(
            name = "Phase 3: High Yield",
            phaseNum = 3,
            daySpan = "Days 17-24",
            targetBlocks = 80,
            completedBlocks = dayPlans.filter { it.phase == 3 }.sumOf { it.completedTaskCount() }
        ),
        PhaseStat(
            name = "Phase 4: Full Revision",
            phaseNum = 4,
            daySpan = "Days 25-30",
            targetBlocks = 60,
            completedBlocks = dayPlans.filter { it.phase == 4 }.sumOf { it.completedTaskCount() }
        )
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .testTag("phase_completion_chart_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.BarChart,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Phase Goal vs Actual",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Legend
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Target",
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(MaterialTheme.colorScheme.primary, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Done",
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Render Grouped Bar Chart with Canvas
            val primaryColor = MaterialTheme.colorScheme.primary
            val targetColor = MaterialTheme.colorScheme.surfaceVariant
            val gridColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
            val textColor = MaterialTheme.colorScheme.onSurfaceVariant

            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                phases.forEach { phase ->
                    val percentage = if (phase.targetBlocks > 0) {
                        (phase.completedBlocks.toFloat() / phase.targetBlocks.toFloat()).coerceIn(0f, 1f)
                    } else 0f

                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${phase.name} (${phase.daySpan})",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "${phase.completedBlocks}/${phase.targetBlocks} (${(percentage * 100).toInt()}%)",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (percentage >= 1f) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))

                        // Double bar progress (Stacked / dual bar look)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(12.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(targetColor)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(percentage)
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(
                                        Brush.horizontalGradient(
                                            listOf(
                                                MaterialTheme.colorScheme.primary,
                                                MaterialTheme.colorScheme.tertiary
                                            )
                                        )
                                    )
                            )
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// Component 3: 30-Day Daily Block Distribution Chart
// -------------------------------------------------------------
@Composable
fun DailyBlockDistributionChartCard(
    dayPlans: List<DayPlan>,
    selectedDay: DayPlan?,
    onSelectDay: (DayPlan?) -> Unit,
    onNavigateToDay: (Int) -> Unit
) {
    val scrollState = rememberScrollState()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .testTag("daily_distribution_chart_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.ViewWeek,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Daily Block Heatmap (Days 1–30)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = "Goal: 10/day",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Text(
                text = "Tap any bar to inspect completion details",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Scrollable 30-Day Columns
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState)
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                dayPlans.sortedBy { it.dayNumber }.forEach { plan ->
                    val count = plan.completedTaskCount()
                    val isSelected = selectedDay?.dayNumber == plan.dayNumber
                    val barHeight = (count.toFloat() / 10f * 90f).coerceAtLeast(6f)

                    val barColor = when {
                        count == 10 -> MaterialTheme.colorScheme.tertiary
                        count >= 7 -> MaterialTheme.colorScheme.primary
                        count >= 4 -> Color(0xFFF59E0B) // Amber
                        count > 0 -> Color(0xFFEC4899) // Pink
                        else -> MaterialTheme.colorScheme.surfaceVariant
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable { onSelectDay(if (isSelected) null else plan) }
                            .padding(horizontal = 2.dp)
                    ) {
                        // Value label
                        Text(
                            text = "$count",
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 9.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        // Column Bar
                        Box(
                            modifier = Modifier
                                .width(18.dp)
                                .height(90.dp),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            // Target guide background bar
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
                            )

                            // Actual completed height
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(barHeight.dp)
                                    .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                    .background(if (isSelected) MaterialTheme.colorScheme.secondary else barColor)
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // Day number label
                        Text(
                            text = "D${plan.dayNumber}",
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 9.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Interactive Tooltip Card when a day bar is clicked
            if (selectedDay != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Day ${selectedDay.dayNumber}: ${selectedDay.title}",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Text(
                                text = "${selectedDay.completedTaskCount()} of 10 tasks completed (${(selectedDay.progressPercent() * 100).toInt()}%)",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
                            )
                        }
                        Button(
                            onClick = { onNavigateToDay(selectedDay.dayNumber) },
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(text = "Open Day", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// Component 4: Subject-wise Mastery Progress Breakdown
// -------------------------------------------------------------
@Composable
fun SubjectMasteryBreakdownCard(dayPlans: List<DayPlan>) {
    data class SubjectStat(
        val name: String,
        val icon: androidx.compose.ui.graphics.vector.ImageVector,
        val color: Color,
        val completedCount: Int,
        val totalCount: Int = 30
    )

    val stats = listOf(
        SubjectStat(
            name = "Quantitative Aptitude",
            icon = Icons.Default.Calculate,
            color = Color(0xFF2563EB),
            completedCount = dayPlans.count { it.quantCompleted }
        ),
        SubjectStat(
            name = "Reasoning Ability",
            icon = Icons.Default.Psychology,
            color = Color(0xFF7C3AED),
            completedCount = dayPlans.count { it.reasoningCompleted }
        ),
        SubjectStat(
            name = "Full Mock Tests (Tier-1)",
            icon = Icons.Default.Assignment,
            color = Color(0xFF059669),
            completedCount = dayPlans.count { it.mockCompleted }
        ),
        SubjectStat(
            name = "Mock Error Analysis",
            icon = Icons.Default.Troubleshoot,
            color = Color(0xFFD97706),
            completedCount = dayPlans.count { it.mockAnalysisCompleted }
        ),
        SubjectStat(
            name = "English Comprehension",
            icon = Icons.Default.MenuBook,
            color = Color(0xFF0284C7),
            completedCount = dayPlans.count { it.englishCompleted }
        ),
        SubjectStat(
            name = "General Awareness",
            icon = Icons.Default.Public,
            color = Color(0xFFDC2626),
            completedCount = dayPlans.count { it.gaCompleted }
        ),
        SubjectStat(
            name = "Formula & Rule Revision",
            icon = Icons.Default.AutoFixHigh,
            color = Color(0xFFDB2777),
            completedCount = dayPlans.count { it.revisionCompleted }
        )
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .testTag("subject_breakdown_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Category,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Subject-Wise Block Completion",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = "Each subject has 30 scheduled focus blocks",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                stats.forEach { stat ->
                    val progress = (stat.completedCount.toFloat() / stat.totalCount.toFloat()).coerceIn(0f, 1f)
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(22.dp)
                                        .background(stat.color.copy(alpha = 0.15f), RoundedCornerShape(4.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = stat.icon,
                                        contentDescription = null,
                                        tint = stat.color,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = stat.name,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Text(
                                text = "${stat.completedCount}/${stat.totalCount} (${(progress * 100).toInt()}%)",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = stat.color
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(7.dp)
                                .clip(RoundedCornerShape(3.5.dp)),
                            color = stat.color,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// Component 5: Mock Test Performance Overview Card
// -------------------------------------------------------------
@Composable
fun MockTestPerformanceSummaryCard(
    mockTests: List<MockTest>,
    onNavigateToMocks: () -> Unit
) {
    val attemptedMocks = mockTests.filter { it.isAttempted }
    val averageScore = if (attemptedMocks.isNotEmpty()) {
        attemptedMocks.map { it.totalScore }.average().toFloat()
    } else 0f
    val highestScore = attemptedMocks.maxOfOrNull { it.totalScore } ?: 0f

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .testTag("mock_test_summary_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Analytics,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Mock Exam Milestone",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                FilledTonalButton(
                    onClick = onNavigateToMocks,
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp)
                ) {
                    Text(text = "View All Mocks", style = MaterialTheme.typography.labelSmall)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Stat 1: Attempted
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "ATTEMPTED",
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${attemptedMocks.size}/30",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                // Stat 2: Average Score
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "AVG SCORE",
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (attemptedMocks.isNotEmpty()) "${String.format("%.1f", averageScore)}" else "--",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }

                // Stat 3: Highest Score
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "BEST SCORE",
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (highestScore > 0f) "${String.format("%.1f", highestScore)}" else "--",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                }
            }
        }
    }
}
