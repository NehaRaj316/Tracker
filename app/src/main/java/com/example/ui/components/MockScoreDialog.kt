package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.MockTest
import com.example.ui.theme.*

/**
 * Reusable dialog to log or update mock test session scores directly in the Room database.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MockScoreDialog(
    mockTest: MockTest,
    availableTests: List<MockTest> = emptyList(),
    allowChangeTestNumber: Boolean = false,
    onDismiss: () -> Unit,
    onSave: (MockTest) -> Unit,
    onReset: ((Int) -> Unit)? = null
) {
    var selectedTestNumber by remember { mutableIntStateOf(mockTest.testNumber) }
    val currentTargetMock = remember(selectedTestNumber, availableTests, mockTest) {
        availableTests.find { it.testNumber == selectedTestNumber } ?: mockTest
    }

    var provider by remember(currentTargetMock) { mutableStateOf(currentTargetMock.provider.ifBlank { "Testbook" }) }
    var quantStr by remember(currentTargetMock) { mutableStateOf(if (currentTargetMock.isAttempted && currentTargetMock.quantScore > 0f) currentTargetMock.quantScore.toString() else "") }
    var reasoningStr by remember(currentTargetMock) { mutableStateOf(if (currentTargetMock.isAttempted && currentTargetMock.reasoningScore > 0f) currentTargetMock.reasoningScore.toString() else "") }
    var englishStr by remember(currentTargetMock) { mutableStateOf(if (currentTargetMock.isAttempted && currentTargetMock.englishScore > 0f) currentTargetMock.englishScore.toString() else "") }
    var gaStr by remember(currentTargetMock) { mutableStateOf(if (currentTargetMock.isAttempted && currentTargetMock.gaScore > 0f) currentTargetMock.gaScore.toString() else "") }

    var attemptedStr by remember(currentTargetMock) { mutableStateOf(if (currentTargetMock.isAttempted && currentTargetMock.attemptedQuestions > 0) currentTargetMock.attemptedQuestions.toString() else "") }
    var correctStr by remember(currentTargetMock) { mutableStateOf(if (currentTargetMock.isAttempted && currentTargetMock.correctAnswers > 0) currentTargetMock.correctAnswers.toString() else "") }
    var wrongStr by remember(currentTargetMock) { mutableStateOf(if (currentTargetMock.isAttempted && currentTargetMock.wrongAnswers > 0) currentTargetMock.wrongAnswers.toString() else "") }
    var timeTakenStr by remember(currentTargetMock) { mutableStateOf(if (currentTargetMock.isAttempted) currentTargetMock.timeTakenMinutes.toString() else "60") }

    var errorAnalysis by remember(currentTargetMock) { mutableStateOf(currentTargetMock.errorAnalysis) }
    var weakTopics by remember(currentTargetMock) { mutableStateOf(currentTargetMock.weakTopics) }

    val providers = listOf("Testbook", "Oliveboard", "PYQ 2024 Shift", "SuperCoaching", "Adda247", "Gradeup/BYJU's", "Other")

    val quantVal = quantStr.toFloatOrNull() ?: 0f
    val reasoningVal = reasoningStr.toFloatOrNull() ?: 0f
    val englishVal = englishStr.toFloatOrNull() ?: 0f
    val gaVal = gaStr.toFloatOrNull() ?: 0f
    val totalScore = quantVal + reasoningVal + englishVal + gaVal

    val attemptedVal = attemptedStr.toIntOrNull() ?: 0
    val correctVal = correctStr.toIntOrNull() ?: 0
    val wrongVal = wrongStr.toIntOrNull() ?: 0
    val calculatedAccuracy = if (attemptedVal > 0) {
        ((correctVal.toFloat() / attemptedVal.toFloat()) * 100f).coerceIn(0f, 100f)
    } else 0f

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .fillMaxHeight(0.92f)
                .testTag("mock_score_dialog"),
            shape = RoundedCornerShape(24.dp),
            color = PureWhiteSurface,
            tonalElevation = 6.dp,
            border = androidx.compose.foundation.BorderStroke(1.dp, LavenderOutline)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
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
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Analytics,
                                    contentDescription = null,
                                    tint = LavenderPrimary,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Log Mock Test Score",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Persisted directly to Room Database",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Scrollable Form
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Test Selection Chips (if general selector allowed)
                    if (allowChangeTestNumber && availableTests.isNotEmpty()) {
                        item {
                            Column {
                                Text(
                                    text = "Select Mock Test (#1 to #30):",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    items(availableTests) { t ->
                                        val isSelected = t.testNumber == selectedTestNumber
                                        FilterChip(
                                            selected = isSelected,
                                            onClick = { selectedTestNumber = t.testNumber },
                                            label = {
                                                Text(
                                                    text = "Mock #${t.testNumber}${if (t.isAttempted) " ✓" else ""}",
                                                    fontSize = 12.sp,
                                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                                )
                                            },
                                            colors = FilterChipDefaults.filterChipColors(
                                                selectedContainerColor = LavenderPrimary,
                                                selectedLabelColor = Color.White
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        item {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = LightLavenderBackground,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Session:",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Mock #${currentTargetMock.testNumber} • ${currentTargetMock.title}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        color = LavenderPrimary
                                    )
                                }
                            }
                        }
                    }

                    // Live Total Score & Performance Banner
                    item {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = LavenderContainer.copy(alpha = 0.6f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, LavenderOutline),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "CALCULATED TOTAL SCORE",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = OnLavenderContainer.copy(alpha = 0.8f)
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Row(verticalAlignment = Alignment.Bottom) {
                                        Text(
                                            text = String.format("%.1f", totalScore),
                                            style = MaterialTheme.typography.headlineMedium,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = LavenderPrimary
                                        )
                                        Text(
                                            text = " / 200",
                                            style = MaterialTheme.typography.titleMedium,
                                            color = OnLavenderContainer.copy(alpha = 0.7f),
                                            modifier = Modifier.padding(bottom = 4.dp, start = 2.dp)
                                        )
                                    }
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    val tierStatus = when {
                                        totalScore >= 150f -> "Safe Target (150+)"
                                        totalScore >= 135f -> "Borderline Cutoff"
                                        totalScore > 0f -> "Needs Push (<135)"
                                        else -> "Pending Attempt"
                                    }
                                    val tierColor = when {
                                        totalScore >= 150f -> EmeraldSuccess
                                        totalScore >= 135f -> AmberAccent
                                        totalScore > 0f -> RoseWarning
                                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = tierColor.copy(alpha = 0.15f)
                                    ) {
                                        Text(
                                            text = tierStatus,
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = tierColor,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }

                                    if (attemptedVal > 0) {
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Accuracy: ${String.format("%.1f", calculatedAccuracy)}%",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Test Provider Selector
                    item {
                        Column {
                            Text(
                                text = "Test Series Platform:",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                providers.forEach { p ->
                                    val isSelected = provider == p
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = { provider = p },
                                        label = { Text(p, fontSize = 12.sp) },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = LavenderSecondary,
                                            selectedLabelColor = Color.White
                                        )
                                    )
                                }
                            }
                        }
                    }

                    // Sectional Scores Inputs (Out of 50 each)
                    item {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Sectional Scores (Max 50 each):",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Target: 40+ each",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = LavenderPrimary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                OutlinedTextField(
                                    value = quantStr,
                                    onValueChange = { quantStr = it },
                                    label = { Text("Quant (/50)", fontSize = 11.sp) },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    singleLine = true,
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("input_quant_score"),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                OutlinedTextField(
                                    value = reasoningStr,
                                    onValueChange = { reasoningStr = it },
                                    label = { Text("Reasoning (/50)", fontSize = 11.sp) },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    singleLine = true,
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("input_reasoning_score"),
                                    shape = RoundedCornerShape(12.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                OutlinedTextField(
                                    value = englishStr,
                                    onValueChange = { englishStr = it },
                                    label = { Text("English (/50)", fontSize = 11.sp) },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    singleLine = true,
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("input_english_score"),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                OutlinedTextField(
                                    value = gaStr,
                                    onValueChange = { gaStr = it },
                                    label = { Text("GA (/50)", fontSize = 11.sp) },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    singleLine = true,
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("input_ga_score"),
                                    shape = RoundedCornerShape(12.dp)
                                )
                            }
                        }
                    }

                    // Question Breakdown
                    item {
                        Column {
                            Text(
                                text = "Question Breakdown (Out of 100 questions):",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = attemptedStr,
                                    onValueChange = { attemptedStr = it },
                                    label = { Text("Attempted", fontSize = 11.sp) },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    singleLine = true,
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("input_attempted_questions"),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                OutlinedTextField(
                                    value = correctStr,
                                    onValueChange = { correctStr = it },
                                    label = { Text("Correct", fontSize = 11.sp) },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    singleLine = true,
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("input_correct_answers"),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                OutlinedTextField(
                                    value = wrongStr,
                                    onValueChange = { wrongStr = it },
                                    label = { Text("Wrong", fontSize = 11.sp) },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    singleLine = true,
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("input_wrong_answers"),
                                    shape = RoundedCornerShape(12.dp)
                                )
                            }
                        }
                    }

                    // Time Taken
                    item {
                        OutlinedTextField(
                            value = timeTakenStr,
                            onValueChange = { timeTakenStr = it },
                            label = { Text("Time Taken (Minutes - standard is 60 min)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_time_taken"),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    // Error Notebook & Weak Topics
                    item {
                        Column {
                            Text(
                                text = "Analysis & Error Notebook:",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = weakTopics,
                                onValueChange = { weakTopics = it },
                                label = { Text("Weak Topics (e.g., Geometry Tangents, Active Voice, Article 32)") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("input_weak_topics"),
                                shape = RoundedCornerShape(12.dp)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = errorAnalysis,
                                onValueChange = { errorAnalysis = it },
                                label = { Text("Error Log, Calculation Sips & Solutions to Revise") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp)
                                    .testTag("input_error_analysis"),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Actions Footer
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (currentTargetMock.isAttempted && onReset != null) {
                        TextButton(
                            onClick = {
                                onReset(currentTargetMock.testNumber)
                                onDismiss()
                            },
                            colors = ButtonDefaults.textButtonColors(contentColor = RoseWarning)
                        ) {
                            Icon(imageVector = Icons.Default.DeleteOutline, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Clear Score")
                        }
                    } else {
                        TextButton(onClick = onDismiss) {
                            Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }

                    Button(
                        onClick = {
                            val updated = currentTargetMock.copy(
                                provider = provider,
                                isAttempted = true,
                                quantScore = quantVal,
                                reasoningScore = reasoningVal,
                                englishScore = englishVal,
                                gaScore = gaVal,
                                totalScore = totalScore,
                                attemptedQuestions = attemptedVal,
                                correctAnswers = correctVal,
                                wrongAnswers = wrongVal,
                                accuracyPercent = calculatedAccuracy,
                                timeTakenMinutes = timeTakenStr.toIntOrNull() ?: 60,
                                dateAttemptedMillis = currentTargetMock.dateAttemptedMillis ?: System.currentTimeMillis(),
                                errorAnalysis = errorAnalysis,
                                weakTopics = weakTopics
                            )
                            onSave(updated)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LavenderPrimary,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("btn_save_mock_score")
                    ) {
                        Icon(imageVector = Icons.Default.Save, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Save Score to Room",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
