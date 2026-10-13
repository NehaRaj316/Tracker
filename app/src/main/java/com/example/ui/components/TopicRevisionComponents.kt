package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.RevisionStatus
import com.example.data.model.TopicRevision

fun getSubjectColor(subject: String): Color {
    return when {
        subject.contains("Quant", ignoreCase = true) -> Color(0xFF2563EB) // Blue
        subject.contains("Reason", ignoreCase = true) -> Color(0xFF0D9488) // Teal
        subject.contains("English", ignoreCase = true) -> Color(0xFF7C3AED) // Purple
        else -> Color(0xFFD97706) // Amber / Orange for GA
    }
}

fun getStatusColor(status: String): Color {
    return when (status) {
        "Mastered" -> Color(0xFF16A34A) // Vibrant Green
        "Needs Review" -> Color(0xFFEA580C) // Warm Amber/Orange
        else -> Color(0xFF64748B) // Slate Gray for Not Started
    }
}

fun getStatusContainerColor(status: String): Color {
    return when (status) {
        "Mastered" -> Color(0xFFDCFCE7) // Soft Green
        "Needs Review" -> Color(0xFFFFEDD5) // Soft Orange
        else -> Color(0xFFF1F5F9) // Soft Slate
    }
}

@Composable
fun TopicStatusSelector(
    currentStatus: String,
    onStatusSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val statuses = listOf("Mastered", "Needs Review", "Not Started")

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
            .padding(3.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        statuses.forEach { status ->
            val isSelected = currentStatus.equals(status, ignoreCase = true)
            val color = getStatusColor(status)
            val containerColor = getStatusContainerColor(status)

            Surface(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(9.dp))
                    .clickable { onStatusSelected(status) }
                    .testTag("status_chip_${status.lowercase().replace(" ", "_")}"),
                color = if (isSelected) containerColor else Color.Transparent,
                shape = RoundedCornerShape(9.dp),
                border = if (isSelected) {
                    androidx.compose.foundation.BorderStroke(1.5.dp, color.copy(alpha = 0.8f))
                } else null
            ) {
                Row(
                    modifier = Modifier
                        .padding(vertical = 8.dp, horizontal = 4.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = when (status) {
                            "Mastered" -> Icons.Default.CheckCircle
                            "Needs Review" -> Icons.Default.ErrorOutline
                            else -> Icons.Outlined.RadioButtonUnchecked
                        },
                        contentDescription = status,
                        tint = if (isSelected) color else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = status,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) color else MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun TopicRevisionCard(
    topic: TopicRevision,
    onStatusChange: (String) -> Unit,
    onNotesChange: (String) -> Unit,
    onDelete: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }
    var isEditingNotes by remember { mutableStateOf(false) }
    var noteInput by remember(topic.notes) { mutableStateOf(topic.notes) }

    val subjectColor = getSubjectColor(topic.subject)
    val statusColor = getStatusColor(topic.status)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .border(
                width = if (topic.status == "Mastered") 1.dp else 0.5.dp,
                color = if (topic.status == "Mastered") statusColor.copy(alpha = 0.5f)
                        else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                shape = RoundedCornerShape(16.dp)
            )
            .testTag("topic_revision_card_${topic.id}"),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header: Day, Subject, and Status Tag
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
                    ) {
                        Text(
                            text = "Day ${topic.dayNumber}",
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = subjectColor.copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = topic.subject,
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = subjectColor,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                        )
                    }
                }

                // Current status badge
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = getStatusContainerColor(topic.status)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(statusColor)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = topic.status,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = statusColor,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Topic Title
            Text(
                text = topic.topicTitle,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 3-way Status Selector: Mastered, Needs Review, Not Started
            TopicStatusSelector(
                currentStatus = topic.status,
                onStatusSelected = { newStatus -> onStatusChange(newStatus) }
            )

            // Notes Section
            if (topic.notes.isNotBlank() && !isEditingNotes) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Default.EditNote,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .size(18.dp)
                                .padding(top = 1.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Revision Notes / Traps to Remember:",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = topic.notes,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        IconButton(
                            onClick = { isEditingNotes = true },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Note",
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Note editor
            AnimatedVisibility(visible = isEditingNotes) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    OutlinedTextField(
                        value = noteInput,
                        onValueChange = { noteInput = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Topic Revision Notes") },
                        placeholder = { Text("E.g., remember formula x² + 1/x² = k² - 2 or tricky exceptions...") },
                        maxLines = 3,
                        shape = RoundedCornerShape(12.dp)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { isEditingNotes = false }) {
                            Text("Cancel")
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Button(
                            onClick = {
                                onNotesChange(noteInput.trim())
                                isEditingNotes = false
                            },
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Save Note")
                        }
                    }
                }
            }

            // Footer actions (Add Note button if no notes, or Delete if provided)
            if (topic.notes.isBlank() && !isEditingNotes) {
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = { isEditingNotes = true },
                        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddComment,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Add Revision Note",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }

                    if (onDelete != null) {
                        IconButton(
                            onClick = onDelete,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.DeleteOutline,
                                contentDescription = "Delete",
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SubjectRevisionRoadmapSummaryCard(
    totalTopics: Int,
    masteredCount: Int,
    needsReviewCount: Int,
    notStartedCount: Int,
    onLogNewTopicClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val masteredPct = if (totalTopics > 0) (masteredCount.toFloat() / totalTopics.toFloat()) else 0f
    val reviewPct = if (totalTopics > 0) (needsReviewCount.toFloat() / totalTopics.toFloat()) else 0f

    Card(
        modifier = modifier
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
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "31-Day Subject Revision Roadmap",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "$masteredCount of $totalTopics Topics Mastered (${(masteredPct * 100).toInt()}%)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                    )
                }

                Button(
                    onClick = onLogNewTopicClick,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Log Revision",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Log Topic",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Multi-segment progress bar
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp)),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Row(modifier = Modifier.fillMaxSize()) {
                    if (masteredPct > 0f) {
                        Box(
                            modifier = Modifier
                                .weight(masteredPct.coerceAtLeast(0.01f))
                                .fillMaxHeight()
                                .background(Color(0xFF16A34A))
                        )
                    }
                    if (reviewPct > 0f) {
                        Box(
                            modifier = Modifier
                                .weight(reviewPct.coerceAtLeast(0.01f))
                                .fillMaxHeight()
                                .background(Color(0xFFEA580C))
                        )
                    }
                    val remainingPct = (1f - masteredPct - reviewPct).coerceAtLeast(0f)
                    if (remainingPct > 0f) {
                        Box(
                            modifier = Modifier
                                .weight(remainingPct.coerceAtLeast(0.01f))
                                .fillMaxHeight()
                                .background(Color(0xFFCBD5E1))
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Stat Badges Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatusStatPill(
                    label = "Mastered",
                    count = masteredCount,
                    color = Color(0xFF16A34A),
                    containerColor = Color(0xFFDCFCE7),
                    icon = Icons.Default.CheckCircle
                )
                StatusStatPill(
                    label = "Needs Review",
                    count = needsReviewCount,
                    color = Color(0xFFEA580C),
                    containerColor = Color(0xFFFFEDD5),
                    icon = Icons.Default.ErrorOutline
                )
                StatusStatPill(
                    label = "Not Started",
                    count = notStartedCount,
                    color = Color(0xFF64748B),
                    containerColor = Color(0xFFF1F5F9),
                    icon = Icons.Outlined.RadioButtonUnchecked
                )
            }
        }
    }
}

@Composable
fun StatusStatPill(
    label: String,
    count: Int,
    color: Color,
    containerColor: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = containerColor,
        modifier = Modifier.padding(horizontal = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Column {
                Text(
                    text = count.toString(),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 9.sp,
                    color = color.copy(alpha = 0.9f)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTopicRevisionDialog(
    initialDayNumber: Int = 1,
    onDismiss: () -> Unit,
    onConfirm: (dayNumber: Int, subject: String, topicTitle: String, status: String, notes: String) -> Unit
) {
    var selectedDay by remember { mutableIntStateOf(initialDayNumber) }
    var selectedSubject by remember { mutableStateOf("Quantitative Aptitude") }
    var topicTitle by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf("Not Started") }
    var notes by remember { mutableStateOf("") }
    var titleError by remember { mutableStateOf(false) }

    val subjects = listOf("Quantitative Aptitude", "Reasoning", "English", "General Awareness")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Log Subject Revision Topic",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Day Selector (1 to 31)
                Text(
                    text = "Select Roadmap Day: Day $selectedDay of 31",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    (1..31).forEach { dayNum ->
                        FilterChip(
                            selected = selectedDay == dayNum,
                            onClick = { selectedDay = dayNum },
                            label = { Text("Day $dayNum") }
                        )
                    }
                }

                // Subject Selector
                Text(
                    text = "Subject",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    subjects.forEach { subj ->
                        FilterChip(
                            selected = selectedSubject == subj,
                            onClick = { selectedSubject = subj },
                            label = { Text(subj) }
                        )
                    }
                }

                // Topic Title Field
                OutlinedTextField(
                    value = topicTitle,
                    onValueChange = {
                        topicTitle = it
                        if (it.isNotBlank()) titleError = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Topic Name / Concept *") },
                    placeholder = { Text("E.g. Geometry: Circles & Tangents") },
                    isError = titleError,
                    supportingText = if (titleError) {
                        { Text("Please enter a topic title") }
                    } else null,
                    singleLine = false,
                    maxLines = 2,
                    shape = RoundedCornerShape(12.dp)
                )

                // Initial Status
                Text(
                    text = "Initial Revision Status",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold
                )

                TopicStatusSelector(
                    currentStatus = selectedStatus,
                    onStatusSelected = { selectedStatus = it }
                )

                // Optional Notes
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Revision Notes / Key Formulas (Optional)") },
                    placeholder = { Text("E.g. PT² = PA × PB, don't confuse tangent with secant") },
                    maxLines = 2,
                    shape = RoundedCornerShape(12.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (topicTitle.isBlank()) {
                        titleError = true
                    } else {
                        onConfirm(selectedDay, selectedSubject, topicTitle.trim(), selectedStatus, notes.trim())
                    }
                },
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Save Topic")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun FormulaRevisionCard(card: com.example.data.model.RevisionCard) {
    val subjectColor = when (card.subject) {
        "Quantitative Aptitude" -> MaterialTheme.colorScheme.primary
        "Reasoning" -> Color(0xFF0D9488)
        "English" -> Color(0xFF7C3AED)
        else -> Color(0xFFEA580C)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .border(
                0.5.dp,
                MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                RoundedCornerShape(16.dp)
            ),
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
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = subjectColor.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = card.category,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = subjectColor,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Text(
                    text = card.subject,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = card.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = card.content,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                lineHeight = 18.sp
            )

            if (card.formulaOrRule != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Key,
                            contentDescription = null,
                            tint = subjectColor,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = card.formulaOrRule,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold,
                            color = subjectColor
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DailySubjectRevisionSection(
    dayNumber: Int,
    topics: List<TopicRevision>,
    onStatusChange: (Long, String) -> Unit,
    onNotesChange: (Long, String) -> Unit,
    onLogTopicClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dayTopics = remember(topics, dayNumber) {
        topics.filter { it.dayNumber == dayNumber }
    }
    val mastered = dayTopics.count { it.status == "Mastered" }
    val review = dayTopics.count { it.status == "Needs Review" }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("daily_subject_revision_section_day_$dayNumber"),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AutoFixHigh,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Day $dayNumber Subject Revisions",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
                ) {
                    Text(
                        text = "$mastered/${dayTopics.size} Mastered",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Mark each subject topic as Mastered, Needs Review, or Not Started for your 31-day roadmap:",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (dayTopics.isEmpty()) {
                Text(
                    text = "No topics logged yet for Day $dayNumber.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                dayTopics.forEach { topic ->
                    val subjectColor = getSubjectColor(topic.subject)
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
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

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = topic.topicTitle,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        TopicStatusSelector(
                            currentStatus = topic.status,
                            onStatusSelected = { newStatus -> onStatusChange(topic.id, newStatus) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(
                    onClick = onLogTopicClick,
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Topic for Day $dayNumber", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}

