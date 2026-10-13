package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DayPlan
import com.example.data.model.MockTest
import com.example.ui.theme.*

/**
 * 4 Primary SSC CGL Subjects with specific metadata and Tier-1 exam parameters.
 */
enum class CglSubject(
    val key: String,
    val displayName: String,
    val shortName: String,
    val icon: ImageVector,
    val primaryColor: Color,
    val containerColor: Color,
    val targetScore: Float = 40f,
    val maxScore: Float = 50f,
    val highYieldTips: List<String>
) {
    MATHS(
        key = "quant",
        displayName = "Quantitative Aptitude (Maths)",
        shortName = "Maths",
        icon = Icons.Default.Calculate,
        primaryColor = Color(0xFF4F46E5), // Indigo
        containerColor = Color(0xFFEEF2FF),
        highYieldTips = listOf(
            "Geometry: Circle tangents, chords, cyclic quadrilaterals & triangle similarities",
            "Trigonometry: Standard angles table & identity transformations (sin²θ+cos²θ=1)",
            "Algebra: Polynomial identities (a³+b³+c³-3abc) & reciprocal equations (x+1/x=k)",
            "Arithmetic Speed: Practice mental calculation & percentage fraction equivalents"
        )
    ),
    ENGLISH(
        key = "english",
        displayName = "English Comprehension",
        shortName = "English",
        icon = Icons.Default.MenuBook,
        primaryColor = Color(0xFF0D9488), // Teal
        containerColor = Color(0xFFF0FDFA),
        highYieldTips = listOf(
            "Golden Grammar Rules: Subject-Verb agreement, inversion & conditional sentences",
            "Active-Passive & Direct-Indirect Speech: High scoring 5-6 questions in Tier-1",
            "Vocabulary: Previous year 500 idioms, phrases & one-word substitutions",
            "Reading Comprehension: Scan question keywords first before reading passages"
        )
    ),
    REASONING(
        key = "reasoning",
        displayName = "General Intelligence & Reasoning",
        shortName = "Reasoning",
        icon = Icons.Default.Psychology,
        primaryColor = Color(0xFF7C3AED), // Violet
        containerColor = Color(0xFFF5F3FF),
        highYieldTips = listOf(
            "Syllogism: Master 'Only a few' & 'Some not' logic using Venn diagrams",
            "Number Series & Analogies: Check differences, n²±1, n³±1 & prime number patterns",
            "Non-Verbal: Embedded figures, paper folding symmetry & mirror reflections",
            "Coding-Decoding: Memorize alphabet ranks (EJOTY) & reverse pairs (AZ, BY...)"
        )
    ),
    GS(
        key = "ga",
        displayName = "General Studies (GS / GK)",
        shortName = "GS",
        icon = Icons.Default.Public,
        primaryColor = Color(0xFFEA580C), // Orange
        containerColor = Color(0xFFFFF7ED),
        highYieldTips = listOf(
            "Indian Polity: Articles 12-51A (Fundamental Rights, DPSP, Fundamental Duties)",
            "Modern History: 1857-1947 timeline, INC sessions & Governor-Generals",
            "Geography: Indian river origins, tributaries, national parks & mountain passes",
            "General Science: Chemistry common names/formulas, Biology vitamins & Physics units"
        )
    );

    fun getCompletedBlocks(dayPlans: List<DayPlan>): Int = when (this) {
        MATHS -> dayPlans.count { it.quantCompleted }
        ENGLISH -> dayPlans.count { it.englishCompleted }
        REASONING -> dayPlans.count { it.reasoningCompleted }
        GS -> dayPlans.count { it.gaCompleted }
    }

    fun getTopicForDay(dayPlan: DayPlan): String = when (this) {
        MATHS -> dayPlan.quantTopic
        ENGLISH -> dayPlan.englishTopic
        REASONING -> dayPlan.reasoningTopic
        GS -> dayPlan.gaTopic
    }

    fun isBlockDoneForDay(dayPlan: DayPlan): Boolean = when (this) {
        MATHS -> dayPlan.quantCompleted
        ENGLISH -> dayPlan.englishCompleted
        REASONING -> dayPlan.reasoningCompleted
        GS -> dayPlan.gaCompleted
    }

    fun getScoresFromMocks(mockTests: List<MockTest>): List<Float> = mockTests.filter { it.isAttempted }.map { mock ->
        when (this) {
            MATHS -> mock.quantScore
            ENGLISH -> mock.englishScore
            REASONING -> mock.reasoningScore
            GS -> mock.gaScore
        }
    }
}

/**
 * Comprehensive Subject Performance Drill-down Component for SSC CGL Tier-1.
 * Allows switching between Maths, English, Reasoning, and GS to inspect
 * sectional mock scores, syllabus completion %, phase breakdown, error logs,
 * and high-yield revision topics.
 */
@Composable
fun SubjectDrillDownCard(
    dayPlans: List<DayPlan>,
    mockTests: List<MockTest>,
    modifier: Modifier = Modifier,
    onNavigateToDay: (Int) -> Unit = {}
) {
    var selectedSubject by remember { mutableStateOf(CglSubject.MATHS) }
    val attemptedMocks = remember(mockTests) { mockTests.filter { it.isAttempted }.sortedBy { it.testNumber } }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .testTag("subject_drill_down_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = PureWhiteSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, LavenderOutline)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // Header
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
                                imageVector = Icons.Default.Tune,
                                contentDescription = null,
                                tint = LavenderPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Subject Performance Drill-Down",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Sectional Metrics • Maths, English, Reasoning, GS",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = LightLavenderBackground
                ) {
                    Text(
                        text = "Tier-1 Breakdown",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = LavenderPrimary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 4 Subject Selection Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CglSubject.entries.forEach { subject ->
                    val isSelected = selectedSubject == subject
                    val completedBlocks = subject.getCompletedBlocks(dayPlans)
                    val scores = subject.getScoresFromMocks(mockTests)
                    val avg = if (scores.isNotEmpty()) scores.average().toFloat() else 0f

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) subject.primaryColor else subject.containerColor,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) subject.primaryColor else subject.primaryColor.copy(alpha = 0.3f)
                        ),
                        modifier = Modifier
                            .clickable { selectedSubject = subject }
                            .testTag("tab_subject_${subject.key}")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = subject.icon,
                                contentDescription = null,
                                tint = if (isSelected) Color.White else subject.primaryColor,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(
                                    text = subject.shortName,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else subject.primaryColor
                                )
                                Text(
                                    text = if (scores.isNotEmpty()) "${String.format("%.1f", avg)}/50" else "$completedBlocks/30 blk",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 10.sp,
                                    color = if (isSelected) Color.White.copy(alpha = 0.85f) else Color(0xFF64748B)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Detailed Drill-Down View for Selected Subject
            SubjectDetailPanel(
                subject = selectedSubject,
                dayPlans = dayPlans,
                attemptedMocks = attemptedMocks,
                onNavigateToDay = onNavigateToDay
            )
        }
    }
}

@Composable
fun SubjectDetailPanel(
    subject: CglSubject,
    dayPlans: List<DayPlan>,
    attemptedMocks: List<MockTest>,
    onNavigateToDay: (Int) -> Unit
) {
    val scores = remember(subject, attemptedMocks) { subject.getScoresFromMocks(attemptedMocks) }
    val avgScore = if (scores.isNotEmpty()) scores.average().toFloat() else 0f
    val peakScore = if (scores.isNotEmpty()) scores.maxOrNull() ?: 0f else 0f
    val latestScore = if (scores.isNotEmpty()) scores.last() else 0f

    val completedBlocks = subject.getCompletedBlocks(dayPlans)
    val totalBlocks = 30
    val blockCompletionPercent = (completedBlocks.toFloat() / totalBlocks.toFloat()).coerceIn(0f, 1f)

    // Readiness status
    val readinessStatus = when {
        avgScore >= 42f -> "Strong Master (42+)"
        avgScore >= 38f -> "Cutoff Safe (38-42)"
        avgScore >= 30f -> "Needs Push (30-38)"
        scores.isNotEmpty() -> "Under Target (<30)"
        else -> "Pending Attempt"
    }

    val statusColor = when {
        avgScore >= 38f -> EmeraldSuccess
        avgScore >= 30f -> AmberAccent
        scores.isNotEmpty() -> RoseWarning
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        // Banner Card: Subject Name + Exam Target Readiness
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = subject.containerColor,
            border = androidx.compose.foundation.BorderStroke(1.dp, subject.primaryColor.copy(alpha = 0.25f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = subject.displayName,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = subject.primaryColor
                    )
                    Text(
                        text = "Weightage: 25 Questions • 50 Marks (Target: 40+)",
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 11.sp,
                        color = Color(0xFF475569)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = statusColor.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = readinessStatus,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = statusColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 4 KPI Metrics Grid
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Metric 1: Average Score
            SubjectKpiBox(
                label = "AVG SCORE",
                value = if (scores.isNotEmpty()) String.format("%.1f", avgScore) else "--",
                unit = "/50",
                subtitle = if (avgScore >= 40f) "Above 40 Cutoff" else "Target: 40+",
                color = subject.primaryColor,
                modifier = Modifier.weight(1f)
            )

            // Metric 2: Peak Score
            SubjectKpiBox(
                label = "BEST SCORE",
                value = if (scores.isNotEmpty()) String.format("%.1f", peakScore) else "--",
                unit = "/50",
                subtitle = "Personal Record",
                color = Color(0xFF059669),
                modifier = Modifier.weight(1f)
            )

            // Metric 3: Latest Score
            val latestDelta = if (scores.size > 1) latestScore - scores[scores.size - 2] else null
            SubjectKpiBox(
                label = "LATEST SCORE",
                value = if (scores.isNotEmpty()) String.format("%.1f", latestScore) else "--",
                unit = "/50",
                subtitle = if (latestDelta != null) {
                    val sign = if (latestDelta >= 0) "+" else ""
                    "$sign${String.format("%.1f", latestDelta)} pts"
                } else "Last attempt",
                color = if (latestDelta != null && latestDelta >= 0) Color(0xFF059669) else Color(0xFFDC2626),
                modifier = Modifier.weight(1f)
            )

            // Metric 4: Syllabus Blocks Done
            SubjectKpiBox(
                label = "SYLLABUS",
                value = "$completedBlocks",
                unit = "/30",
                subtitle = "${(blockCompletionPercent * 100).toInt()}% Done",
                color = Color(0xFF7C3AED),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Sectional Score Progression Trend Line for this Subject
        Text(
            text = "${subject.shortName} Mock Score Progression (/50):",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(6.dp))

        if (scores.isEmpty()) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = LightLavenderBackground,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text(
                    text = "No mock scores logged yet. Log test sessions to see ${subject.shortName} score progression.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(14.dp),
                    textAlign = TextAlign.Center
                )
            }
        } else {
            SubjectScoreMiniChart(
                scores = scores,
                subject = subject
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 3-Phase Mastery Progress
        Text(
            text = "3-Phase Syllabus Mastery:",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(6.dp))

        val phase1Plans = dayPlans.filter { it.phase == 1 }
        val phase2Plans = dayPlans.filter { it.phase == 2 }
        val phase3Plans = dayPlans.filter { it.phase == 3 }

        val phase1Done = phase1Plans.count { subject.isBlockDoneForDay(it) }
        val phase2Done = phase2Plans.count { subject.isBlockDoneForDay(it) }
        val phase3Done = phase3Plans.count { subject.isBlockDoneForDay(it) }

        PhaseMiniProgressBar("Phase 1: Foundation (Days 1-10)", phase1Done, phase1Plans.size, subject.primaryColor)
        PhaseMiniProgressBar("Phase 2: Speed & Depth (Days 11-20)", phase2Done, phase2Plans.size, subject.primaryColor)
        PhaseMiniProgressBar("Phase 3: Simulation (Days 21-30)", phase3Done, phase3Plans.size, subject.primaryColor)

        Spacer(modifier = Modifier.height(14.dp))

        // Weak Topics & Error Notebook Insights for this subject
        val relevantWeakTopics = attemptedMocks
            .filter { it.weakTopics.isNotBlank() }
            .map { "Mock #${it.testNumber}: ${it.weakTopics}" }

        if (relevantWeakTopics.isNotEmpty()) {
            Text(
                text = "Weak Areas Logged in Mock Sessions:",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = RoseWarning
            )
            Spacer(modifier = Modifier.height(4.dp))

            Surface(
                shape = RoundedCornerShape(10.dp),
                color = RoseWarning.copy(alpha = 0.08f),
                border = androidx.compose.foundation.BorderStroke(1.dp, RoseWarning.copy(alpha = 0.2f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    relevantWeakTopics.takeLast(3).forEach { note ->
                        Row(
                            modifier = Modifier.padding(vertical = 2.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(text = "• ", color = RoseWarning, fontWeight = FontWeight.Bold)
                            Text(
                                text = note,
                                style = MaterialTheme.typography.bodySmall,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        // High-Yield Revision Focus Areas
        Text(
            text = "High-Yield Tier-1 Exam Priorities:",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(6.dp))

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            subject.highYieldTips.forEach { tip ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = subject.primaryColor,
                        modifier = Modifier
                            .size(14.dp)
                            .padding(top = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = tip,
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Next Upcoming Study Topic Shortcut
        val nextPendingDay = dayPlans.firstOrNull { !subject.isBlockDoneForDay(it) }
        if (nextPendingDay != null) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = LightLavenderBackground,
                border = androidx.compose.foundation.BorderStroke(1.dp, LavenderOutline),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Next Target Topic (Day ${nextPendingDay.dayNumber}):",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = LavenderPrimary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = subject.getTopicForDay(nextPendingDay),
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    FilledTonalButton(
                        onClick = { onNavigateToDay(nextPendingDay.dayNumber) },
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Day ${nextPendingDay.dayNumber}", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(12.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun SubjectKpiBox(
    label: String,
    value: String,
    unit: String,
    subtitle: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = color.copy(alpha = 0.08f),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.2f)),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                fontSize = 8.5.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Spacer(modifier = Modifier.height(2.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = color
                )
                Text(
                    text = unit,
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 9.sp,
                    color = color.copy(alpha = 0.7f),
                    modifier = Modifier.padding(bottom = 2.dp, start = 1.dp)
                )
            }
            Spacer(modifier = Modifier.height(1.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                fontSize = 8.5.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun SubjectScoreMiniChart(
    scores: List<Float>,
    subject: CglSubject
) {
    var selectedPointIndex by remember(scores) { mutableStateOf(if (scores.isNotEmpty()) scores.lastIndex else -1) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .background(LightLavenderBackground.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
            .border(1.dp, LavenderOutline.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .testTag("subject_mini_chart_canvas")
                .pointerInput(scores) {
                    detectTapGestures { offset ->
                        if (scores.isEmpty()) return@detectTapGestures
                        val padStart = 24f
                        val padEnd = 24f
                        val plotW = size.width - padStart - padEnd
                        val step = if (scores.size > 1) plotW / (scores.size - 1) else plotW
                        val relX = (offset.x - padStart).coerceIn(0f, plotW)
                        val idx = (relX / step).toInt().coerceIn(0, scores.lastIndex)
                        selectedPointIndex = if (idx < scores.lastIndex && (relX - idx * step) > (step / 2)) idx + 1 else idx
                    }
                }
        ) {
            val chartW = size.width
            val chartH = size.height
            val padStart = 26f
            val padEnd = 20f
            val padTop = 16f
            val padBottom = 20f

            val plotW = chartW - padStart - padEnd
            val plotH = chartH - padTop - padBottom
            val maxCap = 50f

            // Target 40 Guideline
            val targetY = padTop + (1f - (40f / maxCap)) * plotH
            drawLine(
                color = AmberAccent,
                start = Offset(padStart, targetY),
                end = Offset(chartW - padEnd, targetY),
                strokeWidth = 1.5f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f)
            )

            // Baseline 25 Guideline
            val midY = padTop + (1f - (25f / maxCap)) * plotH
            drawLine(
                color = Color(0xFF94A3B8).copy(alpha = 0.3f),
                start = Offset(padStart, midY),
                end = Offset(chartW - padEnd, midY),
                strokeWidth = 1f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 8f), 0f)
            )

            // Coordinates
            val points = scores.mapIndexed { i, score ->
                val x = if (scores.size == 1) padStart + plotW / 2f else padStart + (i.toFloat() / (scores.size - 1).toFloat()) * plotW
                val y = padTop + (1f - (score.coerceIn(0f, 50f) / maxCap)) * plotH
                Offset(x, y)
            }

            // Fill area
            if (points.size >= 2) {
                val fillPath = Path().apply {
                    moveTo(points.first().x, padTop + plotH)
                    lineTo(points.first().x, points.first().y)
                    for (i in 0 until points.size - 1) {
                        val p0 = points[i]
                        val p1 = points[i + 1]
                        val midX = (p0.x + p1.x) / 2f
                        cubicTo(midX, p0.y, midX, p1.y, p1.x, p1.y)
                    }
                    lineTo(points.last().x, padTop + plotH)
                    close()
                }

                drawPath(
                    path = fillPath,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            subject.primaryColor.copy(alpha = 0.25f),
                            Color.Transparent
                        ),
                        startY = padTop,
                        endY = padTop + plotH
                    )
                )

                // Curve Line
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
                    color = subject.primaryColor,
                    style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                )
            }

            // Cursor line for selected
            if (selectedPointIndex in points.indices) {
                val selPt = points[selectedPointIndex]
                drawLine(
                    color = subject.primaryColor.copy(alpha = 0.4f),
                    start = Offset(selPt.x, padTop),
                    end = Offset(selPt.x, padTop + plotH),
                    strokeWidth = 1.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 4f), 0f)
                )
            }

            // Draw Dots
            points.forEachIndexed { idx, pt ->
                val isSel = idx == selectedPointIndex
                if (isSel) {
                    drawCircle(color = subject.primaryColor.copy(alpha = 0.25f), radius = 10.dp.toPx(), center = pt)
                }
                drawCircle(color = Color.White, radius = if (isSel) 5.5.dp.toPx() else 4.dp.toPx(), center = pt)
                drawCircle(color = subject.primaryColor, radius = if (isSel) 3.5.dp.toPx() else 2.5.dp.toPx(), center = pt)
            }
        }

        // Top-right label for 40 Cutoff
        Text(
            text = "Target: 40/50",
            style = MaterialTheme.typography.labelSmall,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = AmberAccent,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 4.dp, top = 2.dp)
        )

        // Selected score indicator in bottom
        if (selectedPointIndex in scores.indices) {
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = subject.primaryColor,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(4.dp)
            ) {
                Text(
                    text = "Mock #${selectedPointIndex + 1}: ${String.format("%.1f", scores[selectedPointIndex])} / 50",
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
    }
}

@Composable
fun PhaseMiniProgressBar(
    title: String,
    completed: Int,
    total: Int,
    color: Color
) {
    val progress = if (total > 0) (completed.toFloat() / total.toFloat()).coerceIn(0f, 1f) else 0f
    Column(modifier = Modifier.padding(vertical = 3.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                fontSize = 10.5.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "$completed / $total (${(progress * 100).toInt()}%)",
                style = MaterialTheme.typography.labelSmall,
                fontSize = 10.5.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = color,
            trackColor = LightLavenderBackground
        )
    }
}
