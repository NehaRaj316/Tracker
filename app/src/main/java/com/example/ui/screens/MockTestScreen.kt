package com.example.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MockTest
import com.example.ui.viewmodel.PlannerViewModel

@Composable
fun MockTestScreen(
    viewModel: PlannerViewModel,
    modifier: Modifier = Modifier,
    initialTestNumber: Int? = null
) {
    val mockTests by viewModel.mockTests.collectAsState()
    var selectedFilterTab by remember { mutableIntStateOf(0) } // 0: All, 1: Attempted, 2: Pending, 3: Error Notebook
    var editingMockTest by remember { mutableStateOf<MockTest?>(null) }

    LaunchedEffect(initialTestNumber) {
        if (initialTestNumber != null) {
            val target = mockTests.find { it.testNumber == initialTestNumber }
            if (target != null) {
                editingMockTest = target
            }
        }
    }

    val attemptedList = remember(mockTests) { mockTests.filter { it.isAttempted } }
    val avgScore = remember(attemptedList) {
        if (attemptedList.isNotEmpty()) attemptedList.map { it.totalScore }.average().toFloat() else 0f
    }
    val highestScore = remember(attemptedList) {
        if (attemptedList.isNotEmpty()) attemptedList.maxOf { it.totalScore } else 0f
    }
    val avgAccuracy = remember(attemptedList) {
        if (attemptedList.isNotEmpty()) attemptedList.map { it.accuracyPercent }.average().toFloat() else 0f
    }
    val avgQuant = remember(attemptedList) {
        if (attemptedList.isNotEmpty()) attemptedList.map { it.quantScore }.average().toFloat() else 0f
    }
    val avgReasoning = remember(attemptedList) {
        if (attemptedList.isNotEmpty()) attemptedList.map { it.reasoningScore }.average().toFloat() else 0f
    }
    val avgEnglish = remember(attemptedList) {
        if (attemptedList.isNotEmpty()) attemptedList.map { it.englishScore }.average().toFloat() else 0f
    }
    val avgGA = remember(attemptedList) {
        if (attemptedList.isNotEmpty()) attemptedList.map { it.gaScore }.average().toFloat() else 0f
    }

    val displayedMocks = remember(mockTests, selectedFilterTab) {
        when (selectedFilterTab) {
            1 -> mockTests.filter { it.isAttempted }
            2 -> mockTests.filter { !it.isAttempted }
            3 -> mockTests.filter { it.isAttempted && (it.errorAnalysis.isNotBlank() || it.weakTopics.isNotBlank()) }
            else -> mockTests
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("mock_test_screen_column"),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // Performance Summary Header
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Test Series Performance",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${attemptedList.size} of 30 Mocks Completed",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.secondaryContainer
                            ) {
                                Text(
                                    text = "Avg: ${String.format("%.1f", avgScore)}/200",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    val nextPending = mockTests.firstOrNull { !it.isAttempted } ?: mockTests.firstOrNull()
                                    editingMockTest = nextPending
                                },
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                modifier = Modifier.testTag("btn_log_mock_score")
                            ) {
                                Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Log Score", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Key Metrics Grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        MetricBlock(title = "Highest Score", value = "${String.format("%.1f", highestScore)}", subtitle = "out of 200", color = MaterialTheme.colorScheme.primary)
                        MetricBlock(title = "Avg Accuracy", value = "${String.format("%.1f", avgAccuracy)}%", subtitle = "overall", color = MaterialTheme.colorScheme.tertiary)
                        MetricBlock(title = "Mocks Left", value = "${30 - attemptedList.size}", subtitle = "of 30 series", color = MaterialTheme.colorScheme.secondary)
                    }

                    if (attemptedList.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(14.dp))
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Sectional Score Averages (Target: 40+ each)",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        SectionalMiniBar("Quant", avgQuant, MaterialTheme.colorScheme.primary)
                        SectionalMiniBar("Reasoning", avgReasoning, Color(0xFF0D9488))
                        SectionalMiniBar("English", avgEnglish, Color(0xFF7C3AED))
                        SectionalMiniBar("General Awareness", avgGA, Color(0xFFEA580C))
                    }
                }
            }
        }

        // Tabs
        item {
            ScrollableTabRow(
                selectedTabIndex = selectedFilterTab,
                edgePadding = 16.dp,
                containerColor = Color.Transparent,
                divider = {}
            ) {
                listOf(
                    "All 30 Tests",
                    "Attempted (${attemptedList.size})",
                    "Pending (${30 - attemptedList.size})",
                    "Error Notebook"
                ).forEachIndexed { index, title ->
                    Tab(
                        selected = selectedFilterTab == index,
                        onClick = { selectedFilterTab = index },
                        text = {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = if (selectedFilterTab == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        // List of Mock Tests
        items(displayedMocks, key = { it.testNumber }) { mock ->
            MockTestItemCard(
                mockTest = mock,
                onEditClick = { editingMockTest = mock },
                onToggleAttempted = { viewModel.markMockTestCompleted(mock.testNumber, !mock.isAttempted) }
            )
        }
    }

    // Edit / Log Mock Score Dialog
    if (editingMockTest != null) {
        com.example.ui.components.MockScoreDialog(
            mockTest = editingMockTest!!,
            availableTests = mockTests,
            allowChangeTestNumber = true,
            onDismiss = { editingMockTest = null },
            onSave = { updatedMock ->
                viewModel.saveMockTest(
                    testNumber = updatedMock.testNumber,
                    provider = updatedMock.provider,
                    quantScore = updatedMock.quantScore,
                    reasoningScore = updatedMock.reasoningScore,
                    englishScore = updatedMock.englishScore,
                    gaScore = updatedMock.gaScore,
                    attempted = updatedMock.attemptedQuestions,
                    correct = updatedMock.correctAnswers,
                    wrong = updatedMock.wrongAnswers,
                    timeMinutes = updatedMock.timeTakenMinutes,
                    errorAnalysis = updatedMock.errorAnalysis,
                    weakTopics = updatedMock.weakTopics,
                    dateAttemptedMillis = updatedMock.dateAttemptedMillis
                )
                editingMockTest = null
            },
            onReset = { testNum ->
                viewModel.resetMockTest(testNum)
                editingMockTest = null
            }
        )
    }
}

@Composable
fun MetricBlock(
    title: String,
    value: String,
    subtitle: String,
    color: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = title, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = color)
        Text(text = subtitle, style = MaterialTheme.typography.labelSmall, fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f))
    }
}

@Composable
fun SectionalMiniBar(
    section: String,
    score: Float,
    barColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = section,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.width(110.dp)
        )
        LinearProgressIndicator(
            progress = { (score / 50f).coerceIn(0f, 1f) },
            modifier = Modifier
                .weight(1f)
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = barColor,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "${String.format("%.1f", score)}/50",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = barColor
        )
    }
}

@Composable
fun MockTestItemCard(
    mockTest: MockTest,
    onEditClick: () -> Unit,
    onToggleAttempted: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .border(
                0.5.dp,
                if (mockTest.isAttempted) MaterialTheme.colorScheme.tertiary.copy(alpha = 0.6f) else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                RoundedCornerShape(16.dp)
            )
            .testTag("mock_card_${mockTest.testNumber}"),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (mockTest.isAttempted) MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "#${mockTest.testNumber}",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (mockTest.isAttempted) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = mockTest.provider,
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            if (mockTest.isAttempted) {
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f)
                                ) {
                                    Text(
                                        text = "ATTEMPTED",
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
                            text = mockTest.title,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onToggleAttempted,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = if (mockTest.isAttempted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                            contentDescription = if (mockTest.isAttempted) "Mark unattempted" else "Mark attempted",
                            tint = if (mockTest.isAttempted) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Button(
                        onClick = onEditClick,
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = if (mockTest.isAttempted) "Edit Score" else "Log Score",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }

            if (mockTest.isAttempted) {
                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Total Score", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(
                            "${String.format("%.1f", mockTest.totalScore)} / 200",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Column {
                        Text("Accuracy", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(
                            "${String.format("%.1f", mockTest.accuracyPercent)}%",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }

                    Column {
                        Text("Att / Cor / Wr", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(
                            "${mockTest.attemptedQuestions} / ${mockTest.correctAnswers} / ${mockTest.wrongAnswers}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Column {
                        Text("Time Taken", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(
                            "${mockTest.timeTakenMinutes} min",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Sectional badges
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    SectionScoreChip("Quant", mockTest.quantScore, MaterialTheme.colorScheme.primary)
                    SectionScoreChip("Reasoning", mockTest.reasoningScore, Color(0xFF0D9488))
                    SectionScoreChip("English", mockTest.englishScore, Color(0xFF7C3AED))
                    SectionScoreChip("GA", mockTest.gaScore, Color(0xFFEA580C))
                }

                // Error analysis snippet if present
                if (mockTest.errorAnalysis.isNotBlank() || mockTest.weakTopics.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.ErrorOutline,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Error Log & Weak Topics:",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                            if (mockTest.weakTopics.isNotBlank()) {
                                Text(
                                    text = "Weak Areas: ${mockTest.weakTopics}",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            if (mockTest.errorAnalysis.isNotBlank()) {
                                Text(
                                    text = mockTest.errorAnalysis,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
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
fun SectionScoreChip(label: String, score: Float, color: Color) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = color.copy(alpha = 0.12f)
    ) {
        Text(
            text = "$label: ${String.format("%.1f", score)}",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = color,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
        )
    }
}

